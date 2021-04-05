package com.example.getfitv30.data;

import android.os.AsyncTask;

import com.example.getfitv30.models.Food;
import com.example.getfitv30.models.ParsedFoodData;
import com.example.getfitv30.views.InsertMealActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FetchEdamam extends AsyncTask<Void,Void,Void>
{
    private static final String API_URL = "https://api.edamam.com/api/food-database/v2/parser?nutrition-type=logging";
    private static final String APP_ID = "3a05913f";
    private static final String APP_KEY = "63848f04ae8749a91754aa7e5f9044bb";

    private String data = "";

    private List<ParsedFoodData> parsed = null;
    private Food food = null;

    private String foodname;
    private int amount;

    private double kcal;

    public FetchEdamam(String food, int amount)
    {
        this.foodname = food;
        this.amount = amount;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        try {
            URL url = new URL(API_URL + "&ingr=" + foodname + "&app_id=" + APP_ID +"&app_key=" + APP_KEY);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream             = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while(line != null) {
                line = bufferedReader.readLine();
                this.data += line;
            }

            JSONObject JO = new JSONObject(this.data);
            String p      = JO.getString("parsed");

            JSONArray Parsed     = new JSONArray(p);

            JSONObject food      = Parsed.getJSONObject(0);
            JSONObject ff        = food.getJSONObject("food");
            JSONObject nutrients = ff.getJSONObject("nutrients");

            this.kcal = (nutrients.getDouble("ENERC_KCAL")) * (this.amount / 100.0);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        InsertMealActivity.displayCalories(this.kcal);
    }
}