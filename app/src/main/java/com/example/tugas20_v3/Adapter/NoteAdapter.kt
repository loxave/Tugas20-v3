package com.example.tugas20_v3.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas20_v3.Activity.EditNoteActivity
import com.example.tugas20_v3.Model.DataItem
import com.example.tugas20_v3.R

class NoteAdapter(
    val listNote: ArrayList<DataItem>,
    val listener: OnAdapterListener

) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    fun setData(data: List<DataItem>) {
        listNote.clear()
        listNote.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvJudulNote: TextView = itemView.findViewById(R.id.tv_judul_note)
        var tvAuthor: TextView = itemView.findViewById(R.id.tv_author)
        var tvYear: TextView = itemView.findViewById(R.id.tv_year)
        var cvNote: CardView = itemView.findViewById(R.id.cv_note)
        var btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_note_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listNote[position]
        holder.tvJudulNote.text = data.judul.toString()
        holder.tvAuthor.text = data.author.toString()
        holder.tvYear.text = data.year.toString()

        holder.cvNote.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditNoteActivity::class.java)
            intent.putExtra("id", data.id.toString())
            intent.putExtra("judul", data.judul.toString())
            intent.putExtra("Author", data.author.toString())
            intent.putExtra("tahun", data.year.toString())
            holder.itemView.context.startActivity(intent)
        }
        holder.btnDelete.setOnClickListener {
            listener.onDeleteNote(data.id.toString())
        }
    }

    override fun getItemCount(): Int = listNote.size

    interface OnAdapterListener {
        fun onDeleteNote(id: String)
    }
}