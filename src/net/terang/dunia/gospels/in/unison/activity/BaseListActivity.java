package net.terang.dunia.gospels.in.unison.activity;

import android.app.*;
import android.content.*;
import android.os.*;

public class BaseListActivity
    extends ListActivity
{
    private static final String TAG_NAME = BaseListActivity.class
                    .getSimpleName();
    private KillReceiver mKillReceiver;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mKillReceiver = new KillReceiver();
        registerReceiver(mKillReceiver,
            IntentFilter.create("android.action.KILL", "text/plain"));
    }

    private final class KillReceiver
        extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            finish();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(mKillReceiver);
    }

}
