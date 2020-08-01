package com.nuedevlop.kasirportable.utils.database.refrensi;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Refrensi.class}, version = 1,exportSchema = false)
public abstract class RefrensiDB extends RoomDatabase {
    public abstract RefrensiDAO getRefrensiDAO();
}