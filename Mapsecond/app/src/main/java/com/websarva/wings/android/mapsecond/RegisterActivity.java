package com.websarva.wings.android.mapsecond;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class RegisterActivity extends AppCompatActivity {

    AutoCompleteTextView mEmailView;

    EditText mPasswordView;

    EditText mPasswordView2;

    EditText mUsername;

    UserRegisterTask mAuthTask;

    String up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmailView = findViewById(R.id.register_email);
        mPasswordView = findViewById(R.id.register_password);
        mPasswordView2 = findViewById(R.id.register_password2);
        mUsername = findViewById(R.id.register_username);



        Button mEmailSignInButton = findViewById(R.id.register_submission);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(is_verified()) {

                    try {

                        SecretKeySpec keySpec = new SecretKeySpec(GlobalValue.getCryptKey().getBytes(), "AES"); // キーファイル生成
                        Cipher cipher = Cipher.getInstance("AES");
                        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
                        byte[] encrypted = cipher.doFinal(mPasswordView.getText().toString().getBytes()); // byte配列を暗号化
                        up = Base64.encodeToString(encrypted, Base64.DEFAULT); // Stringにエンコード


                        SecretKeySpec keySpec2 = new SecretKeySpec(GlobalValue.getCryptKey().getBytes(), "AES"); // キーファイル生成
                        Cipher cipher2 = Cipher.getInstance("AES");
                        cipher2.init(Cipher.ENCRYPT_MODE, keySpec2);
                        byte[] encrypted2 = cipher2.doFinal(mPasswordView2.getText().toString().getBytes()); // byte配列を暗号化
                        String up2 = Base64.encodeToString(encrypted2, Base64.DEFAULT);


                        if (up.equals(up2)) {

                            mAuthTask = new UserRegisterTask(mEmailView.getText().toString(),mPasswordView.getText().toString(),mUsername.getText().toString());

                            mAuthTask.setOnCallBack(new UserRegisterTask.CallBackTask() {
                                @Override
                                public void CallBack(Boolean result) {
                                    super.CallBack(result);
                                    if(result){
                                        SharedPreferences data = getApplication().getSharedPreferences("DataSave", Context.MODE_PRIVATE);

                                        SharedPreferences.Editor editor = data.edit();

                                        editor.putString("EM", mEmailView.getText().toString());
                                        editor.putString("PW", up);
                                        editor.putBoolean("Is_Logged_device", true);
                                        // 保存
                                        editor.apply();
                                        Intent intent = new Intent(getApplication(),LoginActivity.class);
                                        getApplication().startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getApplication(),"sorry, Some Issue is occured.",Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                            mAuthTask.execute((Void) null);
                        }

                        // 入力されたログインIDとログインパスワード
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    boolean is_verified(){

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);



        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            return true;
        }

    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    static class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private UserRegisterTask.CallBackTask callBackTask;

        String regisEmail, regisPass, regisUsername;

        UserRegisterTask(String mEmail, String pass, String username){
            this.regisEmail = mEmail;
            this.regisPass = pass;
            this.regisUsername = username;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                return SendRegisterInfoInBackGround.sendRegisterInfo(regisEmail,regisPass,regisUsername);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            callBackTask.CallBack(result);
        }

        void setOnCallBack(CallBackTask _cbj) {
            callBackTask = _cbj;
        }


        /**
         * コールバック用のstaticなclass
         */
        static class CallBackTask {
            void CallBack(Boolean result) {
            }
        }




    }
}
