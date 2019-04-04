package com.simplegames.classcreator;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Environment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.simplegames.classcreator.DataBase.DBContract;
import com.simplegames.classcreator.DataBase.DBHelper;
import com.simplegames.classcreator.adapters.ListOfClassesAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class ListOfClassesActivity extends AppCompatActivity {

    private ListOfClassesAdapter adapter;
    private DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_classes);

        init();
    }

    private void init() {
        helper = new DBHelper(getApplicationContext());
        final RecyclerView listOfClasses = findViewById(R.id.listOfClasses);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        listOfClasses.setLayoutManager(manager);

        adapter = new ListOfClassesAdapter(ListOfClassesActivity.this);
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

        Button imp = findViewById(R.id.imp);
        Button exp = findViewById(R.id.exp);

        imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("IMPORT");
                try {
                    File sd = Environment.getExternalStorageDirectory();
                    if (sd.canWrite()) {
                        File backupDB = getApplicationContext().getDatabasePath("DB_NAME_IGNORE");
                        String backupDBPath = String.format("%s.bak", "DB_NAME_IGNORE");
                        File currentDB = new File(sd, backupDBPath);

                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();

                        System.out.println("SUCCESS");
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка " + e.toString());
                }
            }
        });

        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("EXPORT");
                try {
                    File sd = Environment.getExternalStorageDirectory();
                    File data = Environment.getDataDirectory();

                    if (sd.canWrite()) {
                        String backupDBPath = String.format("%s.bak", "DB_NAME_IGNORE");
                        File currentDB = getApplicationContext().getDatabasePath("DB_NAME_IGNORE");
                        File backupDB = new File(sd, backupDBPath);

                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();

                        System.out.println("SUCCESS");
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка " + e.toString());
                }
            }
        });
    }

    private void createInsertDialog(){
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(ListOfClassesActivity.this);

        View view = getLayoutInflater().inflate(R.layout.class_name_dialog, null);

        final EditText className = view.findViewById(R.id.editClassName);

        builder.setTitle("Добавление")
                .setMessage("Название класса")
                .setView(view)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!className.getText().toString().isEmpty()){
                            boolean isExist = false;

                            Cursor cursor = helper.getReadable().rawQuery("SELECT * FROM " + DBContract.Entry.TABLE_NAME +
                                    " WHERE " + DBContract.Entry.CLASS_NAME + " = " +
                                    className.getText().toString(), new String[]{});

                            if (cursor.moveToFirst()){
                                do {
                                    isExist = true;
                                }while(cursor.moveToNext());
                            }

                            cursor.close();

                            if (!isExist){
                                addNewElementOnDB(className.getText().toString());
                            }else Toast.makeText(getApplicationContext(), "Такой класс уже существует", Toast.LENGTH_SHORT).show();
                        }
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
        values.put(DBContract.Entry.CLASS_EXTENDS, "");
        new DBHelper(getApplicationContext()).getWritable().insert(DBContract.Entry.TABLE_NAME, null, values);
        adapter.reloadData();
    }
}
