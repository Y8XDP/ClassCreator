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
import com.simplegames.classcreator.Item;
import com.simplegames.classcreator.MainActivity;
import com.simplegames.classcreator.R;

import java.util.ArrayList;

public class ListOfClassesAdapter extends RecyclerView.Adapter {

    private LayoutInflater inflater;
    private ArrayList<Item> data = new ArrayList<>();
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
        View rootView = inflater.inflate(R.layout.item_class_file, viewGroup, false);
        return new TextHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("ID", data.get(i).getID());

                context.startActivity(intent);
            }
        });

        TextView text = viewHolder.itemView.findViewById(R.id.text1);
        text.setText(data.get(i).getClassName());
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
                Item item = new Item(cursor.getString(id), cursor.getString(name));
                data.add(item);
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
        helper.getWritable().delete(DBContract.Entry.TABLE_NAME, " _ID LIKE ?", new String[]{data.get(position).getID()});
        data.remove(position);

        notifyItemRemoved(position);
    }
}
