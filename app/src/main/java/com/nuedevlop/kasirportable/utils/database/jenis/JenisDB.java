package com.nuedevlop.kasirportable.utils.database.jenis;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Jenis.class}, version = 1,exportSchema = false)
public abstract class JenisDB extends RoomDatabase {
    public abstract JenisDAO getJenisDAO();
}