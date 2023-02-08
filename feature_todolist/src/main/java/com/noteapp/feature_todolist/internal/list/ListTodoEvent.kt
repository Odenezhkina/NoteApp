package com.noteapp.feature_todolist.internal.list

import com.example.domain.model.Todo
import com.example.domain.model.TodoFilterAdditionalConditions
import com.example.domain.model.TodoFilterPeriod

internal sealed interface ListTodoEvent {
    data class DeleteTodo(val todo: Todo) : ListTodoEvent
    data class UpdateTodoCompletedStatus(val todoId: Long, val isCompleted: Boolean) : ListTodoEvent

    // filters
    data class UpdateTodoFilterPeriod(val todoPeriod: TodoFilterPeriod) : ListTodoEvent
    data class UpdateSelectedCategoriesId(val newId: Long) : ListTodoEvent
    data class UpdateTodoAdditionalFilters(val newCondition: TodoFilterAdditionalConditions) :
        ListTodoEvent


    object Reload : ListTodoEvent
    object RestoreTodo : ListTodoEvent
    object SaveTodoFilters : ListTodoEvent
}
