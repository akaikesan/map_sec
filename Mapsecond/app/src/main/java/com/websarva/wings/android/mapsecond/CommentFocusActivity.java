package com.websarva.wings.android.mapsecond;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CommentFocusActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_focus);

        Intent i = getIntent();

        String ctt = i.getStringExtra("CONTENT");

        String username = i.getStringExtra("USERNAME");

        int fav =i.getIntExtra("FAV",0);

        TextView unv = findViewById(R.id.usernameView);
        unv.setTypeface(Typeface.DEFAULT_BOLD);

        unv.setText(username);

        TextView cttv = findViewById(R.id.contentView);

        cttv.setText(ctt);

        TextView favv = findViewById(R.id.favFocused);
        favv.setTypeface(Typeface.MONOSPACE);

        favv.setText(String.valueOf(fav));





    }
}
