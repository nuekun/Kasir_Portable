package com.nuedevlop.kasirportable.utils.database.transaksi;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransaksiDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Transaksi... transaksis);

    @Query("SELECT COUNT(idTransaksi) FROM Transaksi ")
    int getCount();

    @Query("SELECT * FROM Transaksi")
    List<Transaksi> getAllProses();

    @Query("SELECT * FROM Transaksi WHERE refrensi = :refrensi")
    List<Transaksi> getDetailByRefrensi(String refrensi);

    @Query("DELETE FROM transaksi WHERE refrensi = :refrensi")
    void deleteRefrensi(String refrensi);

}
