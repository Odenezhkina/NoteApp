package com.example.noteapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import com.example.noteapp.ui.util.exceptions.LostNavArgumentsException
import com.example.noteapp.ui.util.exceptions.NotFoundException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoDetailsViewModel @Inject constructor(
    private val noteRepository: TodoRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    private val todoId: Long? by lazy { state.get<Long>("todoId") }
    private var _todo: MutableStateFlow<UiState<Todo>> = MutableStateFlow(UiState.Loading())
    val todo: StateFlow<UiState<Todo>> = _todo

    init {
        loadData()
    }

    fun loadData() {
        _todo.value = UiState.Loading()
        viewModelScope.launch {
            todoId?.let {
                val repoNote = noteRepository.getById(it)
                _todo.value = repoNote?.let { repoTodo -> UiState.Success(repoTodo) } ?: run {
                    UiState.Error(NotFoundException())
                }
            } ?: run { _todo.value = UiState.Error(LostNavArgumentsException()) }
        }
    }

}