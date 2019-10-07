package com.websarva.wings.android.mapsecond;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {






    SharedPreferences data;
    boolean is_logged_device;


    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @SuppressLint("GetInstance")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        data = this.getSharedPreferences("DataSave", Context.MODE_PRIVATE);

        View addview = getLayoutInflater().inflate(R.layout.activity_login,null);
        TextView toRegister = addview.findViewById(R.id.registerInstruction);

        toRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),RegisterActivity.class);
                getApplication().startActivity(intent);

            }
        });


        is_logged_device = data.getBoolean("Is_Logged_device", false);

        MultiDex.install(this);
        if(is_logged_device){

            String up = "";
            SecretKeySpec keySpec = new SecretKeySpec(GlobalValue.getCryptKey().getBytes(), "AES"); // キーファイル生成 暗号化で使った文字列と同様にする
            Cipher cipher;
            String email = data.getString("EM",null);
            String pass = data.getString("PW",null);

            try{

                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, keySpec);
                byte[] decByte = Base64.decode(pass, Base64.DEFAULT); // byte配列にデコード
                byte[] decrypted = cipher.doFinal(decByte); // 複合化
                up = new String(decrypted); // Stringに変換
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


            mAuthTask = new UserLoginTask(email, up);
            mAuthTask.setOnCallBack(new UserLoginTask.CallBackTask(){
                 @Override
                public void CallBack(Boolean result){
                     super.CallBack(result);
                     Log.wtf("omg", "hahahahah");

                    try {
                        if (result) {
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            getApplication().startActivity(intent);
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(),"sorry, no response",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                 }
            });
            mAuthTask.execute((Void) null);
        }else{

            setContentView(R.layout.activity_login);
            // Set up the login form.
            mEmailView = findViewById(R.id.email);


            mPasswordView = findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                    //
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                        attemptLogin();//start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!t
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuthTask = attemptLogin();
                    assert mAuthTask != null;
                    mAuthTask.setOnCallBack(new UserLoginTask.CallBackTask(){
                        @Override
                        public void CallBack(Boolean result){
                            super.CallBack(result);
                            Log.wtf("omg", "hahahahah");

                            SharedPreferences data = getApplication().getSharedPreferences("DataSave", Context.MODE_PRIVATE);

                            try {
                                SharedPreferences.Editor editor = data.edit();
                                editor.putString("EM", mEmailView.getText().toString());

                                SecretKeySpec keySpec = new SecretKeySpec(GlobalValue.getCryptKey().getBytes(), "AES"); // キーファイル生成
                                Cipher cipher = Cipher.getInstance("AES");
                                cipher.init(Cipher.ENCRYPT_MODE, keySpec);
                                byte[] encrypted = cipher.doFinal(mPasswordView.getText().toString().getBytes()); // byte配列を暗号化
                                String up = Base64.encodeToString(encrypted, Base64.DEFAULT); // Stringにエンコード

                                // 入力されたログインIDとログインパスワード
                                editor.putString("PW", up);
                                editor.putBoolean("Is_Logged_device",true);

                                // 保存
                                editor.apply();
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

                            Intent intent = new Intent(getApplication(),MainActivity.class);
                            getApplication().startActivity(intent);



                        }
                    });
                    mAuthTask.execute((Void) null);
                }
            });

        }
    }
    //mayRequestContacts -> 107




    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private UserLoginTask attemptLogin() {
        if (mAuthTask != null) {
            return null;
        }

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
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            mAuthTask = new UserLoginTask(email, password);

            return mAuthTask;
        }

        return null;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }




}


