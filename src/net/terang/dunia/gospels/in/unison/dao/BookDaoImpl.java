package net.terang.dunia.gospels.in.unison.dao;

import java.sql.*;
import java.util.*;

import net.terang.dunia.gospels.in.unison.db.*;
import net.terang.dunia.gospels.in.unison.model.*;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.stmt.*;

/**
 * implements CRUD, among other utility functions
 */
public class BookDaoImpl
{
    private final Dao<BookItem, String> bookDao;

    public BookDaoImpl(DatabaseHelper db) throws SQLException
    {
        bookDao = db.initBookDao();
    }

    public int create(BookItem article)
        throws SQLException
    {
        return bookDao.create(article);
    }

    public int update(BookItem article)
        throws SQLException
    {
        return bookDao.update(article);
    }

    public int delete(BookItem article)
        throws SQLException
    {
        return bookDao.delete(article);
    }

    public BookItem getContent(int chapter, int verse)
        throws SQLException
    {
        // get our query builder from the DAO
        QueryBuilder<BookItem, String> queryBuilder = bookDao.queryBuilder();
        // the 'password' field must be equal to "qwerty"
        queryBuilder.where().eq("chapter", chapter).and().eq("verse", verse);
        // prepare our sql statement
        PreparedQuery<BookItem> preparedQuery = queryBuilder.prepare();
        // query for all accounts that have that password
        return bookDao.queryForFirst(preparedQuery);
    }

    public int length(int chapter)
        throws SQLException
    {
        return getAll(chapter).size();
    }

    public List<BookItem> getAll(int chapter)
        throws SQLException
    {
        // get our query builder from the DAO
        QueryBuilder<BookItem, String> queryBuilder = bookDao.queryBuilder();
        // the 'password' field must be equal to "qwerty"
        queryBuilder.where().eq("chapter", chapter);
        // prepare our sql statement
        PreparedQuery<BookItem> preparedQuery = queryBuilder.prepare();
        // query for all accounts that have that password
        return bookDao.query(preparedQuery);
    }
}
