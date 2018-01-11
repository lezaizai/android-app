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
import com.didlink.xingxing.R;
import com.didlink.xingxing.config.Constants;
import com.didlink.xingxing.models.LoginAuth;
import com.didlink.xingxing.security.ILoginListener;
import com.didlink.xingxing.security.LoginService;
import com.didlink.xingxing.service.RealmDBService;

import dmax.dialog.SpotsDialog;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = (Button) findViewById(R.id.sign_in_button);
        signInCancel = (Button) findViewById(R.id.sign_in_cancel);

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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


        LoginService loginService = AppSingleton.getInstance().getLoginService();
        loginService.setLoginListener(new ILoginListener() {
            @Override
            public void loginResponse(final LoginAuth auth) {

                mHandler.postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                signInButton.setEnabled(true);
                                signInCancel.setEnabled(true);
                                // onLoginFailed();
                                progressDialog.dismiss();
                                if (auth != null && auth.getStatus() == 0) {
                                    AppSingleton.getInstance().setLoginAuth(auth);
                                    AppSingleton.getInstance().getmRealmDBService().saveAuth(auth);
                                    Intent intent = new Intent();
                                    //intent.putExtra("userlogin", mUserlogin);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.error_login_auth), Toast.LENGTH_SHORT).show();
                                    mUserloginView.requestFocus();
                                }
                            }
                        }, 2000);

            }
        });

        loginService.doLogin(Constants.HTTP_BASE_URL,
                userlogin,
                password);
    }

}



