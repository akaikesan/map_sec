package com.websarva.wings.android.mapsecond;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetProfile extends AsyncTask<Void,Void,Boolean> {

    private String str_profile_json;

    @SuppressLint("StaticFieldLeak")
    private View view;

    @SuppressLint("StaticFieldLeak")
    private Context context;

    GetProfile(View view,Context context){
        this.view = view;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {


        GetProfileInBackGround ProfileJson = new GetProfileInBackGround();


        str_profile_json = ProfileJson.getGET();



        return null;
    }


    @Override
    protected void onPostExecute(final Boolean success) {


        try {
            JSONObject json = new JSONObject(str_profile_json);

            String username = json.getString("username");

            String introduce = json.getString("introduce");



            RecyclerView rv = view.findViewById(R.id.myRecyclerView);



            RecycleViewAdapter adapter = new RecycleViewAdapter(this.createDataSet(json));

            LinearLayoutManager llm = new LinearLayoutManager(context);

            rv.setHasFixedSize(true);

            rv.setLayoutManager(llm);

            rv.setAdapter(adapter);


            TextView tv = view.findViewById(R.id.textView3);

            tv.setText(username);

            TextView tv_sec = view.findViewById(R.id.textView);

            tv_sec.setText(introduce);



        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private List<RowData> createDataSet(JSONObject jsonobj) {

        List<RowData> dataset = new ArrayList<>();
        for (int i = 0; i < jsonobj.length()-2 ;i++) {

            RowData data = new RowData();


            try {
                data.setTitle(jsonobj.getString("comment"+i));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            dataset.add(data);
        }
        return dataset;


    }
}
