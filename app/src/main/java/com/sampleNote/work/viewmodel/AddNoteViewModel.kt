package com.sampleNote.work.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.Bindable
import com.sampleNote.work.BR
import com.sampleNote.work.ContextDispatchers
import com.sampleNote.work.model.Note
import com.sampleNote.work.model.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AddNoteViewModel
@Inject constructor (
        private val contextDispatchers: ContextDispatchers,
        private val noteRepository: NoteRepository)
    : ObservableViewModel(), CoroutineScope{

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + contextDispatchers.Main

    private val scope = CoroutineScope(coroutineContext)


    @get:Bindable var isAddBtnEnabled = false
    var isNoteAdded = MutableLiveData<Boolean>()
    var newNote = Note(text = "")


    fun onNoteTextChanged(){
        isAddBtnEnabled = newNote.text.trim().length > 3
        notifyPropertyChanged(BR.addBtnEnabled)
    }

    fun saveNewNote() = scope.launch{
        isNoteAdded.postValue(
                try{
                    async(contextDispatchers.IO) { noteRepository.saveNote(newNote) }.await()
                    true
                }
                catch (e: Throwable){
                    false
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}