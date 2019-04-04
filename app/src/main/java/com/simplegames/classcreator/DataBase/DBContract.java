package com.simplegames.classcreator.DataBase;

import android.provider.BaseColumns;

public class DBContract {

    public class Entry implements BaseColumns{
        public static final String TABLE_NAME = "classes_table";
        public static final String CLASS_NAME = "class_name";
        public static final String CLASS_EXTENDS = "class_extends";
        public static final String CLASS_PARAMS = "class_params";
        public static final String CLASS_METHODS = "class_methods";
        public static final String _ID = BaseColumns._ID;

        static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Entry.TABLE_NAME + " (" +
                Entry._ID + " INTEGER PRIMARY KEY," +
                Entry.CLASS_NAME + " TEXT," +
                Entry.CLASS_EXTENDS + " TEXT," +
                Entry.CLASS_PARAMS + " TEXT," +
                Entry.CLASS_METHODS + " TEXT)";
    }
}
