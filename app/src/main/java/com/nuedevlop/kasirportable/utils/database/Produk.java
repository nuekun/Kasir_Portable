package com.nuedevlop.kasirportable.utils.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = "produk" , indices =  @Index(value = {"barcode"}, unique = true))
public class Produk implements Parcelable{

    @PrimaryKey(autoGenerate = true) private int idProduk;
    @SerializedName(value = "barcode") private String barcode;
    @SerializedName(value = "nama") private String nama;
    @SerializedName(value = "suplier") private String suplier;
    @SerializedName(value = "jenis") private String jenis ;
    @SerializedName(value = "keterangan") private String keterangan;
    @SerializedName(value = "hargaBeli") private int hargaBeli ;
    @SerializedName(value = "hargaJual") private int hargaJual ;
    @SerializedName(value = "stok") private int stok ;

    protected Produk(Parcel in) {
        idProduk = in.readInt();
        barcode = in.readString();
        nama = in.readString();
        suplier = in.readString();
        jenis = in.readString();
        keterangan = in.readString();
        hargaBeli = in.readInt();
        hargaJual = in.readInt();
        stok = in.readInt();
    }

    public static final Creator<Produk> CREATOR = new Creator<Produk>() {
        @Override
        public Produk createFromParcel(Parcel in) {
            return new Produk(in);
        }

        @Override
        public Produk[] newArray(int size) {
            return new Produk[size];
        }
    };

    public int getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(int idProduk) {
        this.idProduk = idProduk;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getSuplier() {
        return suplier;
    }

    public void setSuplier(String suplier) {
        this.suplier = suplier;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public int getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(int hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    public int getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(int hargaJual) {
        this.hargaJual = hargaJual;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public Produk() {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idProduk);
        dest.writeString(this.barcode);
        dest.writeString(this.nama);
        dest.writeString(this.suplier);
        dest.writeString(this.jenis);
        dest.writeString(this.keterangan);
        dest.writeInt(this.hargaBeli);
        dest.writeInt(this.hargaJual);
        dest.writeInt(this.stok);
    }
}
