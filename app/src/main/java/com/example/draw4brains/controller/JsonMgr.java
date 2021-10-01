package com.example.draw4brains.controller;

import android.content.Context;
import android.util.Log;

import com.example.draw4brains.model.Node;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JsonMgr {

    // TEMP METHOD - May change when linked with database
    private static String get_json(Context context, String file_name){
        String json = null;
        try {
            InputStream is = context.getAssets().open(file_name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    // TEMP METHOD - May change when linked with database
    private static JSONObject getNodesObject(String json, String level) {
        JSONObject obj = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            obj = jsonArray.getJSONObject(0);

            JSONObject levels = obj.getJSONObject("level");
            JSONObject chosen_level = levels.getJSONObject(level);
            obj = chosen_level;

            Log.d("JSON_V", chosen_level.toString());

        } catch (JSONException e) {
//            Toast.makeText(getApplicationContext(), String.format("Level %s not found", level), Toast.LENGTH_LONG).show();
            Log.d("JSON_V", "ERROR!");
        }

        return obj;
    }

    // TEMP METHOD - May change when linked with database
    public static ArrayList<Node> getNodeInfoFromSource(Context context, String filename, String level) {

        // For now open JSON file but can be flexible

        ArrayList<Node> preprocessedArray = new ArrayList<Node>();
        String json = get_json(context, filename);
        JSONObject levelInformation = getNodesObject(json, level);
        try {
//            int noOfNodes = Integer.valueOf(levelInformation.getString("node_count"));
            JSONArray nodeArray = levelInformation.getJSONArray("nodes");
            for (int i=1; i<=nodeArray.length(); i++) {
                JSONObject nodeInArray = nodeArray.getJSONObject(i-1);
                String node_num = nodeInArray.getString("node_num");
                int x = Integer.valueOf(nodeInArray.getString("x"));
                int y = Integer.valueOf(nodeInArray.getString("y"));
                Node node = new Node(node_num, x, y);
                preprocessedArray.add(node);
            }
        } catch (JSONException e)   {
            Log.d("JSON", "Error during creation of circle");
            e.printStackTrace();
        }

        return preprocessedArray;
    }


}
