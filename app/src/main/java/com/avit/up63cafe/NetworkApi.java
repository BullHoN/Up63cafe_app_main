package com.avit.up63cafe;

import com.avit.up63cafe.auth.LoginPostData;
import com.avit.up63cafe.auth.LoginResponseData;
import com.avit.up63cafe.auth.OptResponseData;
import com.avit.up63cafe.auth.OtpPostData;
import com.avit.up63cafe.auth.RegisterPostData;
import com.avit.up63cafe.auth.RegisterResponseData;
import com.avit.up63cafe.auth.ResendOtpResponseData;
import com.avit.up63cafe.forgetPassword.ForgetPasswordPostData;
import com.avit.up63cafe.forgetPassword.ForgetResponseData;
import com.avit.up63cafe.notification.NotificationPostData;
import com.avit.up63cafe.notification.NotificationResponseData;
import com.avit.up63cafe.payment.PaymentOrderPostData;
import com.avit.up63cafe.payment.PaymentOrderResponseData;
import com.avit.up63cafe.ui.categories.CategoriesItem;
import com.avit.up63cafe.ui.category.MenuItem;
import com.avit.up63cafe.ui.dashboard.CheckoutDetailsReponseData;
import com.avit.up63cafe.ui.home.SpecialsItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkApi {

//    String BASE_URL = "http://192.168.225.20:5000/";
    String BASE_URL = "https://www.up63cafe.com/";

    @GET("specials")
    Call<ArrayList<SpecialsItem>>  getSpecialItems();

    @GET("categories")
    Call<ArrayList<CategoriesItem>> getCategories();

    @GET("category/{id}")
    Call<ArrayList<MenuItem>> getMenuItemsOfCategory(@Path(value = "id") String categoryName);

    @POST("login")
    Call<LoginResponseData> loginUser(@Body LoginPostData data);

    @POST("register")
    Call<RegisterResponseData> registerUser(@Body RegisterPostData data);

    @POST("otp")
    Call<OptResponseData> validateOtp(@Body OtpPostData data);

    @POST("createOrder")
    Call<PaymentOrderResponseData> generatePaymentOrderId(@Body PaymentOrderPostData data);

    @POST("notification")
    Call<NotificationResponseData> sendNotification(@Body NotificationPostData data);

    @GET("otp/resend")
    Call<ResendOtpResponseData> resendOtp(@Query("email") String email);

    @GET("checkoutDetails")
    Call<CheckoutDetailsReponseData> getCheckoutDetails();

    @POST("user/forget")
    Call<ForgetResponseData> sendForgetPasswordRequest(@Body ForgetPasswordPostData data);

}
