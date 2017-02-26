package com.example.sofiane.esiee_drive.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Created by severin on 19/02/2017.
 */

public class Directory {
    String folderName;
    Date creationDate;
    String directory_id;


    public Directory(String name, Date creationDate){

        this.folderName = name;
        this.creationDate = creationDate;
    }

    public void setDirectory_id(String user_id) {
        this.directory_id = directory_id;
    }
    public String getFolderName(){return this.folderName;}
    public Date getCreationDate(){return this.creationDate;}

    @Override
    public String toString() {
        return "Directory{" +
                "name='" + folderName + '\'' +
                ", creationDate='" + creationDate + '\'' +
                '}';
    }


}
