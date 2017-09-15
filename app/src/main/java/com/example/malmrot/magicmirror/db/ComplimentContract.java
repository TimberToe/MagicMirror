package com.example.malmrot.magicmirror.db;

import android.provider.BaseColumns;

/**
 * Created by malmrot on 2017-09-14.
 */

public class ComplimentContract {

    public static final String DB_NAME = "com.example.malmrot.compliments.db";
    public static final int DB_VERSION = 1;

    public class ComplimentEntry implements BaseColumns {
        public static final String TABLE = "compliments";

        public static final String COL_TASK_TITLE = "compliment";
    }
}

