package com.nuedevlop.kasirportable.utils.database.proses;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface ProsesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Proses... proses);

    @Query("SELECT COUNT(idProses) FROM proses WHERE nama = :nama")
    int getProsesCountbyNama(String nama);

    @Query("SELECT COUNT(idProses) FROM proses")
    int getProsesCount();

    @Query("UPDATE Proses SET jumlah = :jumlah WHERE idProses = :idProses ")
    void updateByID(int jumlah,int idProses);

    @Query("SELECT * FROM Proses")
    List<Proses> getAllProses();

    @Query("DELETE FROM Proses WHERE idProses = :idProses")
    void deleteByID(int idProses);

    @Query("DELETE FROM Proses")
    void deleteALL();

//    @Query("SELECT * FROM produk WHERE barcode = :barcode")
//    List<Produk> getProdukbyBarcode(String barcode);


}
