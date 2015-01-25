package net.terang.dunia.gospels.in.unison.activity;

import java.sql.*;

import net.terang.dunia.gospels.in.unison.*;
import net.terang.dunia.gospels.in.unison.gesture.*;
import net.terang.dunia.gospels.in.unison.view.*;
import android.content.pm.*;
import android.os.*;
import android.util.*;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class BookListActivity
    extends BaseListActivity
    implements OnGestureListener, OnDoubleTapListener, SimpleGestureListener
{
    private static final String TAG_NAME = BookListActivity.class
                    .getSimpleName();
    private TextView txtTitle;
    private Button btnBack;

    private boolean bUseFullscreen;

    // Gestures //
    private SimpleGestureFilter detector;

    // private GestureDetector gestureDetector;
    // private View.OnTouchListener gestureListener;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        bUseFullscreen = false;
        setContentView(R.layout.main);
        Log.d(TAG_NAME, "Book ListView activated successfully!");

        detector = new SimpleGestureFilter(this, this);
        /*
         * gestureDetector = new GestureDetector(detector);
         * gestureListener = new View.OnTouchListener() {
         * 
         * @Override
         * public boolean onTouch(View v, MotionEvent event)
         * {
         * if (gestureDetector.onTouchEvent(event)) {
         * return true;
         * }
         * return false;
         * }
         * };
         */

        // 1. first extract the bundle from intent
        Bundle bundle = getIntent().getExtras();

        // Set the test adapter
        try {
            setListAdapter(new BookAdapter(this, bundle));
        } catch (SQLException e) {
            Log.e(TAG_NAME, "Error: Unable to set Adapter", e);
        }

        // TextViews
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        btnBack = (Button) findViewById(R.id.btnExit);
        if (txtTitle == null || btnBack == null) {
            Log.d(TAG_NAME, "txtTitle / btnBack in main View is null");
            return;
        }

        // set values
        txtTitle.setText(bundle.getString("TITLE"));
        btnBack.setText("Back");
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG_NAME, "Back button was clicked!");
                finish();
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

    /** Gesture events */
    @Override
    public void onSwipe(int direction)
    {
        String str = "";
        switch (direction) {
        case SimpleGestureFilter.SWIPE_RIGHT:
            str = "Swipe Right";
            break;
        case SimpleGestureFilter.SWIPE_LEFT:
            str = "Swipe Left";
            break;
        case SimpleGestureFilter.SWIPE_DOWN:
            str = "Swipe Down";
            break;
        case SimpleGestureFilter.SWIPE_UP:
            str = "Swipe Up";
            break;
        default:
            break;
        }
        // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void updateFullscreenStatus(boolean bUseFullscreen)
    {
        LinearLayout btnBackParent = (LinearLayout) btnBack.getParent();

        if (bUseFullscreen) {
            btnBackParent.setVisibility(View.GONE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            btnBackParent.setVisibility(View.VISIBLE);
            getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }

        btnBackParent.requestLayout();
        findViewById(android.R.id.content).getRootView().requestLayout();
    }

    @Override
    public void onDoubleTap()
    {
        // Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
        bUseFullscreen = !bUseFullscreen;
        updateFullscreenStatus(bUseFullscreen);
    }

    @Override
    public boolean onFling(
        MotionEvent e1,
        MotionEvent e2,
        float velocityX,
        float velocityY)
    {
        Log.d("---onFling---", e1.toString() + e2.toString());
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me)
    {
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public boolean onTouchEvent(MotionEvent me)
    {
        this.detector.onTouchEvent(me);
        return super.onTouchEvent(me);
    }

    @Override
    public boolean onDown(MotionEvent e)
    {
        Log.d("---onDown----", e.toString());
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e)
    {
        Log.d("---onDoubleTap---", e.toString());
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e)
    {
        Log.d("---onDoubleTapEvent---", e.toString());
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e)
    {
        Log.d("---onSingleTapConfirmed---", e.toString());
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {
        Log.d("---onLongPress---", e.toString());
    }

    @Override
    public boolean onScroll(
        MotionEvent e1,
        MotionEvent e2,
        float distanceX,
        float distanceY)
    {
        Log.d("---onScroll---", e1.toString() + e2.toString());
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e)
    {
        Log.d("---onShowPress---", e.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
        Log.d("---onSingleTapUp---", e.toString());
        return false;
    }
}
