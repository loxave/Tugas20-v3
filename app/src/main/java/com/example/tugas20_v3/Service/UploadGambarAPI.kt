package com.example.tugas20_v3.Service

import com.example.tugas20_v3.Model.UploadGambarResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadGambarAPI {

    @Multipart
    @POST("upload_gambar.php?function=upload_gambar")
    fun uploadGambar(
        @Part file: MultipartBody.Part
    ): Call<UploadGambarResponse>
}
