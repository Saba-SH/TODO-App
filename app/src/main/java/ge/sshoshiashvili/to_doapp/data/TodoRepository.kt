package ge.sshoshiashvili.to_doapp.data

import androidx.lifecycle.LiveData
import ge.sshoshiashvili.to_doapp.data.entity.ListItem
import ge.sshoshiashvili.to_doapp.data.entity.TodoItem

interface TodoRepository {

    fun getAllTodoItems(): LiveData<List<TodoItem>>

    fun searchTodoItems(text: String): LiveData<List<TodoItem>>

    suspend fun getTodoItemById(id: Long): TodoItem?

    suspend fun insertTodoItem(todoItem: TodoItem): Long

    suspend fun updateTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(todoItem: TodoItem)


    fun getAllListItems(): LiveData<List<ListItem>>

    suspend fun getListItemById(id: Long): ListItem?

    fun getListItemsByTodoId(todoId: Long): LiveData<List<ListItem>>

    suspend fun insertListItem(listItem: ListItem): Long

    suspend fun updateListItem(listItem: ListItem)

    suspend fun deleteListItem(listItem: ListItem)

}