package gaur.himanshu.august.notes.room

import androidx.room.*

@Dao
interface NotesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Notes)

    @Delete
    suspend fun deleteNotes(notes: Notes)

    @Update
    suspend fun updateNotes(notes: Notes)

    @Query("SELECT * FROM Notes WHERE email =:email")
    suspend fun getAllNotes(email: String): List<Notes>

}