package com.app.industrialwatch.app.network;


import com.app.industrialwatch.app.business.MyJSONObject;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @GET
    Call<ResponseBody> doGetRequest(@Url String url, @QueryMap Map<String, String> queryParams);

    @Headers("Content-Type: application/json")
    @GET
    Call<ResponseBody> doGetRequest(@Url String url);

    @Headers("Content-Type: application/json")
    @POST
    Call<ResponseBody> doPostRequest(@Url String url, @Body RequestBody object);

    @Headers("Content-Type: application/json")
    @PUT
    Call<ResponseBody> doPutRequest(@Url String url, @Body RequestBody object);

    @Headers("Content-Type: application/json")
    @POST
    Call<ResponseBody> doPostRequest(@Url String url, @QueryMap Map<String, Object> queryParams);

    //@Headers("Content-Type: application/json")
    @Multipart
    @POST
    Call<ResponseBody> doPostRequestWithMapBody(@Url String url
            , @PartMap Map<String, RequestBody> partBody
            , @Part List<MultipartBody.Part> imagePart);

    @GET
    Call<ResponseBody> downloadFile(@Url String url, @QueryMap Map<String, String> queryParams);
}
