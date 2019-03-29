package com.simplegames.classcreator;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.simplegames.classcreator.adapters.MainAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView className;
    private ArrayList<String> paramsList = new ArrayList<>();
    private ArrayList<String> methodsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
      /*  paramsList.add("1");
        paramsList.add("2");
        paramsList.add("3");
        paramsList.add("4");
        paramsList.add("5");*/


        className = findViewById(R.id.className);
        Button editClassName = findViewById(R.id.editClassName);

        editClassName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                View view = getLayoutInflater().inflate(R.layout.class_name_dialog, null);

                final EditText editClassName = view.findViewById(R.id.editClassName);

                builder.setTitle("Изменение")
                        .setMessage("Введите имя класса")
                        .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                className.setText(editClassName.getText().toString());
                                dialog.cancel();
                            }
                        })
                        .setView(view);

                dialog = builder.create();
                dialog.show();
            }
        });

        createAdapters();
    }

    private void createAdapters(){
        RecyclerView paramsRecycler = findViewById(R.id.params);
        RecyclerView methodsRecycler= findViewById(R.id.methods);

        paramsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        methodsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final MainAdapter paramsAdapter = new MainAdapter(paramsList, MainActivity.this, MainAdapter.Type.PARAM);
        paramsRecycler.setAdapter(paramsAdapter);
        MainAdapter methodsAdapter = new MainAdapter(methodsList, MainActivity.this, MainAdapter.Type.METHOD);
        methodsRecycler.setAdapter(methodsAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();


            }

        });

        itemTouchHelper.attachToRecyclerView(paramsRecycler);
        itemTouchHelper.attachToRecyclerView(methodsRecycler);
    }
}
