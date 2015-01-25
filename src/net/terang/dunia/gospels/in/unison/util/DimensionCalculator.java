package net.terang.dunia.gospels.in.unison.util;

import android.content.*;
import android.util.*;

/**
 * Sample class from Pulse
 * 
 * Class to store and provide useful dimensions
 */
public class DimensionCalculator
{
    private static DimensionCalculator mInstance = null;
    private final int mScreenWidth;
    private final int mTileWidth;

    /**
     * This class is a singleton
     */
    public static DimensionCalculator getInstance(Context context)
    {
        if (mInstance == null) {
            mInstance = new DimensionCalculator(context);
        }
        return mInstance;
    }

    /**
     * Initialize the class with the proper tile size
     */
    protected DimensionCalculator(Context context)
    {

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mScreenWidth = Math.min(
            context.getResources().getDisplayMetrics().widthPixels, context
                            .getResources().getDisplayMetrics().heightPixels);

        int numTiles = 3;
        int tileGap = 2;
        mTileWidth = ((mScreenWidth - 4 * tileGap) / numTiles);

    }

    /**
     * Return the appropriate tile size for this device
     */
    public int getTileWidth()
    {
        return mTileWidth;
    }
}
