package com.websarva.wings.android.mapsecond;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment {

    GetProfile getTask;
    String username;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_home,null);


        getTask = new GetProfile();

        getTask.setOnCallBack(new GetProfile.CallBackTask(){
            @Override
            void callBack(String str_profile_json){
                try {

                    JSONObject json = new JSONObject(str_profile_json);

                    username = json.getString("username");

                    String introduce = json.getString("introduce");

                    RecyclerView rv = view.findViewById(R.id.myRecyclerView);


                    RecycleViewAdapter adapter = new RecycleViewAdapter(JsonUtil.createDataSet(json),getContext());

                    LinearLayoutManager llm = new LinearLayoutManager(getContext());

                    rv.setHasFixedSize(true);

                    rv.setLayoutManager(llm);

                    rv.setAdapter(adapter);

                    TextView tv = view.findViewById(R.id.HomeUsername);

                    tv.setText(username);

                    TextView tv_sec = view.findViewById(R.id.textView);

                    tv_sec.setText(introduce);

                    String url = "http://" + GlobalValue.getHost() + ":" + GlobalValue.getPort() + "/" + GlobalValue.getPath() + "/image/"+ username;
                    if(getContext() != null)  Glide.with(getContext()).load(url).into((CircleImageView) view.findViewById(R.id.iconImage));



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });






        getTask.execute();

        return view;
    }

    @Override
    public void onDestroy(){
        if( getTask!=null)//cancel AsyncTask
            getTask.cancel(true);

        super.onDestroy();
    }



    static private class GetProfile extends AsyncTask<Void,Void,String> {



        private CallBackTask callbacktask;

        @Override
        protected String doInBackground(Void... params) {
            return GetProfileInBackGround.getGET();
        }


        @Override
        protected void onPostExecute(final String str_profile_json) {
            callbacktask.callBack(str_profile_json);
        }

        void setOnCallBack(CallBackTask _cbt){
            callbacktask = _cbt;
        }

        static class CallBackTask {
            void callBack(String str_profile_json) {
            }
        }


    }



}




