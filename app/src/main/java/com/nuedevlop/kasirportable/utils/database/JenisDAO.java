package com.nuedevlop.kasirportable.utils.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JenisDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Jenis... jenis);

    @Query("SELECT COUNT(jenisProduk) FROM jenis WHERE jenisProduk= :jenisProduk")
    int getIdCount(String jenisProduk);

    @Query("DELETE FROM jenis WHERE jenisProduk = :jenisProduk")
    void deleteJenis(String jenisProduk);

    @Query("SELECT * FROM jenis ORDER BY JenisProduk ASC")
    List<Jenis> getAllJenis();

}
