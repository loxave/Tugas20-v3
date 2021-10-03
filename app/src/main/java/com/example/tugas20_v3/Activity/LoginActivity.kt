package com.example.tugas20_v3.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import com.example.tugas20_v3.Model.UserResponse
import com.example.tugas20_v3.R
import com.example.tugas20_v3.Service.RetrofitClient
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private val api by lazy { RetrofitClient.instanceUser }
    var checkLogin = false

    companion object {
        val EXTRA_USERNAME = "key.username"
        val KEY_NAMA = "key.nama"
        val KEY_EMAIL = "key.email"
        val KEY_GAMBAR = "key.gambar"
        val KEY_LOGIN = "key.login"
        val PERFS_NAME = "LOGIN"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreferences = getSharedPreferences(PERFS_NAME, Context.MODE_PRIVATE)
        checkLogin = sharedPreferences.getBoolean(KEY_LOGIN, false)
        if (checkLogin) {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        btn_signin.setOnClickListener {
            login()
        }
        register_button.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }
    }

    fun login() {
        pesan()
    }


    private fun pesan() {
        val editor = sharedPreferences.edit()
        val username: String = input_username.text.toString()
        val password: String = input_password.text.toString()
        when {
            username == "" -> {
                val pesan =
                    Toast.makeText(applicationContext, "Masukkan username", Toast.LENGTH_LONG)
                pesan.setGravity(Gravity.TOP, 0, 140)
                pesan.show()
            }
            password == "" -> {
                val pesan =
                    Toast.makeText(applicationContext, "Masukkan password", Toast.LENGTH_LONG)
                pesan.setGravity(Gravity.TOP, 0, 140)
                pesan.show()
            }
            else -> {
                api.loginUser(username, password).enqueue(object : Callback<UserResponse> {
                    override fun onResponse(
                        call: Call<UserResponse>?,
                        response: Response<UserResponse>?
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()?.status == 1) {
                                val pesan = Toast.makeText(
                                    applicationContext,
                                    "Login Success!",
                                    Toast.LENGTH_LONG
                                )
                                pesan.setGravity(Gravity.TOP, 0, 140)
                                pesan.show()
                                editor.putString(
                                    EXTRA_USERNAME,
                                    response.body()!!.data!![0]?.username
                                )
                                editor.putString(
                                    KEY_NAMA,
                                    response.body()!!.data!![0]?.nama
                                )
                                editor.putString(
                                    KEY_EMAIL,
                                    response.body()!!.data!![0]?.email
                                )
                                editor.putString(
                                    KEY_GAMBAR,
                                    response.body()!!.data!![0]?.gambar
                                )
                                editor.putBoolean(KEY_LOGIN, true)
                                editor.apply()
                                val intent =
                                    Intent(this@LoginActivity, DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else if (response.body()?.status == 0) {
                                val pesan = Toast.makeText(
                                    applicationContext,
                                    response.body()?.message,
                                    Toast.LENGTH_LONG
                                )
                                pesan.setGravity(Gravity.TOP, 0, 140)
                                pesan.show()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Response failed", Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>?, t: Throwable?) {
                        Toast.makeText(this@LoginActivity, "Can't be response $t", Toast.LENGTH_LONG)
                            .show()
                    }
                })
            }
        }
    }
}