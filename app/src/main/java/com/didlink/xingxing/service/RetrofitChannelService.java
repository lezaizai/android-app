package com.didlink.xingxing.service;

import com.didlink.xingxing.models.Channel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class RetrofitChannelService {
    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTH_TOKEN_PREFIX = "Bearer ";

    private ChannelListener channelListener;

    public static void main(String []args) throws Exception {

    }

     public interface ChannelService {
        /**
         * @param
         * @return
         */
        @POST("/rest/api/channel/add")
        Call<Channel> addChannel(@Header(AUTH_HEADER_NAME) String token,
                                 @Body Channel body);

         @GET("/rest/api/channel/getSimpleList")
         Call<List<Channel>> getSimpleList(@Header(AUTH_HEADER_NAME) String token,
                                  @Query("name") String name);

    }

    public interface ChannelListener {
         public void OnChannelAdded(boolean result, Channel channel);
         public void OnSimpleList(boolean result, List<Channel> channels);
    }

    public ChannelService apiManager = null;

    public void newChannel(String baseurl,String token, Channel channel){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(new Interceptor() {
//                                      @Override
//                                      public Response intercept(Interceptor.Chain chain) throws IOException {
//                                          Request original = chain.request();
//
//                                          Request request = original.newBuilder()
//                                                  .header("X-AUTH-TOKEN", "dsdsds")
//                                                  .build();
//
//                                          return chain.proceed(request);
//                                      }
//                                  });

                OkHttpClient client = httpClient.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit sRetrofit = new Retrofit .Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        apiManager = sRetrofit.create(ChannelService.class);



        Call<Channel> call = apiManager.addChannel(AUTH_TOKEN_PREFIX + token, channel);
        call.enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, retrofit2.Response<Channel> response) {
                System.err.println("onResponse() called with: " + "call = [" + call + "], response = [" + response.body() + "]");
                if (channelListener != null) {
                    channelListener.OnChannelAdded(true, response.body());
                }
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                System.err.println("onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
                if (channelListener != null) {
                    channelListener.OnChannelAdded(false, null);
                }

            }
        });

    }

    public void getSimpleList(String baseurl,String token, String name){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit sRetrofit = new Retrofit .Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        apiManager = sRetrofit.create(ChannelService.class);


        Call<List<Channel>> call = apiManager.getSimpleList(AUTH_TOKEN_PREFIX + token, name);
        call.enqueue(new Callback<List<Channel>>() {
            @Override
            public void onResponse(Call<List<Channel>> call, retrofit2.Response<List<Channel>> response) {
                System.err.println("onResponse() called with: " + "call = [" + call + "], response = [" + response.body().size() + "]");
                if (channelListener != null) {
                    channelListener.OnSimpleList(true, response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Channel>> call, Throwable t) {
                System.err.println("onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
                if (channelListener != null) {
                    channelListener.OnChannelAdded(false, null);
                }

            }
        });

    }

    public void setChannelListener(ChannelListener listener) {
        this.channelListener = listener;
    }

}