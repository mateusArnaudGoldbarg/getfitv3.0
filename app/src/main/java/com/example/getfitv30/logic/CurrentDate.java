package com.example.getfitv30.logic;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentDate
{
    private String format = "dd-MM-yyyy";

    private String current_date_string;

    public CurrentDate(String format)
    {
        this.format = format;
        this.formatDate();
    }

    public CurrentDate()
    {
        this.formatDate();
    }

    private void formatDate()
    {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(this.format);
        this.current_date_string = formatter.format(date);
    }

    public String getDate()
    {
        return this.current_date_string;
    }
}
