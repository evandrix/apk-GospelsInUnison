package net.terang.dunia.gospels.in.unison.activity;

import android.app.*;
import android.content.*;
import android.os.*;

public class BaseActivity
    extends Activity
{
    private KillReceiver mKillReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mKillReceiver = new KillReceiver();
        registerReceiver(mKillReceiver,
            IntentFilter.create("android.action.KILL", "text/plain"));
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(mKillReceiver);
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
}
