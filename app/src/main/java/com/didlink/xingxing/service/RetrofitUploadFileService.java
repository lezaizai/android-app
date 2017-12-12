package com.didlink.xingxing.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public class RetrofitUploadFileService {
    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTH_TOKEN_PREFIX = "Bearer ";

    public static void main(String []args) throws Exception {

        // set file upload parameters
        String httpURL = "http://localhost:5946/rest/api/fileservice/upload/text";
        File filePath = new File("C:/test/download/Sample.txt");
        String filename = "MySample.txt";

        // invoke file upload service using above parameters
        String responseString = testUploadService(httpURL, "", filePath, filename);
        System.out.println("responseString : " + responseString);
    }

     public interface FileUploadService {
        /**
         * @param description
         * @param imgs
         * @return
         */
        @Multipart
        @POST("/rest/api/fileservice/upload/text")
        Call<String> uploadImage(@Part("fileName") String description,
                                 @Header(AUTH_HEADER_NAME) String contentRange,
                                 @Part("uploadedFile\"; filename=\"Myfolder\\new\\image116.png\"") RequestBody imgs);


         /**
         * @param description
         * @param imgs1
         * @param imgs2
         * @param imgs3
         * @param imgs4
         * @param imgs5
         * @param imgs6
         * @return
         */
        @Multipart
        @POST("/rest/api/fileservice/upload/text")
        Call<String> uploadImage(@Part("description") String description,
                                 @Header("X-AUTH-TOKEN") String contentRange,
                                 @Part("file\"; filename=\"image.png\"") RequestBody imgs1,
                                 @Part("file\"; filename=\"image.png\"") RequestBody imgs2,
                                 @Part("file\"; filename=\"image.png\"") RequestBody imgs3,
                                 @Part("file\"; filename=\"image.png\"") RequestBody imgs4,
                                 @Part("file\"; filename=\"image.png\"") RequestBody imgs5,
                                 @Part("file\"; filename=\"image.png\"") RequestBody imgs6);

        /**
         * @param description
         * @param imgs1
         * @return
         */
        @Multipart
        @POST("/rest/api/fileservice/upload/text")
        Call<String> uploadImage(@Part("description") String description,
                                 @Header("X-AUTH-TOKEN") String contentRange,
                                 @PartMap Map<String, RequestBody> imgs1);
    }

    public static FileUploadService apiManager = null;

    public static void upload1(String baseurl, String paths,String token, String desp){

        final class GzipRequestInterceptor implements Interceptor {
            @Override public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
                    return chain.proceed(originalRequest);
                }

                Request compressedRequest = originalRequest.newBuilder()
                        .header("Content-Encoding", "gzip")
                        .method(originalRequest.method(), gzip(originalRequest.body()))
                        .build();
                return chain.proceed(compressedRequest);
            }

            private RequestBody gzip(final RequestBody body) {
                return new RequestBody() {
                    @Override public MediaType contentType() {
                        return body.contentType();
                    }

                    @Override public long contentLength() {
                        return -1; // We don't know the compressed length in advance!
                    }

                    @Override public void writeTo(BufferedSink sink) throws IOException {
                        BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                        body.writeTo(gzipSink);
                        gzipSink.close();
                    }
                };
            }
        }

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor( new GzipRequestInterceptor());
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

        apiManager = sRetrofit.create(FileUploadService.class);


        RequestBody requestBody= RequestBody.create(MediaType.parse("multipart/form-data"), new File(paths));

        Call<String> call = apiManager.uploadImage( desp, AUTH_TOKEN_PREFIX + token, requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                System.err.println("onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.err.println("onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
            }
        });

    }


    /**
     * @param paths
     * @param desp
     */
    public static void upload(ArrayList<String> paths,String token, String desp){
        RequestBody[] requestBody= new RequestBody[6];
        if (paths.size()>0) {
            for (int i=0;i<paths.size();i++) {
                requestBody[i] =
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(paths.get(i)));
            }
        }
        Call<String> call = apiManager.uploadImage( desp,token, requestBody[0],requestBody[1],requestBody[2],requestBody[3],requestBody[4],requestBody[5]);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                System.err.println("onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.err.println("onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
            }
        });

    }

    /**
     *
     * @param paths
     * @param desp
     */
    public static void uploadMany(ArrayList<String> paths, String token, String desp){
        Map<String,RequestBody> photos = new HashMap<>();
        if (paths.size()>0) {
            for (int i=0;i<paths.size();i++) {
                photos.put("photos"+i+"\"; filename=\"icon.png",  RequestBody.create(MediaType.parse("multipart/form-data"), new File(paths.get(i))));
            }
        }
        Call<String> stringCall = apiManager.uploadImage(desp, token, photos);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                System.err.println("onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.err.println("onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
            }
        });
    }

    public static String testUploadService(String httpURL, String token, File filePath, String filename) throws Exception {
        String responseString = null;

        return responseString;
    }

}