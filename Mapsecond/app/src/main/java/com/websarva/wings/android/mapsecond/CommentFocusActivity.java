package com.websarva.wings.android.mapsecond;

import android.content.Intent;
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

        TextView unv = findViewById(R.id.usernameView);

        unv.setText(username);

        TextView cttv = findViewById(R.id.contentView);

        cttv.setText(ctt);

    }
}
