package com.example.getfitv30.logic;

public class BMICalculator
{
    private static final float CM_TO_M_RATE = 100;

    private int weight;
    private float height;

    public BMICalculator(int weight, int height)
    {
        this.weight = weight;
        this.height = height / CM_TO_M_RATE;
    }

    // Calculating the body mass index for the current width and height
    public float calculateBMI()
    {
        return this.weight / (this.height * this.height);
    }
}
