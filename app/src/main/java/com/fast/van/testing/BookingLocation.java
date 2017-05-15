package com.fast.van.testing;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ramangoyal on 23/06/15.
 */
public class BookingLocation implements Parcelable {

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<BookingLocation> CREATOR = new Creator<BookingLocation>() {
        public BookingLocation createFromParcel(Parcel in) {
            return new BookingLocation(in);
        }

        public BookingLocation[] newArray(int size) {
            return new BookingLocation[size];
        }
    };
    public String locationString = "";
    public String ZipCode = "";
    public double latitude=0;
    public double longitude=0;

    public BookingLocation(String locationString, double latitude, double longitude, String ZipCode) {
        this.locationString = locationString;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ZipCode = ZipCode;
    }

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private BookingLocation(Parcel in) {

        locationString = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        ZipCode = in.readString();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationString);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(ZipCode);
    }
}
