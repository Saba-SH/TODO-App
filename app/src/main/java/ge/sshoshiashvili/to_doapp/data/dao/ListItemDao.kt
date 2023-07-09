package ge.sshoshiashvili.to_doapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ge.sshoshiashvili.to_doapp.data.entity.ListItem

@Dao
interface ListItemDao {

    @Query("SELECT * FROM ListItem")
    fun getAllListItems(): LiveData<List<ListItem>>

    @Query("SELECT * FROM ListItem WHERE id = :id")
    fun getListItemById(id: Long): ListItem?

    @Query("SELECT * FROM ListItem WHERE todoItemId = :todoId")
    fun getListItemsByTodoId(todoId: Long): LiveData<List<ListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListItem(listItem: ListItem): Long

    @Update
    fun updateListItem(listItem: ListItem)

    @Delete
    fun deleteListItem(listItem: ListItem)

}
