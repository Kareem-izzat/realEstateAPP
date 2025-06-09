package com.example.realestate.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.realestate.activites.WelcomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    public static List<Categories> categories = new ArrayList<>();
    public static List<Property> properties = new ArrayList<>();

    public static boolean parse(String json, Context context) {

        try {
            Log.d("JSON_DEBUG", "Raw JSON:\n" + json);
            JSONObject obj = new JSONObject(json);

            JSONArray cats = obj.getJSONArray("categories");
            for (int i = 0; i < cats.length(); i++) {
                JSONObject c = cats.getJSONObject(i);
                Categories cat = new Categories();
                cat.setId(c.getInt("id"));
                cat.setName(c.getString("name"));
                categories.add(cat);
            }

            JSONArray props = obj.getJSONArray("properties");
            for (int i = 0; i < props.length(); i++) {
                JSONObject p = props.getJSONObject(i);
                Property prop = new Property();
                prop.setId(p.getInt("id"));
                prop.setTitle(p.getString("title"));
                prop.setType(p.getString("type"));
                prop.setPrice(p.getInt("price"));
                prop.setLocation(p.getString("location"));
                prop.setArea(p.getString("area"));
                prop.setBedrooms(p.getInt("bedrooms"));
                prop.setBathrooms(p.getInt("bathrooms"));
                prop.setImage_url(p.getString("image_url"));
                prop.setDescription(p.getString("description"));
                properties.add(prop);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Property findPropertyById(int id) {
        for (Property p : properties) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
}
