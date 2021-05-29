package gaur.himanshu.august.notes

import gaur.himanshu.august.notes.room.Notes
import gaur.himanshu.august.notes.room.NotesDao
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val notesDao: NotesDao) {


    suspend fun insertNote(note: Notes) {
        notesDao.insertNote(note)
    }


    suspend fun updateNote(note: Notes) {
        notesDao.updateNotes(note)
    }

    suspend fun deleteNote(note: Notes) {
        notesDao.deleteNotes(note)
    }

    suspend fun getAllNotes(email:String): List<Notes> {
        return notesDao.getAllNotes(email)
    }

}