package com.example.tugas20_v3.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tugas20_v3.Model.DefaultResponse
import com.example.tugas20_v3.R
import com.example.tugas20_v3.Service.RetrofitClient
import kotlinx.android.synthetic.main.activity_create_note.*
import kotlinx.android.synthetic.main.activity_edit_note.*
import kotlinx.android.synthetic.main.activity_edit_note.edt_author
import kotlinx.android.synthetic.main.activity_edit_note.edt_year
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditNoteActivity : AppCompatActivity() {
    private val api by lazy { RetrofitClient.instance }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        edt_title_note.setText(intent.getStringExtra("judul"))
        edt_author.setText(intent.getStringExtra("Author"))
        edt_year.setText(intent.getStringExtra("Year"))
        btn_edit.setOnClickListener {
            api.updateNote(
                "",
                edt_title_note.text.toString(),
                edt_author.text.toString(),
                edt_year.text.toString(),
                "update_note"
            ).enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response!!.isSuccessful){
                        if (response.body()?.status == 1){
                            val toast = Toast.makeText(this@EditNoteActivity, "Note has been edited!", Toast.LENGTH_SHORT)
                            toast.show()
                            finish()
                        }
                    } else {
                        val toast = Toast.makeText(this@EditNoteActivity, "Failed to edit notes", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    val toast = Toast.makeText(this@EditNoteActivity, "Can't response $t", Toast.LENGTH_SHORT)
                    toast.show()
                }
            })
        }
    }
}