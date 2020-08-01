
package com.nuedevlop.kasirportable.utils.pengguna;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hasil implements Parcelable
{

    @SerializedName("id_pengguna")
    @Expose
    private String idPengguna;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("nama_toko")
    @Expose
    private String namaToko;
    @SerializedName("no_hp")
    @Expose
    private String noHp;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    public final static Parcelable.Creator<Hasil> CREATOR = new Creator<Hasil>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Hasil createFromParcel(Parcel in) {
            return new Hasil(in);
        }

        public Hasil[] newArray(int size) {
            return (new Hasil[size]);
        }

    }
    ;

    protected Hasil(Parcel in) {
        this.idPengguna = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.namaToko = ((String) in.readValue((String.class.getClassLoader())));
        this.noHp = ((String) in.readValue((String.class.getClassLoader())));
        this.alamat = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Hasil() {
    }

    public String getIdPengguna() {
        return idPengguna;
    }

    public void setIdPengguna(String idPengguna) {
        this.idPengguna = idPengguna;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNamaToko() {
        return namaToko;
    }

    public void setNamaToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(idPengguna);
        dest.writeValue(email);
        dest.writeValue(namaToko);
        dest.writeValue(noHp);
        dest.writeValue(alamat);
    }

    public int describeContents() {
        return  0;
    }

}
