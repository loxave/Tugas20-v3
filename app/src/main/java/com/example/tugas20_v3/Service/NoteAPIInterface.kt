package com.example.tugas20_v3.Service

import com.example.tugas20_v3.Model.DefaultResponse
import com.example.tugas20_v3.Model.NoteResponse
import retrofit2.Call
import retrofit2.http.*

interface NoteAPIInterface {
    @GET("notesAPI.php")
    fun getNoteList(
        @Query("function") function: String
    ): Call<NoteResponse>

    @FormUrlEncoded
    @POST("notesAPI.php")
    fun insertNote(
        @Field("id") id: String,
        @Field("judul") judul: String,
        @Field("author") author: String,
        @Field("year") year: String,
        s: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("notesAPI.php")
    fun updateNote(
        @Field("id") id: String,
        @Field("judul") judul: String,
        @Field("author") author: String,
        @Field("year") year: String,
        s: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("notesAPI.php")
    fun deleteNote(
        @Field("id") id: String,
        @Query("function") function: String
    ): Call<DefaultResponse>
}
