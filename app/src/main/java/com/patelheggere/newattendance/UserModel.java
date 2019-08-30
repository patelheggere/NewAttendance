package com.patelheggere.newattendance;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {
    private String username;
    private String password;
    private String section_1, section_2;
    private String sec, sec_2;

    public UserModel() {
    }

    public UserModel(String username, String password, String section_1, String section_2, String sec, String sec_2) {
        this.username = username;
        this.password = password;
        this.section_1 = section_1;
        this.section_2 = section_2;
        this.sec = sec;
        this.sec_2 = sec_2;
    }


    protected UserModel(Parcel in) {
        username = in.readString();
        password = in.readString();
        section_1 = in.readString();
        section_2 = in.readString();
        sec = in.readString();
        sec_2 = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(section_1);
        parcel.writeString(section_2);
        parcel.writeString(sec);
        parcel.writeString(sec_2);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSection_1() {
        return section_1;
    }

    public void setSection_1(String section_1) {
        this.section_1 = section_1;
    }

    public String getSection_2() {
        return section_2;
    }

    public void setSection_2(String section_2) {
        this.section_2 = section_2;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getSec_2() {
        return sec_2;
    }

    public void setSec_2(String sec_2) {
        this.sec_2 = sec_2;
    }

    public static Creator<UserModel> getCREATOR() {
        return CREATOR;
    }
}
