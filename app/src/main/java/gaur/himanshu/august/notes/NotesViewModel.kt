package gaur.himanshu.august.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.notes.room.Notes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {


    private val _notes = MutableStateFlow<NotesController>(NotesController.Empty)
    val notes: StateFlow<NotesController> = _notes


    sealed class NotesController {
        object Loading : NotesController()
        object Empty : NotesController()
        data class Success(val notes: List<Notes>) : NotesController()
        data class Error(val message: String) : NotesController()
    }


    fun insertNote(note: Notes) = viewModelScope.launch {
        repository.insertNote(note)
    }

    fun deleteNote(note: Notes) = viewModelScope.launch {
        repository.deleteNote(note)
    }

    fun updateNote(note: Notes) = viewModelScope.launch {
        repository.updateNote(note)
    }


    fun getAllData(email:String) = viewModelScope.launch {
        _notes.value = NotesController.Loading
        _notes.value = NotesController.Success(repository.getAllNotes(email))
    }

}