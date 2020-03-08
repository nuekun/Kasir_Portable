package com.nuedevlop.kasirportable.utils.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProdukDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Produk... produk);

    @Query("DELETE FROM produk WHERE idProduk = :idProduk")
    void deleteByidProduk(int idProduk);

    @Query("SELECT COUNT(idProduk) FROM produk WHERE barcode = :barcode")
    int getCountByBarcode(String barcode);

    @Query("SELECT * FROM produk WHERE jenis = :jenis")
    List<Produk> getProdukByJenis(String jenis);

    @Query("SELECT * FROM produk")
    List<Produk> getAllProduk();


}
