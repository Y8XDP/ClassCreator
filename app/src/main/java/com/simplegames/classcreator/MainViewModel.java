package com.simplegames.classcreator;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.simplegames.classcreator.adapters.MainAdapter;

class MainViewModel extends ViewModel {

    boolean isEmpty = true;

    private MainAdapter paramsAdapter;
    private MainAdapter methodsAdapter;
    String ClassName = "";

    MainAdapter getParamsAdapter(Context context) {
        paramsAdapter.setContext(context);
        return paramsAdapter;
    }

    MainAdapter getMethodsAdapter(Context context) {
        methodsAdapter.setContext(context);
        return methodsAdapter;
    }

    void setAdapters(MainAdapter paramsAdapter, MainAdapter methodsAdapter){
        this.paramsAdapter = paramsAdapter;
        this.methodsAdapter = methodsAdapter;
        isEmpty = false;
    }
}
