package com.example.malmrot.magicmirror;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malmrot.magicmirror.db.ComplimentContract;
import com.example.malmrot.magicmirror.db.ComplimentDbHelper;
import com.example.malmrot.magicmirror.db.InsultContract;
import com.example.malmrot.magicmirror.db.InsultDbHelper;

import java.util.ArrayList;
import java.util.Random;

import static com.example.malmrot.magicmirror.R.id.textView;


public class MainActivity extends Activity{

    private static final String TAG = "MagicMirror";
    private Resources mRes;
    private Boolean mStopping = false;
    private Boolean mShowCompliments = true;
    private Boolean mIsActive = false;
    private Boolean mIsStep = false;
    private Boolean mIsVisible = false;

    private AlphaAnimation animFadeInStart = new AlphaAnimation(0.0f, 1.0f);
    private AlphaAnimation animFadeIn = new AlphaAnimation(0.0f, 1.0f);
    private AlphaAnimation animFadeOut = new AlphaAnimation(1.0f, 0.0f);
    private Integer animDuration = 1000;
    private TextView mTextView;
    
    private ComplimentDbHelper mComplimentHelper;
    private ArrayList<String> mCompliments = new ArrayList<>();

    private InsultDbHelper mInsultHelper;
    private ArrayList<String> mInsults = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(textView);
        mRes = getResources();
        mComplimentHelper = new ComplimentDbHelper(this);
        mInsultHelper = new InsultDbHelper(this);

        setupButtons();

        setupAnimation();

        getStringsFromDb();

    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        getStringsFromDb();
    }

    private void getStringsFromDb(){
        getComplimentsFromDb();
        getInsultsFromDb();
    }

    private void getInsultsFromDb(){

        mInsultHelper = new InsultDbHelper(this);
        SQLiteDatabase db = mInsultHelper.getReadableDatabase();
        Cursor cursor = db.query(InsultContract.InsultEntry.TABLE,
                new String[]{InsultContract.InsultEntry._ID, InsultContract.InsultEntry.COL_TASK_TITLE},
                null, null, null, null, null);

        mInsults.clear();

        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(InsultContract.InsultEntry.COL_TASK_TITLE);
            Log.v(TAG, "Task: " + cursor.getString(idx));

            mInsults.add(cursor.getString(idx));
        }
        cursor.close();
        db.close();
    }

    private void getComplimentsFromDb(){

        mComplimentHelper = new ComplimentDbHelper(this);
        SQLiteDatabase db = mComplimentHelper.getReadableDatabase();
        Cursor cursor = db.query(ComplimentContract.ComplimentEntry.TABLE,
                new String[]{ComplimentContract.ComplimentEntry._ID, ComplimentContract.ComplimentEntry.COL_TASK_TITLE},
                null, null, null, null, null);

        mCompliments.clear();

        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(ComplimentContract.ComplimentEntry.COL_TASK_TITLE);
            Log.v(TAG, "Task: " + cursor.getString(idx));

            mCompliments.add(cursor.getString(idx));
        }
        cursor.close();
        db.close();
    }
    
    private void setupAnimation(){

        animFadeInStart.setDuration(animDuration);
        animFadeInStart.setStartOffset(1000);

        animFadeIn.setDuration(animDuration);
        animFadeIn.setStartOffset(5000);
        animFadeOut.setDuration(animDuration);
        animFadeOut.setStartOffset(3000);


        //animFadeIn AnimationListener
        animFadeInStart.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {
                mTextView.startAnimation(animFadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub
            }

        });


        //animFadeIn AnimationListener
        animFadeIn.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {
                mIsVisible = true;
                Log.v(TAG, "mIsVisible=" + mIsVisible);
                if (!mIsStep) {
                    mTextView.startAnimation(animFadeOut);
                }
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub
            }

        });


        //animFadeOut AnimationListener
        animFadeOut.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {
                mIsVisible = false;

                Log.v(TAG, "mIsVisible=" + mIsVisible);
                if (mStopping){
                    mIsActive = mStopping = false;
                    mTextView.setText("");
                }

                if (mIsActive){

                    if (mShowCompliments){
                        mTextView.setText(getRandomString(mCompliments));
                    }else{
                        mTextView.setText(getRandomString(mInsults));
                    }

                    if (!mIsStep){
                        mTextView.startAnimation(animFadeIn);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub
            }

        });
    }

    public void openOptions(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void toggleCompliments(View view){
        mShowCompliments = !mShowCompliments;
    }

    private void setupButtons(){



        View lowerButton = (View) findViewById(R.id.lowerButton);

        lowerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mIsStep){

                    Log.v(TAG, "CLICK: mIsVisible=" + mIsVisible);
                    if (mIsVisible){
                        mTextView.startAnimation(animFadeOut);
                    }else{
                        mTextView.startAnimation(animFadeIn);
                    }
                }
            }

        });

        lowerButton.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                Log.v(TAG, "onLongClick");
                if (mIsActive){
                    mStopping = true;
                } else {
                    mIsActive = true;

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    String startMsg = sharedPref.getString("startMsg", "You woke me...");


                    mTextView.setText(startMsg);
                    mTextView.startAnimation(animFadeInStart);
                }
                return true;
            }

        });
    }

    private String getRandomString(ArrayList<String> list){
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
}
