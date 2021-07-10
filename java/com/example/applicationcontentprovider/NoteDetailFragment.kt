package com.example.applicationcontentprovider

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.content.contentValuesOf
import androidx.fragment.app.DialogFragment
import com.example.applicationcontentprovider.database.NotesDatabaseHelper.Companion.DESCRIPTION_NOTES
import com.example.applicationcontentprovider.database.NotesDatabaseHelper.Companion.TITLE_NOTES
import com.example.applicationcontentprovider.database.NotesProvider.Companion.URI_NOTES

class NoteDetailFragment : DialogFragment(), DialogInterface.OnClickListener {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private var id: Long = 0

    companion object {
        private const val EXTRA_ID = "id"
        fun newIntance(id: Long): NoteDetailFragment{
            val bundle = Bundle()
            bundle.putLong(EXTRA_ID, id)

            val noteFragment = NoteDetailFragment()
            noteFragment.arguments = bundle
            return noteFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View? = activity?.layoutInflater?.inflate(R.layout.note_detail, null)

        editTextTitle = view?.findViewById(R.id.edit_text_title) as EditText
        editTextDescription = view.findViewById(R.id.edit_text_description) as EditText

        var newNote = true

        if(arguments != null && arguments?.getLong(EXTRA_ID) != 0L) {
            id = arguments?.getLong(EXTRA_ID) as Long
            val uri: Uri = Uri.withAppendedPath(URI_NOTES, id.toString())
            val cursor: Cursor? =
                activity?.contentResolver?.query(uri, null, null, null, null)

            if(cursor?.moveToNext() as Boolean){
                newNote = false
                editTextTitle.setText(cursor.getString(cursor.getColumnIndex(TITLE_NOTES)))
                editTextDescription.setText(cursor.getString(cursor.getColumnIndex(DESCRIPTION_NOTES)))
            }
            cursor.close()
        }

        return AlertDialog.Builder(activity as Activity)
            .setTitle(if (newNote) "Nova mensagem" else "Digite a nova mensagem")
            .setView(view)
            .setPositiveButton("Salvar", this)
            .setNegativeButton("Cancelar", this)
            .create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val values = ContentValues()
        values.put(TITLE_NOTES, editTextTitle.text.toString())
        values.put(DESCRIPTION_NOTES, editTextDescription.text.toString())

        if(id != 0L){
            val uri: Uri = Uri.withAppendedPath(URI_NOTES, id.toString())
            context?.contentResolver?.update(uri, values, null, null)
        } else {
            context?.contentResolver?.insert(URI_NOTES, values)
        }
    }
}