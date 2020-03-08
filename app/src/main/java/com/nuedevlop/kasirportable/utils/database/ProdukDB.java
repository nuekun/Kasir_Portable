package com.nuedevlop.kasirportable.utils.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Produk.class}, version = 1,exportSchema = false)
public abstract class ProdukDB extends RoomDatabase {
    public abstract ProdukDAO getProdukDAO();
}
