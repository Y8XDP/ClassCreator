package com.simplegames.classcreator;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;

import com.simplegames.classcreator.DataBase.DBContract;
import com.simplegames.classcreator.DataBase.DBHelper;
import com.simplegames.classcreator.Utils.SwipeToDelete;
import com.simplegames.classcreator.adapters.ButtonHolder;
import com.simplegames.classcreator.adapters.ListOfClassesAdapter;

public class ListOfClasses extends AppCompatActivity {

    private ListOfClassesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_classes);

        init();
    }

    private void init() {
        final RecyclerView listOfClasses = findViewById(R.id.listOfClasses);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        listOfClasses.setLayoutManager(manager);

        adapter = new ListOfClassesAdapter(ListOfClasses.this);
        listOfClasses.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                int position = viewHolder.getAdapterPosition();
                adapter.removeItem(position);

                Snackbar.make(listOfClasses, "Элемент удален", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }

        }).attachToRecyclerView(listOfClasses);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               createInsertDialog();
            }
        });
    }

    private void createInsertDialog(){
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(ListOfClasses.this);

        View view = getLayoutInflater().inflate(R.layout.class_name_dialog, null);

        final EditText className = view.findViewById(R.id.editClassName);

        builder.setTitle("Добавление")
                .setMessage("Название класса")
                .setView(view)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addNewElementOnDB(className.getText().toString());
                    }
                });

        dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        adapter.reloadData();
        super.onResume();
    }

    private void addNewElementOnDB(String className){
        ContentValues values = new ContentValues();
        values.put(DBContract.Entry.CLASS_NAME, className);
        new DBHelper(getApplicationContext()).getWritable().insert(DBContract.Entry.TABLE_NAME, null, values);
        adapter.reloadData();
    }
}
