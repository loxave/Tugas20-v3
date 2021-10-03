package com.example.tugas20_v3.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas20_v3.Adapter.NoteAdapter
import com.example.tugas20_v3.Model.DataItem
import com.example.tugas20_v3.Model.DefaultResponse
import com.example.tugas20_v3.Model.NoteResponse
import com.example.tugas20_v3.R
import com.example.tugas20_v3.Service.RetrofitClient
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val api by lazy { RetrofitClient.instance }
    val noteAdapter = NoteAdapter(arrayListOf(), object : NoteAdapter.OnAdapterListener {

        override fun onDeleteNote(id: String) {
            api.deleteNote(id, "delete_note")
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                val toast = Toast.makeText(
                                    this@MainActivity,
                                    "Note has been deleted!",
                                    Toast.LENGTH_SHORT
                                )
                                toast.show()
                                getNoteData()
                            }
                        } else {
                            val toast = Toast.makeText(
                                this@MainActivity,
                                "Failed to delete note",
                                Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    }

                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        val toast = Toast.makeText(
                            this@MainActivity,
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
        setContentView(R.layout.activity_main)
        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.adapter = noteAdapter
        onStart()
        btn_add.setOnClickListener {
            val intent = Intent(this, CreateNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        getNoteData()
    }

    private fun getNoteData() {
        api.getNoteList("get_note").enqueue(object : Callback<NoteResponse> {
            override fun onResponse(call: Call<NoteResponse>?, response: Response<NoteResponse>?) {
                if (response!!.isSuccessful) {
                    response.body()?.let { tampilkanNote(it) }
                    Toast.makeText(this@MainActivity, "Daftar Catatan", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Gagal mendapatkan daftar catatan",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<NoteResponse>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "Tidak ada respon $t", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun tampilkanNote(dataNote: NoteResponse) {
        val result = dataNote.data
        noteAdapter.setData(result as List<DataItem>)
    }

}