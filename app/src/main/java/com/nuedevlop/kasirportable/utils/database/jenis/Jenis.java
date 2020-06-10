package com.nuedevlop.kasirportable.utils.database.jenis;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "jenis" , indices =  @Index(value = {"jenisProduk"}, unique = true))
public class Jenis implements Parcelable {

    @PrimaryKey(autoGenerate = true) private int idJenis;
    @SerializedName(value = "jenisProduk",alternate = {"type"}) private String jenisProduk;

    public Jenis(Parcel in) {
        this.idJenis = in.readInt();
        this.jenisProduk = in.readString();
    }

    public static final Creator<Jenis> CREATOR = new Creator<Jenis>() {
        @Override
        public Jenis createFromParcel(Parcel in) {
            return new Jenis(in);
        }

        @Override
        public Jenis[] newArray(int size) {
            return new Jenis[size];
        }
    };

    public Jenis() {

    }

    public int getIdJenis() {
        return idJenis;
    }

    public void setIdJenis(int idJenis) {
        this.idJenis = idJenis;
    }

    public String getJenisProduk() {
        return jenisProduk;
    }

    public void setJenisProduk(String jenisProduk) {
        this.jenisProduk = jenisProduk;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.idJenis);
        dest.writeString(this.jenisProduk);
    }
}
