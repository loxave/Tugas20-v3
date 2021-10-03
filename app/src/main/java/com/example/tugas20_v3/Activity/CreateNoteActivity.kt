package com.example.tugas20_v3.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tugas20_v3.Model.DefaultResponse
import com.example.tugas20_v3.R
import com.example.tugas20_v3.Service.RetrofitClient
import kotlinx.android.synthetic.main.activity_create_note.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        btn_create.setOnClickListener {
            RetrofitClient.instance.insertNote(
                "",
                edt_judul.text.toString().trim(),
                edt_author.text.toString().trim(),
                edt_year.text.toString().trim(),
                "insert_note"
            ).enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()?.status == 1) {
                            edt_judul.setText("")
                            edt_author.setText("")
                            edt_year.setText("")
                            Toast.makeText(
                                this@CreateNoteActivity,
                                "Note Has been created!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    } else {
                        Toast.makeText(
                            this@CreateNoteActivity,
                            "Failed to create Note",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(
                        this@CreateNoteActivity,
                        "No response $t",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}