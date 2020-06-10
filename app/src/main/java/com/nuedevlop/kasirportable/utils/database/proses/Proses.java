package com.nuedevlop.kasirportable.utils.database.proses;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Proses implements Parcelable {
    @PrimaryKey(autoGenerate = true) private int idProses;
    @SerializedName(value = "nama") private String nama;
    @SerializedName(value = "jenis") private String jenis ;
    @SerializedName(value = "detail") private String detail;
    @SerializedName(value = "idProduk") private int idProduk ;
    @SerializedName(value = "hargaBeli") private int hargaBeli ;
    @SerializedName(value = "hargaJual") private int hargaJual ;
    @SerializedName(value = "jumlah") private int jumlah ;

    public Proses(){}

    public Proses(Parcel in) {
        this.idProses = in.readInt();
        this.nama = in.readString();
        this.jenis = in.readString();
        this.detail = in.readString();
        this.idProduk = in.readInt();
        this.hargaBeli = in.readInt();
        this.hargaJual = in.readInt();
        this.jumlah = in.readInt();
    }

    public static final Creator<Proses> CREATOR = new Creator<Proses>() {
        @Override
        public Proses createFromParcel(Parcel in) {
            return new Proses(in);
        }

        @Override
        public Proses[] newArray(int size) {
            return new Proses[size];
        }
    };

    public int getIdProses() {
        return idProses;
    }

    public void setIdProses(int idProses) {
        this.idProses = idProses;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idProses);
        dest.writeString(this.nama);
        dest.writeString(this.jenis);
        dest.writeString(this.detail);
        dest.writeInt(this.idProduk);
        dest.writeInt(this.hargaBeli);
        dest.writeInt(this.hargaJual);
        dest.writeInt(this.jumlah);
    }
}
