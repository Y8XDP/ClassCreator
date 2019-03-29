package com.simplegames.classcreator;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.simplegames.classcreator.Utils.SwipeToDelete;
import com.simplegames.classcreator.adapters.MainAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView className;
    private ArrayList<String> paramsList = new ArrayList<>();
    private ArrayList<String> methodsList = new ArrayList<>();
    private MainViewModel viewModel;
    private MainAdapter paramsAdapter;
    private MainAdapter methodsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        className = findViewById(R.id.className);
        Button editClassName = findViewById(R.id.editClassName);

        editClassName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               createEditClassNameDialog();
            }
        });

        createAdapters();
    }

    private void createAdapters(){
        RecyclerView paramsRecycler = findViewById(R.id.params);
        RecyclerView methodsRecycler= findViewById(R.id.methods);

        paramsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        methodsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (!viewModel.isEmpty){
            loadFromViewModel();
        }else{
            paramsAdapter = new MainAdapter(paramsList, MainActivity.this, MainAdapter.Type.PARAM);
            methodsAdapter = new MainAdapter(methodsList, MainActivity.this, MainAdapter.Type.METHOD);

            viewModel.setAdapters(paramsAdapter, methodsAdapter);
        }

        paramsRecycler.setAdapter(paramsAdapter);
        methodsRecycler.setAdapter(methodsAdapter);

        new SwipeToDelete(paramsAdapter, paramsRecycler);
        new SwipeToDelete(methodsAdapter, methodsRecycler);
    }

    private void createEditClassNameDialog(){
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        View view = getLayoutInflater().inflate(R.layout.class_name_dialog, null);

        final EditText editClassName = view.findViewById(R.id.editClassName);

        builder.setTitle("Изменение")
                .setMessage("Введите имя класса")
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.ClassName = editClassName.getText().toString();
                        className.setText(editClassName.getText().toString());
                    }
                })
                .setView(view);

        dialog = builder.create();
        dialog.show();
    }

    private void loadFromViewModel(){
        paramsAdapter = viewModel.getParamsAdapter(MainActivity.this);
        methodsAdapter = viewModel.getMethodsAdapter(MainActivity.this);
        className.setText(viewModel.ClassName);
    }
}
