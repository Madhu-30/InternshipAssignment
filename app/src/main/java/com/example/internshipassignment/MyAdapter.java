package com.example.internshipassignment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pixplicity.sharp.Sharp;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyAdapter extends RecyclerView.Adapter <MyAdapter.ProgrammingViewHolder> {

    private final Context context;
    private final List<CountryModel> mList;
    private OkHttpClient httpClient;

    public MyAdapter(Context context, List<CountryModel> list) {
        this.mList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details, parent, false);
        return new ProgrammingViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProgrammingViewHolder holder, int position) {
        CountryModel currentItem = mList.get(position);

        if(currentItem.getName() != null && !currentItem.getName().isEmpty()) {
            holder.country.setText(currentItem.getName());
        }
        else {
            holder.country.setVisibility(View.GONE);
        }

        if(currentItem.getCapital() != null && !currentItem.getCapital().isEmpty()) {
            holder.capital.setText(currentItem.getCapital());
        }
        else {
            holder.capital_l.setVisibility(View.GONE);
        }

        if(currentItem.getRegion() != null && !currentItem.getRegion().isEmpty()) {
            holder.region.setText(currentItem.getRegion());
        }
        else {
            holder.region_l.setVisibility(View.GONE);
        }

        if(currentItem.getSubregion() != null && !currentItem.getSubregion().isEmpty()) {
            holder.sub_region.setText(currentItem.getSubregion());
        }
        else {
            holder.sub_region_l.setVisibility(View.GONE);
        }

        if(String.valueOf(currentItem.getPopulation()) != null && !String.valueOf(currentItem.getPopulation()).isEmpty()) {
            holder.population.setText(String.valueOf(currentItem.getPopulation()));
        }
        else {
            holder.population_l.setVisibility(View.GONE);
        }

        if (currentItem.getFlag() != null && !currentItem.getFlag().isEmpty()) {
            fetchSvg(context, currentItem.getFlag(), holder.flag);
        }

        if(currentItem.getBorders() != null && currentItem.getBorders().size() != 0) {
            StringBuilder borders = new StringBuilder();
            for(int i = 0; i < currentItem.getBorders().size(); i++) {
                if(i == currentItem.getBorders().size()-1) {
                    borders.append(currentItem.getBorders().get(i));
                }
                else {
                    borders.append(currentItem.getBorders().get(i)).append(", ");
                }
            }
            holder.borders.setText(borders.toString());
        }
        else {
            holder.borders_l.setVisibility(View.GONE);
        }

        if(currentItem.getLanguages() != null && currentItem.getLanguages().size() != 0) {
            StringBuilder languages = new StringBuilder();
            for(int i = 0; i < currentItem.getLanguages().size(); i++) {
                if(i == currentItem.getLanguages().size()-1) {
                    languages.append(currentItem.getLanguages().get(i).getName());
                }
                else {
                    languages.append(currentItem.getLanguages().get(i).getName()).append(", ");
                }
            }
            holder.languages.setText(languages.toString());
        }
        else {
            holder.languages_l.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ProgrammingViewHolder extends RecyclerView.ViewHolder {

        LinearLayout capital_l, region_l, sub_region_l, population_l, borders_l, languages_l;
        TextView country, capital, region, sub_region, population, borders, languages;
        ImageView flag;

        private ProgrammingViewHolder(@NonNull View view) {
            super(view);

            country = view.findViewById(R.id.country_name);
            capital = view.findViewById(R.id.capital);
            region = view.findViewById(R.id.region);
            sub_region = view.findViewById(R.id.sub_region);
            population = view.findViewById(R.id.population);
            borders = view.findViewById(R.id.borders);
            languages = view.findViewById(R.id.languages);
            flag = view.findViewById(R.id.flag);

            capital_l = view.findViewById(R.id.capital_layout);
            region_l = view.findViewById(R.id.region_layout);
            sub_region_l = view.findViewById(R.id.sub_region_layout);
            population_l = view.findViewById(R.id.population_layout);
            borders_l = view.findViewById(R.id.borders_layout);
            languages_l = view.findViewById(R.id.languages_layout);

        }
    }

    private void fetchSvg(Context context, String url, final ImageView target) {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .cache(new Cache(context.getCacheDir(), 20 * 1024 * 1024))
                    .build();
        }

        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                InputStream stream = response.body().byteStream();
                Sharp.loadInputStream(stream).into(target);
                stream.close();
            }
        });
    }
}