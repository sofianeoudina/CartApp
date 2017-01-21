package com.example.sofiane.esiee_drive.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sofiane.esiee_drive.Classes.FileInfos;
import com.example.sofiane.esiee_drive.R;

import java.util.ArrayList;

/**
 * Created by Sofiane on 19/02/2017.
 */

public class FileInfosAdapter extends ArrayAdapter<FileInfos> {
    public FileInfosAdapter(Context context, int resource, ArrayList<FileInfos> people) {
        super(context, resource, people);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View root = inflater.inflate(R.layout.historical_item, null);
        FileInfos f = getItem(position);

        TextView name_file = (TextView) root.findViewById(R.id.name_file);
        TextView user_name = (TextView) root.findViewById(R.id.user_name);
        TextView date = (TextView) root.findViewById(R.id.date);

        name_file.setText(f.getName_file());
        user_name.setText(f.getUser_name());
        date.setText(f.getDate());

        return root;
    }
}
