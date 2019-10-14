package com.websarva.wings.android.mapsecond;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import org.json.JSONException;
import org.json.JSONObject;

import pub.devrel.easypermissions.EasyPermissions;


public class HomeFragment extends Fragment {

    GetProfile getTask;
    String username;
    final private int GALLERY_REQUEST_CODE = 1;
    CircleImageView circleImage;


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

                    circleImage = view.findViewById(R.id.iconImage);

                    circleImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v){

                            if(getContext() == null){return;}

                                String[] galleryPermissions = new String[0];
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                galleryPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            }else{
                                Toast.makeText(getContext(),"update your android to update your Icon",Toast.LENGTH_LONG).show();
                            }

                            if (!EasyPermissions.hasPermissions(getContext(), galleryPermissions)) {
                                EasyPermissions.requestPermissions(getContext(), "Access for storage",
                                        101, galleryPermissions);
                            }

                            pickFromGallery();
                        }
                    });

                    if(getContext() != null)  Glide.with(getContext()).load(url).signature(new ObjectKey(System.currentTimeMillis())).into(circleImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });






        getTask.execute();

        return view;
    }
    private void pickFromGallery(){

        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        Log.wtf("HomeFragment","pickFromGallery");
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        Log.wtf("HomeFragment","onActibityResult started");

        if (resultCode == Activity.RESULT_OK && getContext() != null)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    if(selectedImage == null){
                        return;
                    }else {

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        // Get the cursor
                        Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        if(cursor != null){
                            cursor.moveToFirst();
                        }else{
                            return;
                        }
                        // Move to first row
                        //Get the column index of MediaStore.Images.Media.DATA
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        //Gets the String value in the column
                        String imgDecodableString = cursor.getString(columnIndex);


                        cursor.close();

                        Bitmap fileToSend = resizeBitmap(BitmapFactory.decodeFile(imgDecodableString));


                        if(fileToSend == null){
                            Log.wtf("Homefragment","file is null");
                            return;
                        }


                        SendBitmap sendBMTask = new SendBitmap(fileToSend);
                        Log.wtf("Home Fragment","Image sent");
                        sendBMTask.execute();

                    }
                    break;
            }

    }

    public Bitmap resizeBitmap(Bitmap beforeResizeBitmap){


            // リサイズ前のBitmap
            // リサイズ比
            double resizeScale;
            // 横長画像の場合
            if (beforeResizeBitmap.getWidth() >= beforeResizeBitmap.getHeight()) {
                resizeScale = (double) circleImage.getWidth() / beforeResizeBitmap.getWidth();
            }
            // 縦長画像の場合
            else {
                resizeScale = (double) circleImage.getHeight() / beforeResizeBitmap.getHeight();
            }
            // リサイズ

            Bitmap scaledBitmap;
            scaledBitmap = Bitmap.createScaledBitmap(beforeResizeBitmap,
                    (int) (beforeResizeBitmap.getWidth() * resizeScale),
                    (int) (beforeResizeBitmap.getHeight() * resizeScale),
                    true);
            return scaledBitmap;




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




