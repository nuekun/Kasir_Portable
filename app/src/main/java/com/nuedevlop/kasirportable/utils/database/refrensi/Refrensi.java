package com.nuedevlop.kasirportable.utils.database.refrensi;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "refrensi" , indices =  @Index(value = {"refrensi"}, unique = true))
public class Refrensi implements Parcelable {
    @PrimaryKey(autoGenerate = true) private int idRefrensi;
    @SerializedName(value = "refrensi") private String refrensi;
    @SerializedName(value = "jenis") private String jenis ;
    @SerializedName(value = "tanggal") private String tanggal ;
    @SerializedName(value = "valuasi") private int valuasi ;


    public Refrensi(Parcel in) {
        this.idRefrensi = in.readInt();
        this.refrensi = in.readString();
        this.jenis = in.readString();
        this.tanggal = in.readString();
        this.valuasi = in.readInt();
    }

    public static final Creator<Refrensi> CREATOR = new Creator<Refrensi>() {
        @Override
        public Refrensi createFromParcel(Parcel in) {
            return new Refrensi(in);
        }

        @Override
        public Refrensi[] newArray(int size) {
            return new Refrensi[size];
        }
    };

    public Refrensi() {

    }


    public int getIdRefrensi() {
        return idRefrensi;
    }

    public void setIdRefrensi(int idRefrensi) {
        this.idRefrensi = idRefrensi;
    }

    public String getRefrensi() {
        return refrensi;
    }

    public void setRefrensi(String refrensi) {
        this.refrensi = refrensi;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getValuasi() {
        return valuasi;
    }

    public void setValuasi(int valuasi) {
        this.valuasi = valuasi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.idRefrensi);
        parcel.writeString(this.refrensi);
        parcel.writeString(this.jenis);
        parcel.writeString(this.tanggal);
        parcel.writeInt(this.valuasi);
    }
}
