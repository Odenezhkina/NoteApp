package com.example.mainlist.api.noteitem

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noteapp.model.Note
import com.noteapp.model.NoteItem
import com.noteapp.model.Todo
import com.noteapp.ui.R
import com.noteapp.ui.recycler.note.NoteViewHolder
import com.noteapp.ui.recycler.todo.TodoViewHolder

class NoteItemAdapter : ListAdapter<NoteItem, RecyclerView.ViewHolder>(NoteItemDiffUtilCallback()) {
    var onNoteClick: ((Long) -> Unit)? = null // (noteId: Long) -> Unit
    var onTodoClick: ((Long) -> Unit)? = null // (todoId: Long) -> Unit
    var onTodoCheckboxClick: ((Long, Boolean) -> Unit)? = null // (todoId: Long) -> Unit


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Note -> R.layout.item_note
            is Todo -> R.layout.item_todo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_note -> NoteViewHolder.create(
                parent,
                onNoteClick
            )
            else -> TodoViewHolder.create(parent, onTodoClick, onTodoCheckboxClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return when (val item = getItem(position)) {
            is Note -> (holder as NoteViewHolder).bind(item)
            is Todo -> (holder as TodoViewHolder).bind(item)
        }
    }
}