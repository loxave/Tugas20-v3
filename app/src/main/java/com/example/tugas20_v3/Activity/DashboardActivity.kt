package com.example.tugas20_v3.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tugas20_v3.R
import kotlinx.android.synthetic.main.activity_dashboard.*


class DashboardActivity : AppCompatActivity() {

    companion object {

        val EXTRA_LOGIN = "LOGIN"
        val EXTRA_USERNAME = "key.username"
        val EXTRA_NAMA = "key.nama"
        val EXTRA_EMAIL = "key.email"
        val EXTRA_GAMBAR = "key.gambar"
        val KEY_LOGIN = "key.login"
        val IMAGE_BASE = "http://192.168.43.60/crud/"
    }


    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        sharedPreferences = getSharedPreferences(EXTRA_NAMA, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(EXTRA_USERNAME, "")
        val nama = sharedPreferences.getString(EXTRA_NAMA, "")
        val email = sharedPreferences.getString(EXTRA_EMAIL, "")
        val gambar = sharedPreferences.getString(EXTRA_GAMBAR, "")
        val pathGambar = IMAGE_BASE + gambar
        tv_nama_user.setText("Hi, ${nama}")
        tv_username.setText(username)
        tv_email.setText(email)
        Toast.makeText(this, "Berhasil Login!", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "Halaman Dashboard", Toast.LENGTH_SHORT).show()
        Glide.with(this).load(pathGambar).into(img_user)
        tv_logout.setOnClickListener {
            logout()
        }
        btn_daftar_catatan.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        daftarUser.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logout() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}