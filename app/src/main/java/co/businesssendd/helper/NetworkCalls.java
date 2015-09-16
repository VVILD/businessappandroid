package co.businesssendd.helper;

import java.util.ArrayList;

import co.businesssendd.gettersandsetters.Billing;
import co.businesssendd.gettersandsetters.BillingResponse;
import co.businesssendd.gettersandsetters.LoginUser;
import co.businesssendd.gettersandsetters.Order;
import co.businesssendd.gettersandsetters.User_Profile;
import co.businesssendd.gettersandsetters.Users;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface NetworkCalls {

    @POST("/bapi/v1/loginsession/")
    void LoginUser(@Body LoginUser registeruser, Callback<Users> userInfo);

    @GET("/{resource_uri}")
    void getProfile(@Path(value = "resource_uri", encode = false) String resource_uri, Callback<User_Profile> shipmentDetailsCallback);

    @Headers("Authorization:A")
    @POST("/bapi/v3/order/")
    void CreateOrder( @Body Order order, Callback<Order> orderCallback);

    @GET("/bapi/v2/invoice/")
    void getBilling(@Query("b_username") String b_username,@Query("start_date") String start_date,@Query("end_date") String end_date, Callback<Response> billingCallback);

}
