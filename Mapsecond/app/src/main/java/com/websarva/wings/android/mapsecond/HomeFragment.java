package com.websarva.wings.android.mapsecond;


import android.graphics.Bitmap;
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

import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment {

    GetProfile getTask;

    GetImage getImage;

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

                    String username = json.getString("username");

                    String introduce = json.getString("introduce");

                    RecyclerView rv = view.findViewById(R.id.myRecyclerView);


                    RecycleViewAdapter adapter = new RecycleViewAdapter(JsonUtil.createDataSet(json));

                    LinearLayoutManager llm = new LinearLayoutManager(getContext());

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
        });

        getTask.execute();

        getImage = new GetImage();

        getImage.setOnCallBack(new GetImage.CallBackTask(){
            @Override
            void callBack(Bitmap image){
                System.out.println(image);


                if(getActivity() != null){
                    CircleImageView imageView = view.findViewById(R.id.iconImage);
                    imageView.setImageBitmap(image);

                }

            }


        });

        getImage.execute();






        return view;
    }

    @Override
    public void onDestroy(){
        if( getTask!=null)//cancel AsyncTask
            getTask.cancel(true);

        super.onDestroy();
    }

    static private class GetImage  extends AsyncTask<Void,Void,Bitmap> {
        private CallBackTask callbacktask;
        @Override
        protected Bitmap doInBackground(Void... voids) {
            return GetImageInBackground.getImage();
        }


        @Override
        protected void onPostExecute(Bitmap image) {
            callbacktask.callBack(image);
        }

        void setOnCallBack(CallBackTask cbt){
            callbacktask = cbt;
        }

        static class CallBackTask{
            void callBack(Bitmap image){
            }
        }
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




