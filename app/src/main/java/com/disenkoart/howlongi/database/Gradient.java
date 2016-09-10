package com.disenkoart.howlongi.database;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.
/**
 * Entity mapped to table "GRADIENT".
 */
@Entity
public class Gradient {

    @Id(autoincrement = true)
    private long id;
    private int startColor;
    private int endColor;

    @Generated
    public Gradient() {
    }

    public Gradient(long id) {
        this.id = id;
    }

    @Generated
    public Gradient(long id, int startColor, int endColor) {
        this.id = id;
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStartColor() {
        return startColor;
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public int getEndColor() {
        return endColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }

}