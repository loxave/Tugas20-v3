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
import com.bumptech.glide.Glide
import com.example.tugas20_v3.Model.DefaultResponse
import com.example.tugas20_v3.Model.UploadGambarResponse
import com.example.tugas20_v3.R
import com.example.tugas20_v3.Service.RetrofitClient
import com.example.tugas20_v3.Service.RetrofitImage
import kotlinx.android.synthetic.main.activity_edit_user.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditUserActivity : AppCompatActivity() {
    private var imageUri: Uri? = null
    private val api by lazy { RetrofitClient.instanceUser }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)
        val id = intent.getStringExtra("id")
        val namaGambar = intent.getStringExtra("namaGambar")
        inputEditNama.setText(intent.getStringExtra("nama"))
        inputEditAlamat.setText(intent.getStringExtra("alamat"))
        inputEditEmail.setText(intent.getStringExtra("email"))
        inputEditUsername.setText(intent.getStringExtra("username"))
        inputEditPassword.setText(intent.getStringExtra("password"))
        Glide.with(this).load(intent.getStringExtra("pathGambar")).into(img_edit_user)
        btnEditGambar.setOnClickListener {
            pilihGambar()
        }
        btnEditUser.setOnClickListener {
            if (imageUri == null ){
                editPengguna(id!!,namaGambar!!)
            } else {
                editUserWithImage(id!!,imageUri!!,namaGambar!!)
            }
        }
    }

    private fun pilihGambar(){
        Intent(Intent.ACTION_PICK).also{
            it.type = "image/*"
            startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_IMAGE_PICKER ->{
                    imageUri = data?.data
                    img_edit_user.setImageURI(imageUri)
                }
            }
        }
    }

    companion object{
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
            Log.e(ContentValues.TAG, "getPathFromURI Exception : $e")
            return ""
        } finally {
            cursor?.close()
        }
    }

    private fun editPengguna(idUser: String, namaGambar: String){
        // Update pengguna ke dalam database
        api.updateUser(
            idUser,
            inputEditUsername.text.toString(),
            inputEditPassword.text.toString(),
            inputEditEmail.text.toString(),
            inputEditNama.text.toString(),
            inputEditAlamat.text.toString(),
            namaGambar,
            "update_pengguna"
        ).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response!!.isSuccessful){
                    if (response.body()?.status == 1){
                        Toast.makeText(this@EditUserActivity, "Berhasil edit pengguna!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@EditUserActivity, "Gagal edit pengguna", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@EditUserActivity, "Tidak ada respon $t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editUserWithImage(idUser: String, contentURI: Uri, namaGambar: String) {
        // Upload gambar ke server
        val filePath = getPathFromURI(this, contentURI)
        val file = File(filePath)
        val mFile = RequestBody.create("multipart".toMediaTypeOrNull(), file)
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, mFile)
        RetrofitImage().getService().uploadGambar(
            body
        ).enqueue(object: Callback<UploadGambarResponse> {
            override fun onResponse(
                call: Call<UploadGambarResponse>?,
                response: Response<UploadGambarResponse>?
            ) {
                if (response!!.isSuccessful){
                    if (response.body()?.status == 1){

                        api.updateUser(
                            idUser,
                            inputEditUsername.text.toString(),
                            inputEditPassword.text.toString(),
                            inputEditEmail.text.toString(),
                            inputEditNama.text.toString(),
                            inputEditAlamat.text.toString(),
                            file.name.toString(),
                            "update_user"
                        ).enqueue(object : Callback<DefaultResponse> {
                            override fun onResponse(
                                call: Call<DefaultResponse>,
                                response: Response<DefaultResponse>
                            ) {
                                if (response!!.isSuccessful){
                                    if (response.body()?.status == 1){
                                        Toast.makeText(this@EditUserActivity, "Berhasil edit pengguna!", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                } else {
                                    Toast.makeText(this@EditUserActivity, "Gagal edit pengguna", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                Toast.makeText(this@EditUserActivity, "Tidak ada respon $t", Toast.LENGTH_SHORT).show()
                            }
                        })
                        Toast.makeText(this@EditUserActivity, "Berhasil upload foto!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@EditUserActivity, "Gagal upload foto!", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UploadGambarResponse>, t: Throwable) {
                Toast.makeText(this@EditUserActivity, "Tidak ada respon $t", Toast.LENGTH_SHORT).show()
            }
        })
    }
}