
package com.nuedevlop.kasirportable.utils.pengguna;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfilToko implements Parcelable
{

    @SerializedName("Hasil")
    @Expose
    private List<Hasil> hasil = null;
    @SerializedName("success")
    @Expose
    private Integer success;
    public final static Parcelable.Creator<ProfilToko> CREATOR = new Creator<ProfilToko>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ProfilToko createFromParcel(Parcel in) {
            return new ProfilToko(in);
        }

        public ProfilToko[] newArray(int size) {
            return (new ProfilToko[size]);
        }

    }
    ;

    protected ProfilToko(Parcel in) {
        in.readList(this.hasil, (com.nuedevlop.kasirportable.utils.pengguna.Hasil.class.getClassLoader()));
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public ProfilToko() {
    }

    public List<Hasil> getHasil() {
        return hasil;
    }

    public void setHasil(List<Hasil> hasil) {
        this.hasil = hasil;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(hasil);
        dest.writeValue(success);
    }

    public int describeContents() {
        return  0;
    }

}
