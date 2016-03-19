package com.example.dmitry.androidrestclient.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dmitry on 14.02.16.
 */
public class Credit implements Serializable{
    private String _id;
    private String name;
    private Number sum;
    private Date date;

    public Credit(String name, Number sum) {
        this.name = name;
        this.sum = sum;
        this.date = null;
        this._id = null;
    }

    public Credit(String _id, String name, Number sum, Date date) {
        this._id = _id;
        this.name = name;
        this.sum = sum;
        this.date = date;
    }

    public Credit(String name, Number sum, Date date) {
        this.name = name;
        this.sum = sum;
        this.date = date;
        this._id = null;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Number getSum() {
        return sum;
    }

    public void setSum(Number sum) {
        this.sum = sum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Credit) && ((Credit) o).get_id().equals(_id);
    }
}
