package net.terang.dunia.gospels.in.unison.context;

import java.sql.*;

import net.terang.dunia.gospels.in.unison.dao.*;
import net.terang.dunia.gospels.in.unison.db.*;

import android.content.Context;

public class BookDbContext
{
    public BookDaoImpl book;
    private final DatabaseHelper db;

    public BookDbContext(Context context) throws SQLException
    {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        db = manager.getHelper(context);
        book = new BookDaoImpl(db);
    }
}
