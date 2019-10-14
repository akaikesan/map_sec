package com.websarva.wings.android.mapsecond;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class SendBitmap extends AsyncTask<Void,Void,Boolean> {

    private Bitmap image;


    SendBitmap(Bitmap image){
        this.image = image;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        SendBitmapInBackGround sender = new SendBitmapInBackGround(image);

        return sender.send_bm();

    }
}
