package ge.sshoshiashvili.to_doapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ListItem(
    val name: String?,
    val completed: Boolean,
    val todoItemId: Long // Foreign key referencing the TodoItem
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    override fun toString(): String {
        return super.toString() + " " + name + ", " + (if (completed) "checked" else "unchecked") +
                ", todoId=" + todoItemId.toString() + ", id=" + id.toString()
    }
}

