package com.example.tugas20_v3.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteResponse(

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
) : Parcelable

@Parcelize
data class DataItem(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("judul")
    val judul: String? = null,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("year")
    val year: String? = null


) : Parcelable


