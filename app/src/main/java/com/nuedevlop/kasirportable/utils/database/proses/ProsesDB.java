package com.nuedevlop.kasirportable.utils.database.proses;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Proses.class}, version = 1,exportSchema = false)
public abstract class ProsesDB extends RoomDatabase {
    public abstract ProsesDAO getProsesDAO();
}
