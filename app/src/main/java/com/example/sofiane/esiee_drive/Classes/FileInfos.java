package com.example.sofiane.esiee_drive.Classes;

/**
 * Created by Sofiane on 19/02/2017.
 */

public class FileInfos {
    String name_file;
    String user_name;
    String date;

    public FileInfos(String name_file, String user_name, String date) {
        this.name_file = name_file;
        this.user_name = user_name;
        this.date = date;
    }

    public String getName_file() {
        return name_file;
    }

    public void setName_file(String name_file) {
        this.name_file = name_file;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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
                ", user_name='" + user_name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
