package com.example.applicationcontentprovider

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns._ID
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationcontentprovider.database.NotesDatabaseHelper.Companion.TITLE_NOTES
import com.example.applicationcontentprovider.database.NotesProvider.Companion.URI_NOTES
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    lateinit var noteRecycler: RecyclerView
    lateinit var noteAddButton: FloatingActionButton
    lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        noteAddButton = findViewById(R.id.note_add)
        noteAddButton.setOnClickListener(){
            NoteDetailFragment().show(supportFragmentManager, "dialog")
        }

        notesAdapter = NotesAdapter(object : NoteClickedListener {
            override fun notesClickedItem(cursor: Cursor) {
                val id: Long = cursor.getLong(cursor.getColumnIndex(_ID))
                val fragment: NoteDetailFragment = NoteDetailFragment.newIntance(id)
                fragment.show(supportFragmentManager, "dialog")
            }

            override fun notesRemoveItem(cursor: Cursor?) {
                val id: Long? = cursor?.getLong(cursor.getColumnIndex(_ID))
                contentResolver.delete(Uri.withAppendedPath(URI_NOTES, id.toString()), null, null)
            }

        })
        notesAdapter.setHasStableIds(true)
        noteRecycler = findViewById(R.id.recycler_notes)
        noteRecycler.layoutManager = LinearLayoutManager(this)
        noteRecycler.adapter = notesAdapter

        LoaderManager.getInstance(this).initLoader(0, null, this)

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> =
        CursorLoader(this, URI_NOTES, null, null, null, TITLE_NOTES)

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if(data != null){ notesAdapter.setCursor(data) }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        notesAdapter.setCursor(null)
    }
}