package ge.sshoshiashvili.to_doapp.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ge.sshoshiashvili.to_doapp.data.TodoDatabase
import ge.sshoshiashvili.to_doapp.data.TodoRepository
import ge.sshoshiashvili.to_doapp.data.TodoRepositoryImpl
import ge.sshoshiashvili.to_doapp.data.entity.ListItem
import ge.sshoshiashvili.to_doapp.data.entity.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TodoRepository

    init {
        val todoDao = TodoDatabase.getInstance(application).todoItemDao()
        val listDao = TodoDatabase.getInstance(application).listItemDao()

        repository = TodoRepositoryImpl(todoDao, listDao)

    }

    fun getAllTodoItems(): LiveData<List<TodoItem>> {
        return repository.getAllTodoItems()
    }

    fun searchTodoItems(text: String): LiveData<List<TodoItem>> {
        return repository.searchTodoItems(text)
    }

    fun getAllListItems(): LiveData<List<ListItem>> {
        return repository.getAllListItems()
    }

    fun deleteListItem(listItem: ListItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteListItem(listItem)
    }

    fun updateListItem(listItem: ListItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateListItem(listItem)
    }

    fun insertListItem(listItem: ListItem): Long = runBlocking {
        repository.insertListItem(listItem)
    }

    suspend fun getListItemById(id: Long): ListItem? {
        return repository.getListItemById(id)
    }

    fun getListItemsOfTodoItem(todoId: Long): LiveData<List<ListItem>> {
        return repository.getListItemsByTodoId(todoId)
    }

    suspend fun getTodoItemById(id: Long): TodoItem? {
        return repository.getTodoItemById(id)
    }

    fun insertTodoItem(todoItem: TodoItem): Long = runBlocking {
        repository.insertTodoItem(todoItem)
    }

    fun updateTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTodoItem(todoItem)
    }

    fun deleteTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTodoItem(todoItem)
    }

}