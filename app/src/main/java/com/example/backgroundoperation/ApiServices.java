package com.example.backgroundoperation;

import com.example.backgroundoperation.model.SliderResponse;
import com.example.backgroundoperation.model.imageSave.SaveImageResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiServices {

    @GET(Constant.BaseUrl)
    Call<SliderResponse> getSliders();

    @GET(Constant.BaseUrl)
    Call<SliderResponse> getSliderList();

    @GET(Constant.BaseUrl)
    Call<SliderResponse> getSlidersItemList();


    @Multipart
    @POST("merchant/product/image")
    Call<SaveImageResponse> uploadImage(
            @Header("Authorization") String headerAuth,
            @Part("product_id") String productID,
            @Part MultipartBody.Part file

    );


}
