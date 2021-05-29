package gaur.himanshu.august.notes.hilt

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gaur.himanshu.august.notes.NotesRepository
import gaur.himanshu.august.notes.room.NotesDao
import gaur.himanshu.august.notes.room.NotesDatabase
import gaur.himanshu.august.notes.utils.Constants
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HiltModules {


    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): NotesDatabase {
        return NotesDatabase.getInstance(context)
    }


    @Singleton
    @Provides
    fun provideNoteDao(notesDatabase: NotesDatabase): NotesDao {
        return notesDatabase.getNotesDao()
    }

    @Singleton
    @Provides
    fun provideRepository(notesDao: NotesDao):NotesRepository{
        return NotesRepository(notesDao)
    }

    @Singleton
    @Provides
    fun provideSharedPrefrences(@ApplicationContext context: Context):SharedPreferences{
        return context.getSharedPreferences(Constants.SHAREDPREF_NAME,MODE_PRIVATE)
    }
}