package net.terang.dunia.gospels.in.unison.view;

import java.io.*;
import java.sql.*;
import java.util.*;

import net.terang.dunia.gospels.in.unison.*;
import net.terang.dunia.gospels.in.unison.activity.*;
import net.terang.dunia.gospels.in.unison.context.*;
import net.terang.dunia.gospels.in.unison.db.*;
import net.terang.dunia.gospels.in.unison.model.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class TocAdapter
    extends BaseAdapter
{
    private static final String TAG_NAME = TocAdapter.class.getSimpleName();
    private final LayoutInflater mInflater;

    private final Context context;
    private final TocDbContext tocDbContext;
    private final BookDbContext bookDbContext;

    /**
     * Constructor
     * 
     * @param context
     * @throws SQLException
     * @throws IOException
     */
    public TocAdapter(Context context) throws SQLException, IOException
    {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        tocDbContext = new TocDbContext(context);
        bookDbContext = new BookDbContext(context);
        Log.d(TAG_NAME, "TocAdapter constructed successfully!");
    }

    @Override
    public int getCount()
    {
        try {
            return tocDbContext.toc.length();
        } catch (SQLException e) {
            Log.e(TAG_NAME, "Error: unable to get count of ToC items", e);
        }
        return 0;
    }

    @Override
    public TocItem getItem(int position)
    {
        try {
            Log.d(TAG_NAME,
                String.format("ToC#%d retrieved successfully", position));

            // convert from 0-based (here) -> 1-based (SQL) indexing
            return tocDbContext.toc.getById(position + 1);
        } catch (SQLException e) {
            Log.e(TAG_NAME,
                String.format("Error: unable to retrieve item#%d", position, e));
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
        // setup view
        View newView = convertView;
        if (newView == null) {
            newView = mInflater.inflate(R.layout.list_item, parent, false);
        }
        TextView txtTocItem = (TextView) newView.findViewById(R.id.listItem);

        // query DB for item
        TocItem thisItem = getItem(position);

        // populate textview with db data
        Log.d(TAG_NAME, "getView(): thisItem - " + thisItem);

        txtTocItem.setText("" + thisItem);
        List<BookItem> items;
        try {
            items = bookDbContext.book.getAll(thisItem.getId());

            // highlight uncompleted chapters in red (HTML)
            if (items.size() <= 0) {
                txtTocItem.setText(Html.fromHtml(String.format(
                    "<font color=red>%s</font>", thisItem)));
            }
        } catch (SQLException e) {
            Log.e(TAG_NAME, "Error: unable to retrieve items from database", e);
        }

        // attach event handler
        final int pos = position;
        txtTocItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG_NAME, "TOCItem#" + (pos + 1) + " was clicked!");
                Intent myIntent = new Intent(context, BookListActivity.class);

                // pass parameters onto next Activity //
                // 1. first create the bundle and initialize it
                Bundle bundle = new Bundle();
                // 2. then add the parameters to bundle as required
                bundle.putString("TITLE", getItem(pos).getTitle());
                bundle.putInt("CHAPTER", pos + 1);
                // 3. add this bundle to the intent
                myIntent.putExtras(bundle);

                // now finally start next Activity
                v.getContext().startActivity(myIntent);
            }
        });

        return newView;
    }
}
