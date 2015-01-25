package net.terang.dunia.gospels.in.unison.model;

import javax.persistence.*;

@Entity(name = "book")
public class BookItem
{
    @Column
    private int chapter;

    @Column
    private int verse;

    @Column
    private String content;

    public BookItem()
    {
        // required by ORMLite
    }

    public BookItem(int chapter, int verse, String content)
    {
        super();
        this.chapter = chapter;
        this.verse = verse;
        this.content = content;
    }

    public int getChapter()
    {
        return chapter;
    }

    public void setChapter(int chapter)
    {
        this.chapter = chapter;
    }

    public int getVerse()
    {
        return verse;
    }

    public void setVerse(int verse)
    {
        this.verse = verse;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @Override
    public String toString()
    {
        return String.format("%d:%d %s", chapter, verse, content);
    }
}