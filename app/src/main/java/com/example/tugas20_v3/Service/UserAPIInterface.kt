package com.example.tugas20_v3.Service


import com.example.tugas20_v3.Model.DefaultResponse
import com.example.tugas20_v3.Model.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserAPIInterface {
    @GET("userAPI.php")
    fun getUserList(
        @Query("function") function: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("penggunaApi.php")
    fun insertUser(
        @Field("id") id: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("nama") nama: String,
        @Field("gambar") gambar: String,
        @Query("function") function: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("userAPI.php")
    fun updateUser(
        @Field("id") id: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("nama") nama: String,
        @Field("gambar") gambar: String,
        @Query("function") function: String,
        s: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("userAPI.php?function=login_pengguna")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("userAPI.php")
    fun deleteUser(
        @Field("id") id: String,
        @Query("function") function: String
    ): Call<DefaultResponse>

}