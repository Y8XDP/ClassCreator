package com.simplegames.classcreator;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.simplegames.classcreator.DataBase.DBContract;
import com.simplegames.classcreator.DataBase.DBHelper;
import com.simplegames.classcreator.Utils.SwipeToDelete;
import com.simplegames.classcreator.adapters.ExtendsListAdapter;
import com.simplegames.classcreator.adapters.MainAdapter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> paramsList = new ArrayList<>();
    private ArrayList<String> methodsList = new ArrayList<>();
    private MainViewModel viewModel;
    private MainAdapter paramsAdapter;
    private MainAdapter methodsAdapter;
    private DBHelper dbHelper;
    private String id;

    private String title = "";
    private String ext = "";

    private RecyclerView paramsRecycler;
    private RecyclerView methodsRecycler;

    private ArrayList<String> idsToDelete = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id = getIntent().getStringExtra("ID");

        init();
    }

    private void init(){
        dbHelper = new DBHelper(getApplicationContext());

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        createAdapters();
    }

    private void createAdapters(){
        paramsRecycler = findViewById(R.id.params);
        methodsRecycler= findViewById(R.id.methods);

        paramsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        methodsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (!viewModel.isEmpty){
            loadFromViewModel();
        }else{
            loadDataFromDB(id);
            paramsAdapter = new MainAdapter(paramsList, MainActivity.this, MainAdapter.Type.PARAM, id);
            methodsAdapter = new MainAdapter(methodsList, MainActivity.this, MainAdapter.Type.METHOD, id);
            viewModel.ClassName = title;
            viewModel.extend = ext;

            viewModel.setAdapters(paramsAdapter, methodsAdapter);
        }

        paramsRecycler.setAdapter(paramsAdapter);
        methodsRecycler.setAdapter(methodsAdapter);

        new SwipeToDelete(paramsAdapter, paramsRecycler, idsToDelete);
        new SwipeToDelete(methodsAdapter, methodsRecycler, idsToDelete);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                createEditClassNameDialog();
                break;
        }
        return true;
    }

    private void createEditClassNameDialog(){
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        View view = getLayoutInflater().inflate(R.layout.class_name_dialog, null);

        RecyclerView extendsList = view.findViewById(R.id.extend);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayout.HORIZONTAL);

        extendsList.setLayoutManager(manager);

        ExtendsListAdapter adapter = new ExtendsListAdapter(MainActivity.this, testMethod(), id);
        extendsList.setAdapter(adapter);

        final EditText editClassName = view.findViewById(R.id.editClassName);

        builder.setTitle("Изменение")
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues values = new ContentValues();
                        values.put(DBContract.Entry.CLASS_NAME, editClassName.getText().toString());

                        viewModel.ClassName = editClassName.getText().toString();
                        setTitle(editClassName.getText().toString() + " extends " + ext);

                        dbHelper.getWritable().update(DBContract.Entry.TABLE_NAME, values, "_ID LIKE ?", new String[]{id});
                    }
                })
                .setView(view);

        dialog = builder.create();
        dialog.show();
    }

    private ArrayList<String> testMethod() {
        ArrayList<String> listOfItems = new ArrayList<>();

        Cursor cursor = dbHelper.getReadable().rawQuery("SELECT * FROM " + DBContract.Entry.TABLE_NAME,
                new String[]{});

        if (cursor.moveToFirst()){
            int name = cursor.getColumnIndex(DBContract.Entry.CLASS_NAME);

            do {
                String s = cursor.getString(name);

                if(!s.equals(title) && !s.equals(ext)){
                    listOfItems.add(s);
                }
            }while(cursor.moveToNext());
        }

        cursor.close();

        return listOfItems;
    }

    private void loadFromViewModel(){
        paramsAdapter = viewModel.getParamsAdapter(MainActivity.this);
        methodsAdapter = viewModel.getMethodsAdapter(MainActivity.this);
        ext = viewModel.extend;
        title = viewModel.ClassName;
        setTitle(title + " extends " + ext);
    }

    private void loadDataFromDB(String id){
        paramsList.clear();
        methodsList.clear();

        Cursor cursor = dbHelper.getReadable().rawQuery("SELECT * FROM " +
                        DBContract.Entry.TABLE_NAME + " WHERE " +
                        DBContract.Entry._ID + " = ?",
                new String[]{id});

        if (cursor.moveToFirst()){
            int name = cursor.getColumnIndex(DBContract.Entry.CLASS_NAME);
            int ex = cursor.getColumnIndex(DBContract.Entry.CLASS_EXTENDS);
            int methods = cursor.getColumnIndex(DBContract.Entry.CLASS_METHODS);
            int params = cursor.getColumnIndex(DBContract.Entry.CLASS_PARAMS);

            do {
                title = cursor.getString(name);
                ext = cursor.getString(ex);
                viewModel.extend = ext;
                setTitle(cursor.getString(name) + " extends " + cursor.getString(ex));

                if (cursor.getString(methods) != null){
                    parse(methodsList, cursor.getString(methods));
                }

                if (cursor.getString(params) != null){
                    parse(paramsList, cursor.getString(params));
                }

            }while(cursor.moveToNext());
        }

        cursor.close();

        if (!ext.isEmpty()){
            exM(ext);
        }
    }

    private void exM(String ext){
        String extTmp = "";

        if (!ext.isEmpty()){
            Cursor cursor = dbHelper.getReadable().rawQuery("SELECT * FROM " +
                            DBContract.Entry.TABLE_NAME + " WHERE " +
                            DBContract.Entry.CLASS_NAME + " = ?",
                    new String[]{ext});

            if (cursor.moveToFirst()){
                int exts = cursor.getColumnIndex(DBContract.Entry.CLASS_EXTENDS);
                int methods = cursor.getColumnIndex(DBContract.Entry.CLASS_METHODS);
                int params = cursor.getColumnIndex(DBContract.Entry.CLASS_PARAMS);

                do {
                    extTmp = cursor.getString(exts);

                    if (cursor.getString(methods) != null){
                        parseExtends(methodsList, cursor.getString(methods), ext);
                    }

                    if (cursor.getString(params) != null){
                        parseExtends(paramsList, cursor.getString(params), ext);
                    }

                }while(cursor.moveToNext());
            }

            cursor.close();
        }

        if (!extTmp.isEmpty()){
            exM(extTmp);
        }
    }

    private void parse(ArrayList<String> list, String str){

        char[] array = str.toCharArray();
        String item = "";

        for (int i = 0; i < array.length; i++){
            if (array[i] == ':' && array[i+1] == ':'){
                list.add(item);
                item = "";
                i++;
            }else{
                item += String.valueOf(array[i]);
            }
        }
    }

    private void parseExtends(ArrayList<String> list, String str, String from){
        char[] array = str.toCharArray();
        String item = "";

        for (int i = 0; i < array.length; i++){
            if (array[i] == ':' && array[i+1] == ':'){
                if (item.substring(0, 1).equals("+")){
                    list.add(0, item.substring(0, 2) + "<font color='#FF7838'>from</font> " + from + ": " + item.substring(2));
                }
                item = "";
                i++;
            }else{
                item += String.valueOf(array[i]);
            }
        }
    }

    public void setT() {
        loadDataFromDB(id);

        paramsRecycler.setAdapter(paramsAdapter);
        methodsRecycler.setAdapter(methodsAdapter);
    }
}