package net.terang.dunia.gospels.in.unison.activity;

import java.io.*;
import java.sql.*;

import net.terang.dunia.gospels.in.unison.*;
import net.terang.dunia.gospels.in.unison.view.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class TocListActivity
    extends BaseListActivity
{
    private static final String TAG_NAME = TocListActivity.class
                    .getSimpleName();
    private TextView txtTitle;
    private Button btnExit;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        Log.d(TAG_NAME, "Toc ListView activated successfully!");

        // Set the test adapter
        try {
            setListAdapter(new TocAdapter(this));
        } catch (SQLException e) {
            Log.e(TAG_NAME, "Error: SQLException", e);
        } catch (IOException e) {
            Log.e(TAG_NAME, "Error: IOException", e);
        }

        // TextViews
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        btnExit = (Button) findViewById(R.id.btnExit);
        if (txtTitle == null || btnExit == null) {
            Log.d(TAG_NAME, "txtTitle / btnExit in main View is null");
            return;
        }

        // set values
        txtTitle.setText("Daftar Isi");
        btnExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG_NAME, "Exit button was clicked!");

                Intent intent = new Intent("android.action.KILL");
                intent.setType("text/plain");
                sendBroadcast(intent);
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        System.gc();
    }

    /**
     * Set the current orientation to landscape. This will prevent the OS from
     * changing
     * the app's orientation.
     */
    public void lockOrientationLandscape()
    {
        lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Set the current orientation to portrait. This will prevent the OS from
     * changing
     * the app's orientation.
     */
    public void lockOrientationPortrait()
    {
        lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Locks the orientation to a specific type. Possible values are:
     * <ul>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_BEHIND}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_LANDSCAPE}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_NOSENSOR}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_PORTRAIT}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_SENSOR}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_UNSPECIFIED}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_USER}</li>
     * </ul>
     * 
     * @param orientation
     */
    public void lockOrientation(int orientation)
    {
        setRequestedOrientation(orientation);
    }
}