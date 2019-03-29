package com.simplegames.classcreator.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simplegames.classcreator.DataBase.DBContract;
import com.simplegames.classcreator.DataBase.DBHelper;
import com.simplegames.classcreator.MainActivity;

import java.util.ArrayList;

public class ListOfClassesAdapter extends RecyclerView.Adapter {

    private LayoutInflater inflater;
    private ArrayList<String[]> data = new ArrayList<>();
    private DBHelper helper;
    private Context context;

    public ListOfClassesAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        helper = new DBHelper(context);
        loadData();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        return new TextHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("ID", data.get(i)[1]);

                context.startActivity(intent);
            }
        });

        TextView text = viewHolder.itemView.findViewById(android.R.id.text1);
        text.setText(data.get(i)[0]);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void loadData(){
        Cursor cursor = helper.getReadable().rawQuery("SELECT * FROM " + DBContract.Entry.TABLE_NAME,
                new String[]{});

        if (cursor.moveToFirst()){
            int name = cursor.getColumnIndex(DBContract.Entry.CLASS_NAME);
            int id = cursor.getColumnIndex(DBContract.Entry._ID);

            do {
                String[] dataTemp = new String[2];
                dataTemp[0] = cursor.getString(name);
                dataTemp[1] = cursor.getString(id);
                data.add(dataTemp);
            }while(cursor.moveToNext());
        }

        cursor.close();
    }

    public void reloadData() {
        data.clear();
        loadData();
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        new DBHelper(context).getWritable().delete(DBContract.Entry.TABLE_NAME, " _ID LIKE ?", new String[]{data.get(position)[1]});
        data.remove(position);

        notifyItemRemoved(position);
    }
}
