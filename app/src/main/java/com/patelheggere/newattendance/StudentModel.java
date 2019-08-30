package com.patelheggere.newattendance;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentModel implements Parcelable {
    private String parentNumber;
    private String name;
    private String semester;
    private long regNo;
    private boolean selected = false;

    public StudentModel() {
    }

    public StudentModel(String parentNumber, String name, String semester, long regNo, boolean selected) {
       this.parentNumber = parentNumber;
        this.name = name;
        this.semester = semester;
        this.regNo = regNo;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static Creator<StudentModel> getCREATOR() {
        return CREATOR;
    }

    protected StudentModel(Parcel in) {
        parentNumber = in.readString();
        name = in.readString();
        semester = in.readString();
        regNo = in.readLong();
    }

    public static final Creator<StudentModel> CREATOR = new Creator<StudentModel>() {
        @Override
        public StudentModel createFromParcel(Parcel in) {
            return new StudentModel(in);
        }

        @Override
        public StudentModel[] newArray(int size) {
            return new StudentModel[size];
        }
    };

    public String getParentNumber() {
        return parentNumber;
    }

    public void setParentNumber(String parentNumber) {
        this.parentNumber = parentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public long getRegNo() {
        return this.regNo;
    }

    public void setRegNo(long regNo) {
        this.regNo = regNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(parentNumber);
        parcel.writeString(name);
        parcel.writeString(semester);
        parcel.writeLong(regNo);
    }
}
