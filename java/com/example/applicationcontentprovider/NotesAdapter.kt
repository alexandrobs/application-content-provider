package com.example.applicationcontentprovider

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationcontentprovider.database.NotesDatabaseHelper.Companion.DESCRIPTION_NOTES
import com.example.applicationcontentprovider.database.NotesDatabaseHelper.Companion.TABLE_NOTES
import com.example.applicationcontentprovider.database.NotesDatabaseHelper.Companion.TITLE_NOTES

class NotesAdapter(private val listener: NoteClickedListener): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private var mCursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false))

    override fun getItemCount(): Int =
        if (mCursor != null) mCursor?.count as Int else 0

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        mCursor?.moveToPosition(position)

        holder.noteTitle.text = mCursor?.getString(mCursor?.getColumnIndex(TITLE_NOTES) as Int)
        holder.noteDescription.text = mCursor?.getString(mCursor?.getColumnIndex(DESCRIPTION_NOTES) as Int)
        holder.noteButtonRemover.setOnClickListener {
            mCursor?.moveToPosition(position)
            listener.notesRemoveItem(mCursor)
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener { listener.notesClickedItem(mCursor as Cursor) }
    }

    fun setCursor(newCursor: Cursor?){
        mCursor = newCursor
        notifyDataSetChanged()
    }

    class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val noteTitle = itemView.findViewById(R.id.note_title) as TextView
        val noteDescription = itemView.findViewById(R.id.note_description) as TextView
        val noteButtonRemover = itemView.findViewById(R.id.button_remove) as Button
    }
}