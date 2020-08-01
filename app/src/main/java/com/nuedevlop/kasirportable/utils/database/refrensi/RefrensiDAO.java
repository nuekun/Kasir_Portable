package com.nuedevlop.kasirportable.utils.database.refrensi;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RefrensiDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Refrensi... refrensis);

    @Query("SELECT * FROM refrensi WHERE tanggal like '%'||:tanggal||'%' and jenis like '%'||:jenis||'%' ORDER BY idRefrensi DESC")
    List<Refrensi> getAllProses(String tanggal, String jenis);

    @Query("DELETE FROM Refrensi WHERE refrensi = :refrensi")
    void deleteRefrensi(String refrensi);

}
