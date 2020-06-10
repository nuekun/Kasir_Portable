package com.nuedevlop.kasirportable.utils.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RefrensiDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Refrensi... refrensis);

    @Query("SELECT * FROM refrensi ORDER BY idRefrensi DESC")
    List<Refrensi> getAllProses();

    @Query("DELETE FROM Refrensi WHERE refrensi = :refrensi")
    void deleteRefrensi(String refrensi);

}
