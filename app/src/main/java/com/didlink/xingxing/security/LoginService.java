package com.didlink.xingxing.security;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class LoginService {
    private ILoginListener mLoginListener;

    public interface RestLoginService {
        @POST("rest/api/login")
        Call<LoginAuth> login(@Body LoginAuth body);
    }

    public void setLoginListener(ILoginListener loginListener) {
        this.mLoginListener = loginListener;
    }

    public void doLogin(String baseurl, String username, String password) {
        System.out.println("Start login to url: " + baseurl);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestLoginService service = retrofit.create(RestLoginService.class);

        LoginAuth auth = new LoginAuth();
        auth.setUsername(username);
        auth.setPassword(password);

        Call<LoginAuth> call = service.login(auth);
        call.enqueue(new Callback<LoginAuth>() {
            @Override
            public void onResponse(Call<LoginAuth> call, retrofit2.Response<LoginAuth> response) {
                Log.i("LoginService", "onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
                Log.e("LoginService", response.body().toString());
                if (mLoginListener != null) {
                    mLoginListener.loginResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginAuth> call, Throwable t) {
                Log.e("LoginService", "onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
            }
        });
//        try {
//            LoginAuth loginAuth = call.execute().body();
//
//            Log.e("LoginService", loginAuth.toString());
//
//            auth.setStatus(loginAuth.getStatus());
//        } catch (IOException e) {
//            System.err.println(e.toString());
//        }
//        return auth;
    }

}
