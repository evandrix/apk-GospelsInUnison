package net.terang.dunia.gospels.in.unison.context;

import java.sql.*;

import net.terang.dunia.gospels.in.unison.dao.*;
import net.terang.dunia.gospels.in.unison.db.*;

import android.content.Context;

public class TocDbContext
{
    private final DatabaseHelper db;
    public TocDaoImpl toc;

    public TocDbContext(Context context) throws SQLException
    {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        db = manager.getHelper(context);
        toc = new TocDaoImpl(db);
    }
}
