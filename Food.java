package com.example.android.ehotelsapp;

import java.util.ArrayList;

public class Food
{
    private String foodType;
    private Long foodCost;

    public String getFoodType()
    {
        return foodType;
    }

    public void setFoodType(String foodType)
    {
        this.foodType = foodType;
    }

    public Long getFoodCost()
    {
        return foodCost;
    }

    public void setFoodCost(Long foodCost)
    {
        this.foodCost = foodCost;
    }

    public Food(String food, Long cost)
    {
        foodType = food;
        foodCost = cost;
    }

    public static ArrayList<String> convertToArray(ArrayList<Food> foodArray)
    {
        ArrayList<String> finalFood = new ArrayList<>();
        for(int i = 0; i < foodArray.size(); i++)
        {
            String foodType = foodArray.get(i).getFoodType();
            String foodCost = foodArray.get(i).getFoodCost().toString();
            finalFood.add(foodType + foodCost);
        }
        return finalFood;
    }

}
