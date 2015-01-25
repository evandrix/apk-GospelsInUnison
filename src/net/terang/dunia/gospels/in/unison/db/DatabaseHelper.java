package net.terang.dunia.gospels.in.unison.db;

import java.sql.*;

import net.terang.dunia.gospels.in.unison.model.*;
import android.content.*;
import android.database.sqlite.*;
import android.util.*;

import com.j256.ormlite.android.apptools.*;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.support.*;
import com.j256.ormlite.table.*;

public class DatabaseHelper
    extends OrmLiteSqliteOpenHelper
{
    private static final String TAG_NAME = DatabaseHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 11;
    private transient final Context mContext;

    private final DatabaseInitializer initializer;
    private Dao<TocItem, String> tocDao = null;
    private Dao<BookItem, String> bookDao = null;

    /**
     * Initializes the database helper
     * 
     * @param ctx the context to run in.
     * @throws SQLException
     */
    public DatabaseHelper(final Context context) throws SQLException
    {
        super(context, context.getResources().getString(
            context.getResources().getIdentifier("database_name", "string",
                context.getPackageName())), null, DATABASE_VERSION);
        Log.d(TAG_NAME, "init()::Package name - " + context.getPackageName());
        mContext = context;

        initializer = new DatabaseInitializer(mContext);
        initializer.createDatabase(this);
        initializer.closeDatabase();

        initTocDao();
        initBookDao();
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
    {
        Log.d(TAG_NAME, String.format("onCreate(%s)", db.toString()));
        // try {
        // TableUtils.createTableIfNotExists(connectionSource,
        // TocItem.class);
        // TableUtils.createTableIfNotExists(connectionSource,
        // BookItem.class);
        // TableUtils.clearTable(connectionSource, TocItem.class);
        // TableUtils.clearTable(connectionSource, BookItem.class);
        // TocDbContext tocDbContext = new TocDbContext(mContext);
        // List<TocItem> tocList = tocDbContext.toc.getAll();
        // int chapter_no = 1;
        // for (TocItem item : tocList) {
        // getTocDao().createOrUpdate(item);
        // BookDbContext bookDbContext = new BookDbContext(mContext);
        // List<BookItem> bookList = bookDbContext.book.getAll(chapter_no);
        // if (bookList == null) continue;
        // for (BookItem chapter : bookList) {
        // if (chapter == null) break;
        // getBookDao().createOrUpdate(chapter);
        // }
        // chapter_no++;
        // }
        // fillDatabaseWithDummyData();
        // } catch (SQLException e) {
        // throw new RuntimeException("Can't create database");
        // }
    }

    @Override
    public void onUpgrade(
        SQLiteDatabase db,
        ConnectionSource connectionSource,
        int oldVersion,
        int newVersion)
    {
        Log.d(
            TAG_NAME,
            String.format("onUpgrade(db=%s [%d=>%d], connStr=%s)",
                db.toString(), oldVersion, newVersion,
                connectionSource.toString()));
        // try {
        // Log.d(TAG_NAME, "onUpgrade(): Refreshing table 'toc'...");
        // TableUtils.dropTable(connectionSource, TocItem.class, true);
        // Log.d(TAG_NAME, "onUpgrade(): Refreshing table 'book'...");
        // TableUtils.dropTable(connectionSource, BookItem.class, true);
        // onCreate(db, connectionSource);
        // } catch (SQLException e) {
        // throw new RuntimeException("Can't drop tables in database");
        // }
    }

    /**
     * Drops all tables and recreates them.
     * Used by unit tests only!!!
     * 
     * @throws SQLException
     */
    public void recreateDatabase()
        throws SQLException
    {
        final SQLiteDatabase database = getWritableDatabase();
        final ConnectionSource connection = getConnectionSource();

        TableUtils.dropTable(connection, TocItem.class, true);
        TableUtils.dropTable(connection, BookItem.class, true);

        onCreate(database, connection);
    }

    public Dao<TocItem, String> initTocDao()
        throws SQLException
    {
        if (tocDao == null) {
            tocDao = DaoManager.createDao(getConnectionSource(), TocItem.class);
        }
        return tocDao;
    }

    public Dao<BookItem, String> initBookDao()
        throws SQLException
    {
        if (bookDao == null) {
            bookDao = DaoManager.createDao(getConnectionSource(),
                BookItem.class);
        }
        return bookDao;
    }

    @Override
    public void close()
    {
        super.close();
        tocDao = null;
        bookDao = null;
    }

    /** for unit testing purposes */
    // private void fillDatabaseWithDummyData() {
    // final ScheduleActivityRepo activityService = new
    // ScheduleActivityRepo(mContext);
    // final Random rand = new Random();
    // final Date date = new Date();
    // final GregorianCalendar cal = new GregorianCalendar();
    // final StepRepo mStepService = new StepRepo(mContext);
    // final DateRepo mDateService = new DateRepo(mContext);
    // final List<Integer> activities = new ArrayList<Integer>();
    // Step step;
    // ScheduleActivity activity;
    // DbDate dbdate;
    //
    // //Wake up activity
    // activity = new ScheduleActivity("Good morning!");
    // activity.setImage("/mnt/sdcard/Visual Schedule Media/Visual Schedule Images/alarmclock"+IMG_EXTENSION);
    // activityService.create(activity);
    // date.setHours( 7 );
    // date.setMinutes( 30 );
    // dbdate = new DbDate(activity, date);
    // mDateService.create(dbdate);
    // activityService.update(activity);
    // activity = activityService.getById(activity.getId());
    //
    // step = new Step(activity, "Turn alarm clock off");
    // step.setImage("/mnt/sdcard/Visual Schedule Media/Visual Schedule Images/alarmclock"+IMG_EXTENSION);
    // mStepService.create(step);
    //
    // //Add some random activities
    // final int amountActivities = 4;
    // final int amountStepsPerActivity = 5;
    //
    // for (int i = 1; i <= amountActivities; i++) {
    // final ScheduleActivity act = new ScheduleActivity("Activity Example " +
    // i);
    // act.setImage("/mnt/sdcard/Visual Schedule Media/Visual Schedule Images/"
    // + (i % 14) + IMG_EXTENSION);
    //
    // activityService.create(act);
    // activities.add(act.getId());
    //
    // date.setHours( (cal.get(Calendar.HOUR_OF_DAY) + (rand.nextInt(10)) ) % 22
    // );
    // date.setMinutes( (cal.get(Calendar.MINUTE) + (rand.nextInt(60)) ) % 58 );
    //
    // dbdate = new DbDate(act, date);
    // mDateService.create(dbdate);
    //
    // activityService.update(act);
    // }
    //
    // ScheduleActivity act = activityService.getById(activities.get(0));
    //
    // step = new Step(act, "Step 1");
    // step.setImage("/mnt/sdcard/Visual Schedule Media/Visual Schedule Images/1"
    // + IMG_EXTENSION);
    // mStepService.create(step);
    //
    // //Generate image only steps
    // for (int i = 1; i <= amountActivities; i++) {
    // act = activityService.getById(activities.get(i-1));
    // for (int j = 1; j <= amountStepsPerActivity; j++) {
    // step = new Step(act, "Step " + j);
    // if (j % 2 == 0) {
    // step.setImage("/mnt/sdcard/Visual Schedule Media/Visual Schedule Images/"
    // + j + IMG_EXTENSION);
    // } else {
    // step.setImage("/mnt/sdcard/Visual Schedule Media/Visual Schedule Images/"
    // + ( j % 15) + IMG_EXTENSION);
    // step.setAudio("/mnt/sdcard/Visual Schedule Media/Visual Schedule Audio/"
    // + (j % 6) + IMG_EXTENSION);
    // }
    // mStepService.create(step);
    // }
    // }
    // }
}
