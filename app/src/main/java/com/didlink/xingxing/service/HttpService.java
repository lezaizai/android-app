package com.didlink.xingxing.service;

import android.util.Log;

import com.didlink.xingxing.AppSingleton;
import com.didlink.xingxing.config.Constants;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by xingxing on 2016/5/15.
 */
public class HttpService {

    private AppSingleton app;
    private OnGetCustomerCntListener mGetCustomerListener;

    public static class Customer {
        public final String d;
        public final String r;
        public final String es;
        public final String ho;
        public final String pe;

        public Customer(String d, String r, String es, String ho, String pe) {
            this.d = d;
            this.r = r;
            this.es = es;
            this.ho = ho;
            this.pe = pe;
        }
    }

    public void setApp(AppSingleton app) {
        this.app = app;
    }
    public interface Disfanscn {
        @GET("/client/customer/shanghai/{year}/{month}/{day}")
        Call<List<Customer>> contributors(
                @Path("year") String year,
                @Path("month") String month,
                @Path("day") String day);
    }

    public interface OnGetCustomerCntListener{

       void GetCustomerCnt(int cnt);
    }

    public void setGetCustomerCntListener(OnGetCustomerCntListener listener) {
        mGetCustomerListener = listener;
    }

    public void getCustomerCnt() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        Log.i("getCustomerCnt", year + "/" +  month + "/" +  day);
        //getCustomerCnt(year, month, day);
        getCustomerCnt(year, "6", "16");
    }

    public void getCustomerCnt(String year, String month, String day) {
        // Create a very simple REST adapter which points the GitHub API.
        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HTTP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        // Create an instance of our GitHub API interface.
        Disfanscn disfanscn = retrofit.create(Disfanscn.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<List<Customer>> call = disfanscn.contributors(year, month, day);

        call.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>>  response) {
                try {
                    if (response.code() == 200) {
                        List<Customer> customers = response.body();
                        if (customers != null || customers.size() != 0) {
                            Customer customer = customers.get(0);
                            int customercnt = Integer.parseInt(customer.r, 10);
                            if (mGetCustomerListener != null) {
                                mGetCustomerListener.GetCustomerCnt(customercnt);
                            }
                        }
                    } else {
                        Log.e("getCustomerCnt", response.errorBody().string());
                    }
                } catch (IOException e) {
                    Log.e("getCustomerCnt", e.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                //Do something with failure
                Log.e("getCustomerCnt", t.toString());
            }
        });
    }
}
