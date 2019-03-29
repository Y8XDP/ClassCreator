package com.simplegames.classcreator.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.simplegames.classcreator.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter {

    private ArrayList<String> itemList;
    private LayoutInflater inflater;
    private Context context;

    public enum Type {METHOD, PARAM}
    private Type type;


    public MainAdapter(ArrayList<String> itemList, Context context, Type type) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.type = type;
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView;
        RecyclerView.ViewHolder holder;

        if (i == 2){
            rootView = inflater.inflate(R.layout.last_item_adapter, viewGroup, false);
            holder = new TextHolder(rootView);
        }else{
            rootView = inflater.inflate(R.layout.item_adapter, viewGroup, false);
            holder = new ButtonHolder(rootView);
        }

        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) return 2;
        else return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {

        if (viewHolder.getItemViewType() == 2){
            CardView addItem = viewHolder.itemView.findViewById(R.id.addItem);

            addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog[] dialog = new AlertDialog[1];
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    View view = inflater.inflate(R.layout.add_item_dialog, null);

                    final EditText editClassName = view.findViewById(R.id.editClassName);
                    final char[] c = {'~'};

                    builder.setTitle("Добавление элемента")
                            .setSingleChoiceItems(new String[] {"private", "protected", "package", "public"}, 2,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int item) {
                                    switch (item){
                                        case 0:
                                            c[0] = '-';
                                            break;
                                        case 1:
                                            c[0] = '#';
                                            break;
                                        case 2:
                                            c[0] = '~';
                                            break;
                                        case 3:
                                            c[0] = '+';
                                            break;
                                    }
                                }
                            })
                            .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!(editClassName.getText().toString().isEmpty())){
                                        itemList.add(0, c[0] + " " + editClassName.getText().toString());
                                        notifyItemInserted(0);
                                    }
                                }
                            })
                            .setView(view);

                    dialog[0] = builder.create();
                    dialog[0].show();
                }
            });

        }else {
            TextView text = viewHolder.itemView.findViewById(R.id.item_name);
            TextView mod = viewHolder.itemView.findViewById(R.id.mod);

            String result = "";

            switch (type){
                case PARAM:
                    result = itemList.get(i).substring(1);
                    break;
                case METHOD:
                    result = itemList.get(i).substring(1) + "()";
                    break;
            }
            text.setText(result);

            mod.setText(itemList.get(i).substring(0, 1));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size() + 1;
    }
}