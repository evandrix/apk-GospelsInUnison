package net.terang.dunia.gospels.in.unison.model;

import javax.persistence.*;

@Entity(name = "toc")
public class TocItem
{
    @Id
    private int id;

    @Column
    private String title;

    public TocItem()
    {
        // required by ORMLite
    }

    public TocItem(int id, String title)
    {
        super();
        this.id = id;
        this.title = title;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public String toString()
    {
        return String.format("%d: %s", id, title);
    }
}