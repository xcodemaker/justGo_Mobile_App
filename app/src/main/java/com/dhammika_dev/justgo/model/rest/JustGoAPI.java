package com.dhammika_dev.justgo.model.rest;

import com.dhammika_dev.justgo.model.entities.request.CreateTicketRequest;
import com.dhammika_dev.justgo.model.entities.request.EmailValidateRequest;
import com.dhammika_dev.justgo.model.entities.request.LoginRequest;
import com.dhammika_dev.justgo.model.entities.request.PasswordResetRequest;
import com.dhammika_dev.justgo.model.entities.request.RequestTokenRequest;
import com.dhammika_dev.justgo.model.entities.request.UpdateUserDetailsRequest;
import com.dhammika_dev.justgo.model.entities.request.ValidateTicketRequest;
import com.dhammika_dev.justgo.model.entities.response.CreateTicketResponse;
import com.dhammika_dev.justgo.model.entities.response.EmailValidateResponse;
import com.dhammika_dev.justgo.model.entities.response.LoginResponse;
import com.dhammika_dev.justgo.model.entities.response.LogoutResponse;
import com.dhammika_dev.justgo.model.entities.response.PasswordResetResponse;
import com.dhammika_dev.justgo.model.entities.response.RequestTokenResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketListResponse;
import com.dhammika_dev.justgo.model.entities.response.TokenVerifyResponse;
import com.dhammika_dev.justgo.model.entities.response.UserDetailsResponse;
import com.dhammika_dev.justgo.model.entities.response.ValidateTicketResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;


public interface JustGoAPI {


    @POST("/api/login")
    Observable<LoginResponse> doLoginAPI(
            @Header("Content-Type") String contentType,
//            @Header("X-Requested-With") String xRequestedWith,
            @Body LoginRequest loginRequest);

    @Multipart
    @POST("/api/register")
    Observable<LoginResponse> doRegister(
            @Part MultipartBody.Part profile_pic,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("email") RequestBody email,
            @Part("address") RequestBody address,
            @Part("nic_or_passport") RequestBody nic_or_passport,
            @Part("contact_number") RequestBody contact_number,
            @Part("user_type") RequestBody user_type,
            @Part("password") RequestBody password,
            @Part("password_confirmation") RequestBody password_confirmation
    );


    @GET("/api/logout")
    Observable<LogoutResponse> doLogoutAPI(
            @Header("Authorization") String access_token);

    @GET("/api/user")
    Observable<UserDetailsResponse> getUserDetails(
            @Header("Authorization") String access_token);

    @POST("/api/validate/email")
    Observable<EmailValidateResponse> doEmailValidate(
            @Body EmailValidateRequest emailValidateRequest);

    @POST("/api/password/create")
    Observable<RequestTokenResponse> requestResetToken(
            @Header("Content-Type") String contentType,
            @Header("X-Requested-With") String xRequestedWith,
            @Body RequestTokenRequest requestTokenRequest);

    @GET("/api/password/find/{token}")
    Observable<TokenVerifyResponse> verifyToken(@Path("token") String token);

    @POST("/api/password/reset")
    Observable<PasswordResetResponse> passwordReset(
            @Body PasswordResetRequest requestTokenRequest);

    @POST("/api/ticket/create")
    Observable<CreateTicketResponse> createTicket(
            @Header("Content-Type") String contentType,
            @Header("Authorization") String token,
            @Body CreateTicketRequest createTicketRequest
    );

    @Multipart
    @POST("/api/user/update/image")
    Observable<UserDetailsResponse> updateUserProfileImage(
            @Header("Authorization") String authorization,
            @Part MultipartBody.Part profile_pic
    );

    @POST("/api/user/update/details")
    Observable<UserDetailsResponse> updateUserDetails(
            @Header("Content-Type") String contentType,
            @Header("Authorization") String token,
            @Body UpdateUserDetailsRequest updateUserDetailsRequest
    );

    @POST("/api/ticket/get")
    Observable<TicketListResponse> getMyTickets(
            @Header("Content-Type") String contentType,
            @Header("Authorization") String token
    );

    @POST("/api/ticket/validate")
    Observable<ValidateTicketResponse> validateTicket(
            @Header("Content-Type") String contentType,
            @Header("Authorization") String token,
            @Body ValidateTicketRequest validateTicketRequest
    );

}
