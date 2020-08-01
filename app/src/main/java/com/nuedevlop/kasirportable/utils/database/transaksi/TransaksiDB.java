package com.nuedevlop.kasirportable.utils.database.transaksi;

import androidx.room.Database;
import androidx.room.RoomDatabase;



@Database(entities = {Transaksi.class}, version = 1,exportSchema = false)
public abstract class TransaksiDB extends RoomDatabase {
    public abstract TransaksiDAO getTransaksiDAO();
}