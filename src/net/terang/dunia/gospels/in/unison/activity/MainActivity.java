package net.terang.dunia.gospels.in.unison.activity;

import net.terang.dunia.gospels.in.unison.view.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;

public class MainActivity
    extends BaseActivity
    implements OnClickListener
{
    private static final String TAG_NAME = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View view = new Banner(this);
        view.setOnClickListener(this);
        setContentView(view);
        Log.d(TAG_NAME, "Banner View activated successfully!");
    }

    @Override
    public void onClick(View v)
    {
        v.setOnClickListener(null);
        Log.d(TAG_NAME, "View: " + v.toString() + " clicked!");
        startActivity(new Intent(this, TocListActivity.class));
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        System.gc();
    }

    /**
     * Set the current orientation to landscape. This will prevent the OS from
     * changing the app's orientation.
     */
    public void lockOrientationLandscape()
    {
        lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Set the current orientation to portrait. This will prevent the OS from
     * changing the app's orientation.
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