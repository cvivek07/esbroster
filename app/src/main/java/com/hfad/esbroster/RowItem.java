package com.hfad.esbroster;

/**
 * Created by Vivek on 21-Apr-16.
 */
public class RowItem {
    private String date_string;
    private String name_string;
    private String shift_string;

    public String getDate_string() {
        return date_string;
    }

    public void setDate_string(String date_string) {
        this.date_string = date_string;
    }

    public String getName_string() {
        return name_string;
    }

    public void setName_string(String name_string) {
        this.name_string = name_string;
    }

    public String getShift_string() {
        return shift_string;
    }

    public void setShift_string(String shift_string) {
        this.shift_string = shift_string;
    }

    public RowItem(String date_string, String name_string, String shift_string) {
        this.date_string = date_string;
        this.name_string = name_string;
        this.shift_string = shift_string;
    }
    @Override
    public String toString() {
        return date_string + name_string + shift_string;
    }
}
