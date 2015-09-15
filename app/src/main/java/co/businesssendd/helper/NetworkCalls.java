package co.businesssendd.helper;

import co.businesssendd.gettersandsetters.LoginUser;
import co.businesssendd.gettersandsetters.Order;
import co.businesssendd.gettersandsetters.User_Profile;
import co.businesssendd.gettersandsetters.Users;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

public interface NetworkCalls {

    @POST("/bapi/v1/loginsession/")
    void LoginUser(@Body LoginUser registeruser, Callback<Users> userInfo);

    @GET("/{resource_uri}")
    void getProfile(@Path(value = "resource_uri", encode = false) String resource_uri, Callback<User_Profile> shipmentDetailsCallback);

    @Headers("Authorization:A")
    @POST("/bapi/v3/order/")
    void CreateOrder( @Body Order order, Callback<Order> orderCallback);

}
