package com.example.sofiane.esiee_drive.Classes;

/**
 * Created by Sofiane on 19/02/2017.
 */

public class FileInfos {
    String name_file;
    String date;

    public FileInfos(String name_file, String date) {
        this.name_file = name_file;
        this.date = date;
    }

    public String getName_file() {
        return name_file;
    }

    public void setName_file(String name_file) {
        this.name_file = name_file;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "FileInfos{" +
                "name_file='" + name_file + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
