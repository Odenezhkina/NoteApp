package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Note

@Entity(
    tableName = "notes"
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
) {
    fun mapToNote(): Note =
        Note(id = id, title = title, content = content, categories = emptyList(), date = null)

    companion object {
        fun from(note: Note): NoteEntity {
            return NoteEntity(
                title = note.title,
                content = note.content
            )
        }
    }
}
