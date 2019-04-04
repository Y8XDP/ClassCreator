package com.simplegames.classcreator.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.simplegames.classcreator.DataBase.DBContract;
import com.simplegames.classcreator.DataBase.DBHelper;
import com.simplegames.classcreator.MainActivity;
import com.simplegames.classcreator.R;

import java.util.ArrayList;

public class ExtendsListAdapter extends RecyclerView.Adapter {

    private LayoutInflater inflater;
    private ArrayList<String> listOfItems;
    private Context context;
    private MainActivity activity;
    private String id;

    public ExtendsListAdapter(MainActivity activity, ArrayList<String> listOfItems, String id) {
        this.listOfItems = listOfItems;
        this.context = activity.getApplicationContext();
        this.activity = activity;
        this.id = id;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_extends, viewGroup, false);

        return new TextHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final  int i) {
        final TextView view = viewHolder.itemView.findViewById(R.id.text1);
        view.setText(listOfItems.get(i));


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(DBContract.Entry.CLASS_EXTENDS, listOfItems.get(i));

                new DBHelper(context).getWritable().update(DBContract.Entry.TABLE_NAME, values, "_ID LIKE ?", new String[]{id});

                activity.setT();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfItems.size();
    }
}
