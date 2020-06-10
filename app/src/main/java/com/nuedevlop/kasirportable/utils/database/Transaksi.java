package com.nuedevlop.kasirportable.utils.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;



@Entity
public class Transaksi implements Parcelable {
    @PrimaryKey(autoGenerate = true) private int idTransaksi;
    @SerializedName(value = "nama") private String nama;
    @SerializedName(value = "jenis") private String jenis ;
    @SerializedName(value = "detail") private String detail;
    @SerializedName(value = "tipe") private String tipe;
    @SerializedName(value = "refrensi") private String refrensi;
    @SerializedName(value = "tanggal") private String tanggal;
    @SerializedName(value = "idProduk") private int idProduk ;
    @SerializedName(value = "hargaBeli") private int hargaBeli ;
    @SerializedName(value = "hargaJual") private int hargaJual ;
    @SerializedName(value = "jumlah") private int jumlah ;

    public Transaksi(Parcel in) {
        this.idTransaksi = in.readInt();
        this.nama = in.readString();
        this.jenis = in.readString();
        this.detail = in.readString();
        this.tipe = in.readString();
        this.refrensi = in.readString();
        this.tanggal = in.readString();
        this.idProduk = in.readInt();
        this.hargaBeli = in.readInt();
        this.hargaJual = in.readInt();
        this.jumlah = in.readInt();
    }

    public static final Creator<Transaksi> CREATOR = new Creator<Transaksi>() {
        @Override
        public Transaksi createFromParcel(Parcel in) {
            return new Transaksi(in);
        }

        @Override
        public Transaksi[] newArray(int size) {
            return new Transaksi[size];
        }
    };

    public Transaksi() {

    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getRefrensi() {
        return refrensi;
    }

    public void setRefrensi(String refrensi) {
        this.refrensi = refrensi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(int idProduk) {
        this.idProduk = idProduk;
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

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.idTransaksi);
        parcel.writeString(this.nama);
        parcel.writeString(this.jenis);
        parcel.writeString(this.detail);
        parcel.writeString(this.tipe);
        parcel.writeString(this.refrensi);
        parcel.writeString(this.tanggal);
        parcel.writeInt(this.idProduk);
        parcel.writeInt(this.hargaBeli);
        parcel.writeInt(this.hargaJual);
        parcel.writeInt(this.jumlah);
    }
}
