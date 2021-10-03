package com.example.tugas20_v3.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas20_v3.Adapter.UserAdapter
import com.example.tugas20_v3.Model.DefaultResponse
import com.example.tugas20_v3.Model.UserItem
import com.example.tugas20_v3.Model.UserResponse
import com.example.tugas20_v3.R
import com.example.tugas20_v3.Service.RetrofitClient
import kotlinx.android.synthetic.main.activity_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : AppCompatActivity() {

    private val api by lazy { RetrofitClient.instanceUser }

    //  val userAdapter = UserAdapter(arrayListOf(), object : UserAdapter.OnAdapterListener {
    val userAdapter = UserAdapter(arrayListOf(), object : UserAdapter.OnAdapterListener {
        override fun onDeleteUser(id: String) {
            api.deleteUser(id, "delete_user")
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                val toast = Toast.makeText(
                                    this@UserActivity,
                                    "Delete success!",
                                    Toast.LENGTH_SHORT
                                )
                                toast.show()
                                getUserData()
                            }
                        } else {
                            val toast = Toast.makeText(
                                this@UserActivity,
                                "Failed to delete user",
                                Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    }

                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        val toast = Toast.makeText(
                            this@UserActivity,
                            "Can't response $t",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                })
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user)
        rv_recyclerUser.layoutManager = LinearLayoutManager(this)
        rv_recyclerUser.adapter = userAdapter
        onStart()
        btn_create_user.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        getUserData()
    }

    private fun getUserData() {
        api.getUserList("get_user").enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>?, response: Response<UserResponse>?) {
                if (response!!.isSuccessful) {
                    response.body()?.let { tampilUser(it) }
                    Toast.makeText(this@UserActivity, "Daftar User", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        this@UserActivity,
                        "Gagal mendapatkan daftar user",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>?, t: Throwable?) {
                Toast.makeText(this@UserActivity, "Tidak ada respon $t", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun tampilUser(dataUser: UserResponse) {
        val result = dataUser.data
        userAdapter.setData(result as List<UserItem>)
    }
}