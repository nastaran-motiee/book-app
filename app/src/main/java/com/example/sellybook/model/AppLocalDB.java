package com.example.sellybook.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.sellybook.MyApplication;

@Database(entities = {Book.class}, version = 36)

abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract BookDao bookDao();
}

public class AppLocalDB {
    static public final AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "booksFile.db")
                    .fallbackToDestructiveMigration()
                    .build();


    private AppLocalDB(){}
}


