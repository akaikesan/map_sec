package com.websarva.wings.android.mapsecond;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class JsonUtil {
    static List<RowData> createDataSet(JSONObject jsonobj) {
        List<RowData> dataset = new ArrayList<>();
        //System.out.println(jsonobj);
        for (int i = 0; i < jsonobj.length()-2 ;i++) {

            RowData data = new RowData();



            try {
                data.setUsername(jsonobj.getString("username"));
                JSONObject jvalue = jsonobj.getJSONObject("comment"+i);
                data.setTitle(jvalue.getString("content"));
                data.setFav(jvalue.getInt("fav"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            dataset.add(data);
        }
        return dataset;


    }
}
