package ge.sshoshiashvili.to_doapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ge.sshoshiashvili.to_doapp.data.entity.TodoItem

@Dao
interface TodoItemDao {

    @Query("SELECT * FROM TodoItem")
    fun getAllTodoItems(): LiveData<List<TodoItem>>

    @Query("SELECT * FROM TodoItem WHERE title LIKE '%' || :text || '%'")
    fun searchTodoItems(text: String): LiveData<List<TodoItem>>

    @Query("SELECT * FROM TodoItem WHERE id = :id")
    fun getTodoItemById(id: Long): TodoItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodoItem(todoItem: TodoItem): Long

    @Update
    fun updateTodoItem(todoItem: TodoItem)

    @Delete
    fun deleteTodoItem(todoItem: TodoItem)

}