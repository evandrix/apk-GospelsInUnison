package net.terang.dunia.gospels.in.unison.view;

import net.terang.dunia.gospels.in.unison.*;
import android.content.*;
import android.graphics.drawable.*;
import android.util.*;
import android.view.*;

public class Banner
    extends View
{
    private Drawable logo;

    private void draw(Context context)
    {
        logo = context.getResources().getDrawable(R.drawable.logo);
        setBackgroundDrawable(logo);
    }

    public Banner(Context context)
    {
        super(context);
        draw(context);
    }

    public Banner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        draw(context);
    }

    public Banner(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        draw(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width * logo.getIntrinsicHeight()
                        / logo.getIntrinsicWidth();
        setMeasuredDimension(width, height);
    }
}
