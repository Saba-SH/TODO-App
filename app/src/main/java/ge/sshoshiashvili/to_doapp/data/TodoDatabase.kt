package ge.sshoshiashvili.to_doapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ge.sshoshiashvili.to_doapp.data.dao.ListItemDao
import ge.sshoshiashvili.to_doapp.data.dao.TodoItemDao
import ge.sshoshiashvili.to_doapp.data.entity.ListItem
import ge.sshoshiashvili.to_doapp.data.entity.TodoItem

@Database(entities = [TodoItem::class, ListItem::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoItemDao(): TodoItemDao
    abstract fun listItemDao(): ListItemDao

    companion object {
        private const val dbName = "todo_db"

        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    dbName
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }

}