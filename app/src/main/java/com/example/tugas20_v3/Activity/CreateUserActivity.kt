package com.example.tugas20_v3.Activity

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.tugas20_v3.Model.DefaultResponse
import com.example.tugas20_v3.Model.UploadGambarResponse
import com.example.tugas20_v3.R
import com.example.tugas20_v3.Service.RetrofitClient
import com.example.tugas20_v3.Service.RetrofitImage
import kotlinx.android.synthetic.main.activity_create_user.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CreateUserActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        btn_create_user.setOnClickListener {
            pilihGambar()
        }
        btn_create_user.setOnClickListener {
            if (imageUri == null) {
                insertUser()
            } else {
                insertUserWithImage(imageUri!!)
            }
        }
    }

    private fun pilihGambar() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE_PICKER -> {
                    imageUri = data?.data
                    img_user.setImageURI(imageUri)
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

    private fun getPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, filePathColumn, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "getPathFromURI Exception : ${e.toString()}")
            return ""
        } finally {
            cursor?.close()
        }
    }

    // Function untuk upload gambar ke server dan insert pengguna ke database
    private fun insertUserWithImage(contentURI: Uri) {
        // Upload gambar ke server
        val filePath = getPathFromURI(this, contentURI)
        val file = File(filePath)
        val mFile = RequestBody.create("multipart".toMediaTypeOrNull(), file)
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, mFile)
        RetrofitImage().getService().uploadGambar(
            body
        ).enqueue(object : Callback<UploadGambarResponse> {
            override fun onResponse(
                call: Call<UploadGambarResponse>?,
                response: Response<UploadGambarResponse>?
            ) {
                if (response!!.isSuccessful) {
                    if (response.body()?.status == 1) {
                        // Insert pengguna ke dalam database
                        RetrofitClient.instanceUser.insertUser(
                            "",
                            inputUsername.text.toString().trim(),
                            inputPassword.text.toString().trim(),
                            inputEmail.text.toString().trim(),
                            inputNama.text.toString().trim(),

                            file.name.toString().trim(),
                            "insert_pengguna"
                        ).enqueue(object : Callback<DefaultResponse> {
                            override fun onResponse(
                                call: Call<DefaultResponse>,
                                response: Response<DefaultResponse>
                            ) {
                                if (response!!.isSuccessful) {
                                    if (response.body()?.status == 1) {
                                        inputNama.setText("")

                                        inputEmail.setText("")
                                        inputUsername.setText("")
                                        inputPassword.setText("")
                                        Toast.makeText(
                                            this@CreateUserActivity,
                                            "Berhasil menambah user!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    }
                                } else {
                                    Toast.makeText(
                                        this@CreateUserActivity,
                                        "Gagal menambah user!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                Toast.makeText(
                                    this@CreateUserActivity,
                                    "Tidak ada respon $t",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                        Toast.makeText(
                            this@CreateUserActivity,
                            "Berhasil upload foto!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@CreateUserActivity,
                        "Gagal upload foto!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UploadGambarResponse>, t: Throwable) {
                Toast.makeText(this@CreateUserActivity, "Tidak ada respon $t", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun insertUser() {
        // Insert pengguna ke dalam database
        RetrofitClient.instanceUser.insertUser(
            "",
            inputUsername.text.toString().trim(),
            inputPassword.text.toString().trim(),
            inputEmail.text.toString().trim(),
            inputNama.text.toString().trim(),
            "user.png",
            "insert_pengguna"
        ).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response!!.isSuccessful) {
                    if (response.body()?.status == 1) {
                        inputNama.setText("")
                        inputEmail.setText("")
                        inputUsername.setText("")
                        inputPassword.setText("")
                        Toast.makeText(
                            this@CreateUserActivity,
                            "Berhasil menambah user!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this@CreateUserActivity,
                        "Gagal menambah user!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@CreateUserActivity, "Tidak ada respon $t", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}