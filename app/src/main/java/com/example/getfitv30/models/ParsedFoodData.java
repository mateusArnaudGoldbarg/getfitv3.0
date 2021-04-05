package com.example.getfitv30.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParsedFoodData {

    @SerializedName("food")
    @Expose
    private Food food;

    public Food getFood()
    {
        return this.food;
    }

    public void setFood(Food food)
    {
        this.food = food;
    }

}
