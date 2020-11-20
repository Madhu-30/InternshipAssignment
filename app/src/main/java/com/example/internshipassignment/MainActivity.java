package com.example.internshipassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button delete;
    private TextView textView;
    private List<CountryModel> countryModelArrayList;
    private RoomDB database;
    private IntroPreference pref;
    private MyAdapter adapter;
    private LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = new IntroPreference(this);
        database = RoomDB.getInstance(this);
        textView = findViewById(R.id.none);
        countryModelArrayList = new ArrayList<>();
        delete = findViewById(R.id.deletebtn);
        recyclerView = findViewById(R.id.recyclerview);
        lottie = findViewById(R.id.lottie);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(true);

        delete.setOnClickListener(view -> {
            if(countryModelArrayList.size() > 0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete all data")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Delete", (dialog, which) -> {

                            pref.setIsFirstTimeLaunch(true);
                            database.daoInterface().reset(countryModelArrayList);

                            countryModelArrayList.clear();
                            countryModelArrayList.addAll(database.daoInterface().getAll());
                            adapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            lottie.pauseAnimation();
                        })
                        .setNegativeButton("Cancel",null)
                        .setCancelable(true);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else {
                Toast.makeText(MainActivity.this, "There is no data present", Toast.LENGTH_SHORT).show();
            }
        });


        if(pref.isFirstTimeLaunch()) {
            if(((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null) {
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
            }
            else {
                getDetails();
                pref.setIsFirstTimeLaunch(false);
            }
        }
        else {
            countryModelArrayList = database.daoInterface().getAll();
            adapter = new MyAdapter(MainActivity.this, countryModelArrayList);
            recyclerView.setAdapter(adapter);
        }

    }

    private void getDetails() {
        Call<ArrayList<CountryModel>> call = Retrofit.getInstance().getApiInterface().getCountryList();
        call.enqueue(new Callback<ArrayList<CountryModel>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CountryModel>> arrayListCall, @NonNull Response<ArrayList<CountryModel>> arrayListResponse) {

                if(arrayListResponse.body().size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    database.daoInterface().insert(arrayListResponse.body());

                    countryModelArrayList = database.daoInterface().getAll();
                    adapter = new MyAdapter(MainActivity.this, countryModelArrayList);
                    recyclerView.setAdapter(adapter);
                }
                
                else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<CountryModel>> call, @NonNull Throwable t) {
                recyclerView.setVisibility(View.GONE);
            }
        });
    }
}