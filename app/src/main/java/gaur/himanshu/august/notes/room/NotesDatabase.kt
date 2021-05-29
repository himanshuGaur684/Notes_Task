package gaur.himanshu.august.notes.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Notes::class], exportSchema = false, version = 1)

abstract class NotesDatabase : RoomDatabase() {


    companion object {
        fun getInstance(context: Context): NotesDatabase {
            return Room.databaseBuilder(context, NotesDatabase::class.java, "notes_db")
                .fallbackToDestructiveMigration().build()
        }
    }

    abstract fun getNotesDao():NotesDao

}