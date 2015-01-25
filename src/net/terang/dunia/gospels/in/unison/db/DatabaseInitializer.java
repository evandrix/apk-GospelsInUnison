package net.terang.dunia.gospels.in.unison.db;

import java.io.*;

import net.terang.dunia.gospels.in.unison.*;
import android.content.*;
import android.content.res.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.*;

public class DatabaseInitializer
{
    private static final String TAG_NAME = DatabaseInitializer.class
                    .getSimpleName();
    private SQLiteDatabase mDatabase;
    private final Context mContext;
    private final String DB_PATH, DB_NAME;

    public DatabaseInitializer(Context context)
    {
        Log.d(TAG_NAME, "::init()");
        this.mContext = context;

        DB_PATH = String.format("/data/data/%s/databases/",
            context.getPackageName());
        DB_NAME = context.getResources().getString(R.string.database_name);
    }

    public void createDatabase(DatabaseHelper mDbHelper)
    {
        Log.d(TAG_NAME, String.format("createDatabase(%s)", mDbHelper));
        try {
            if (!isDatabasePresent()) {
                // get database, we will override it in next steep
                // but folders containing the database are created
                mDatabase = mDbHelper.getWritableDatabase();
                mDatabase.close();

                // copy database
                copyDatabase();
            }
            mDatabase = mDbHelper.getWritableDatabase();
        } catch (SQLException eSQL) {
            Log.e(TAG_NAME, "Error: Cannot open database", eSQL);
        } catch (IOException IOe) {
            Log.e(TAG_NAME, "Error: Cannot copy initial database", IOe);
        }
    }

    /**
     * queries existence of database by attempting to open it
     */
    private boolean isDatabasePresent()
    {
        File dbFile = new File(DB_PATH, DB_NAME);
        return dbFile.exists();
    }

    private void copyDatabase()
        throws IOException
    {
        String inFileName = DB_NAME;
        String outFileName = DB_PATH + DB_NAME;

        Log.d(TAG_NAME,
            String.format("copyDatabase(): %s -> %s", inFileName, outFileName));

        // Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(inFileName,
            AssetManager.ACCESS_STREAMING);

        // Path to the just created empty db

        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public synchronized void closeDatabase()
    {
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
    }
}
