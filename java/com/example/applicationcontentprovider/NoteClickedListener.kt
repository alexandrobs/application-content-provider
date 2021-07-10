package com.example.applicationcontentprovider

import android.database.Cursor

interface NoteClickedListener {
    fun notesClickedItem(cursor: Cursor)
    fun notesRemoveItem(cursor: Cursor?)
}