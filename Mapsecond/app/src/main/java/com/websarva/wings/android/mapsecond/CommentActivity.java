package com.websarva.wings.android.mapsecond;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class CommentActivity extends FragmentActivity implements View.OnClickListener{


    EditText editText;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 親クラスのonCreate()の呼び出し
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // 戻るボタンを取得
        // 戻るボタンにリスナを登録
        findViewById(R.id.btCancel).setOnClickListener(this);
        findViewById(R.id.btContribution).setOnClickListener(this);



    }


    //ウィンドウのフォーカス変更イベント
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //EditTextのインスタンスを取得
        editText = findViewById(R.id.etComment);

        //EditTextがフォーカスを取得した場合
        if (hasFocus && editText == getCurrentFocus()) {
            new Handler().postDelayed(showKeyboardDelay, 0);
        }


    }

    // ソフトキーボード表示の遅延実行処理
    private final Runnable showKeyboardDelay = new Runnable() {
        @Override
        public void run() {
            EditText editText = findViewById(R.id.etComment);
            //ソフトキーボードを表示
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, 0);
        }
    };



    @Override
    public void onClick(View v) {

        String content = editText.getText().toString();
        if(v != null) {
            switch (v.getId()){
                case R.id.btCancel:
                    finish();
                    break;
                case R.id.btContribution:
                    Intent i = getIntent();

                    double lat = (double) i.getFloatExtra("LAT",300);

                    double lon = (double) i.getFloatExtra("LON",300);



                    SendComment mTask = new SendComment(content,lat,lon);
                    mTask.execute((Void) null);

                    break;






            }
        }
    }

}
