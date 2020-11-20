package com.example.internshipassignment;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;

@Database(entities = {CountryModel.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RoomDB extends androidx.room.RoomDatabase {

    private static com.example.internshipassignment.RoomDB database;
    private static final String DATABASE_NAME ="database";

    public synchronized static com.example.internshipassignment.RoomDB getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(), com.example.internshipassignment.RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract MainDAO daoInterface();
}