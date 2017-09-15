package com.example.malmrot.magicmirror;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.malmrot.magicmirror.db.InsultContract;
import com.example.malmrot.magicmirror.db.InsultDbHelper;

import java.util.ArrayList;

public class InsultsSettingsActivity extends AppCompatActivity {
    private InsultDbHelper mHelper;
    private ListView mInsultListView;
    private ArrayAdapter<String> mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insults_settings);

        mHelper = new InsultDbHelper(this);
        mInsultListView = (ListView) findViewById(R.id.list_insults);

        updateUI();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new insult")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String insult = String.valueOf(taskEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(InsultContract.InsultEntry.COL_TASK_TITLE, insult);
                                db.insertWithOnConflict(InsultContract.InsultEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.text_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(InsultContract.InsultEntry.TABLE,
                InsultContract.InsultEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    private void updateUI() {
        ArrayList<String> insultList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(InsultContract.InsultEntry.TABLE,
                new String[]{InsultContract.InsultEntry._ID, InsultContract.InsultEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(InsultContract.InsultEntry.COL_TASK_TITLE);
            insultList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_text,
                    R.id.text_title,
                    insultList);
            mInsultListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(insultList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }
}


/*

        //this is my preferences variable
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //I create a StringSet then add elements to it
        Set<String> set = new HashSet<String>();

        set.add("test 1");
        set.add("test 2");
        set.add("test 3");


        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("test", "Mitt namn is Anton");

        editor.putStringSet("insults", set);

        editor.apply();

        Set<String> fetch = prefs.getStringSet("insults", null);

        //I then convert it to an Array List and try to see if I got the values
        List<String> list = new ArrayList<String>(fetch);

        for(int i = 0 ; i < list.size() ; i++){
            Log.v("fetching values", "fetch value " + list.get(i));
        }
 */