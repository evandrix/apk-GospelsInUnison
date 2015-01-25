package net.terang.dunia.gospels.in.unison.view;

import java.sql.*;

import net.terang.dunia.gospels.in.unison.*;
import net.terang.dunia.gospels.in.unison.context.*;
import net.terang.dunia.gospels.in.unison.model.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class BookAdapter
    extends BaseAdapter
{
    private static final String TAG_NAME = BookAdapter.class.getSimpleName();
    private final LayoutInflater mInflater;
    private final BookDbContext bookDbContext;
    private final Context context;
    private final Bundle bundle;
    private final int chapter;

    /**
     * Constructor
     * 
     * @param context
     * @throws SQLException
     */
    public BookAdapter(Context context, Bundle bundle) throws SQLException
    {
        this.context = context;
        this.bundle = bundle;
        this.chapter = bundle.getInt("CHAPTER");

        mInflater = LayoutInflater.from(context);
        bookDbContext = new BookDbContext(context);

        Log.d(TAG_NAME, "BookAdapter constructed successfully!");
    }

    @Override
    public int getCount()
    {
        try {
            return bookDbContext.book.length(chapter);
        } catch (SQLException e) {
            Log.e(TAG_NAME, "Error: unable to get length of chapter", e);
        }
        return 0;
    }

    @Override
    public BookItem getItem(int position)
    {
        try {
            Log.d(TAG_NAME,
                String.format("BookItem#%d retrieved successfully", position));

            // convert from 0-based (here) -> 1-based (SQL) indexing
            return bookDbContext.book.getContent(chapter, position + 1);

        } catch (SQLException e) {
            Log.e(TAG_NAME, String.format(
                "Error: unable to retrieve BookItem@%d", position), e);
        }
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View newView = convertView;
        if (newView == null) {
            newView = mInflater.inflate(R.layout.list_item, parent, false);
        }

        TextView txtBookItem = (TextView) newView.findViewById(R.id.listItem);
        Log.d(TAG_NAME, Integer.toString(position + 1));

        txtBookItem.setPadding(0, 0, 0, 0);

        // populate TextView with data from DB
        try {
            // convert from 0-based (here) -> 1-based (SQL) indexing
            BookItem item = bookDbContext.book
                            .getContent(chapter, position + 1);
            if (item != null) {
                Log.d(TAG_NAME, "" + item);
                txtBookItem.setText(Html.fromHtml(item.getContent()));
            }
        } catch (SQLException e) {
            Log.e(TAG_NAME, "Error: unable to get content of chapter", e);
        }

        return newView;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return false;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return false;
    }
}
