package com.SXG.sam.choosu.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sam on 3/27/18.
 */

public class YelpModel implements Parcelable {
    public YelpModel (){}

    public String name;

    public String idNumber;

    public String getIdNumber(){return idNumber;}

    public void setIdNumber(String idNumber){
        this.idNumber = idNumber;
    }

    protected YelpModel(Parcel in) {
        name = in.readString();
        url = in.readString();
        imageurl = in.readString();
    }

    public static final Creator<YelpModel> CREATOR = new Creator<YelpModel>() {
        @Override
        public YelpModel createFromParcel(Parcel in) {
            return new YelpModel(in);
        }

        @Override
        public YelpModel[] newArray(int size) {
            return new YelpModel[size];
        }
    };

    public String getName(){
        return name;
    }

    public String url;

    public String getUrl(){
        return url;
    }

    public String imageurl;

    public String getYelpImageurl(){
        return imageurl;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(imageurl);
    }
}
