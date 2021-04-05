package com.example.getfitv30.models;

public class UserWeights
{
    private long id;
    private int weight;
    private String date;

    public long getId()
    {
        return this.id;
    }

    public int getWeight()
    {
        return this.weight;
    }

    public String getDate()
    {
        return this.date;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    public void setDate(String date)
    {
        this.date = date;
    }
}
