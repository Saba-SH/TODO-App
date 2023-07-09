package ge.sshoshiashvili.to_doapp.data

import androidx.lifecycle.LiveData
import ge.sshoshiashvili.to_doapp.data.dao.ListItemDao
import ge.sshoshiashvili.to_doapp.data.dao.TodoItemDao
import ge.sshoshiashvili.to_doapp.data.entity.ListItem
import ge.sshoshiashvili.to_doapp.data.entity.TodoItem

class TodoRepositoryImpl(
    private val todoItemDao: TodoItemDao,
    private val listItemDao: ListItemDao
) : TodoRepository {

    private val allTodoItems: LiveData<List<TodoItem>> = todoItemDao.getAllTodoItems()
    private val allListItems: LiveData<List<ListItem>> = listItemDao.getAllListItems()

    override fun getAllTodoItems(): LiveData<List<TodoItem>> {
        return allTodoItems
    }

    override fun searchTodoItems(text: String): LiveData<List<TodoItem>> {
        return todoItemDao.searchTodoItems(text)
    }

    override suspend fun getTodoItemById(id: Long): TodoItem? {
        return todoItemDao.getTodoItemById(id)
    }

    override suspend fun insertTodoItem(todoItem: TodoItem): Long {
        return todoItemDao.insertTodoItem(todoItem)
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        todoItemDao.updateTodoItem(todoItem)
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        todoItemDao.deleteTodoItem(todoItem)
    }

    override fun getAllListItems(): LiveData<List<ListItem>> {
        return allListItems
    }

    override suspend fun getListItemById(id: Long): ListItem? {
        return listItemDao.getListItemById(id)
    }

    override fun getListItemsByTodoId(todoId: Long): LiveData<List<ListItem>> {
        return listItemDao.getListItemsByTodoId(todoId)
    }

    override suspend fun insertListItem(listItem: ListItem): Long {
        return listItemDao.insertListItem(listItem)
    }

    override suspend fun updateListItem(listItem: ListItem) {
        listItemDao.updateListItem(listItem)
    }

    override suspend fun deleteListItem(listItem: ListItem) {
        listItemDao.deleteListItem(listItem)
    }
}