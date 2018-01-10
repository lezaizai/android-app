package com.didlink.xingxing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.didlink.xingxing.AppSingleton;

import dmax.dialog.SpotsDialog;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A login screen that offers login via username.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mUserloginView;
    private EditText mPasswordView;

    private String mUserlogin;
    private String mPassword;
    private Button signInButton;
    private Button signInCancel;
    private SpotsDialog progressDialog;
    private Handler mHandler = new Handler();

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = (Button) findViewById(R.id.sign_in_button);
        signInCancel = (Button) findViewById(R.id.sign_in_cancel);

        AppSingleton app = (Application) getApplication();
        mSocket = app.getSocket();

        // Set up the login form.
        mUserloginView = (EditText) findViewById(R.id.input_login);
        mPasswordView = (EditText) findViewById(R.id.input_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                Log.d("onEditorAction", ""+id+","+keyEvent);
                //Log.d("onEditorAction", ""+R.id.login+","+KeyEvent.KEYCODE_ENTER);
                //Log.d("onEditorAction", ""+EditorInfo.IME_NULL+","+EditorInfo.IME_ACTION_UNSPECIFIED);
                //Log.d("onEditorAction", ""+keyEvent.getKeyCode());
                //if (id == R.id.login || id == EditorInfo.IME_NULL) {
                if (id==EditorInfo.IME_ACTION_DONE
                        ||(keyEvent!=null&&keyEvent.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        signInCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("userid", "");
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        mSocket.on("signed", onSigned);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.off("signed", onSigned);
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUserloginView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userlogin = mUserloginView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        // Check for a valid username.
        if (TextUtils.isEmpty(userlogin)) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            mUserloginView.setError(getString(R.string.error_field_required));
            mUserloginView.requestFocus();
            return;
        } else if (!userlogin.matches("[1]\\d{10}") && !android.util.Patterns.EMAIL_ADDRESS.matcher(userlogin).matches()) {
            mUserloginView.setError(getString(R.string.error_phone_email));
            mUserloginView.requestFocus();
            return;
        } else {
            mUserloginView.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            mPasswordView.setError(getString(R.string.error_password_format));
            mPasswordView.requestFocus();
            return;
        } else {
            mPasswordView.setError(null);
        }

        signInButton.setEnabled(false);
        signInCancel.setEnabled(false);

        progressDialog = new SpotsDialog(LoginActivity.this, R.style.Logining);
        //progressDialog.setIndeterminate(true);
        //progressDialog.setMessage(getString(R.string.processing_sign));
        progressDialog.show();

        mUserlogin = userlogin;
        JSONObject data = new JSONObject();
        try {
            data.put("userlogin",userlogin);
            data.put("password",password);
        } catch (JSONException e) {
            Log.e("LoginaAtivity",e.toString());
            return;
        }

        // perform the user login attempt.
        mSocket.emit( "sign in", data);
    }

    private Emitter.Listener onSigned = new Emitter.Listener() {
        boolean result;
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            JSONObject user;
            int errcode;
            String avatar = "";
            String uid = "";
            String nickname = "";

            int numUsers = 1;
            try {
                result = data.getBoolean("result");
                if (result) {
                    user = (JSONObject) data.getJSONObject("user");
                    uid = user.getString("uid");
                    mUserlogin = user.getString("username");
                    avatar = user.getString("gravatarpicture");
                    nickname = user.getString("nickname");
                } else {
                    errcode = data.getInt("user");
                }
            } catch (JSONException e) {
                Log.e("LoginActivity", e.toString());
                result = false;
                errcode = -3;
            }
            try {
                DB snappydb = DBFactory.open(getApplicationContext()); //create or open an existing databse using the default name
                snappydb.put(Constants.DBKEY_USERLOGIN, mUserlogin);
                snappydb.put(Constants.DBKEY_GRAVATARPICTURE, avatar);
                snappydb.put(Constants.DBKEY_USERID, uid);
                snappydb.put(Constants.DBKEY_NICKNAME, nickname);
                snappydb.putBoolean(Constants.DBKEY_LOGIN, true);
                snappydb.close();

                DsnApplication app = (DsnApplication) getApplication();
                app.setLoginname(mUserlogin);
                app.setUserid(uid);
                app.setAvatar(avatar);
                app.setNickname(nickname);
                app.setIsLogin(true);
                app.getSocketIORTCClient().start(uid);
            } catch (SnappydbException e) {
                Log.e("mSettingFreeMsg",e.toString());
            }

            mHandler.postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            signInButton.setEnabled(true);
                            signInCancel.setEnabled(true);
                            // onLoginFailed();
                            progressDialog.dismiss();
                            if (result) {
                                Intent intent = new Intent();
                                //intent.putExtra("userlogin", mUserlogin);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            if (!result) {
                                Toast.makeText(getApplicationContext(), getString(R.string.error_login_auth), Toast.LENGTH_SHORT).show();
                                mUserloginView.requestFocus();
                            }
                        }
                    }, 2000);
        }
    };

}



