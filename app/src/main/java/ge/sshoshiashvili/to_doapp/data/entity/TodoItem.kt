package ge.sshoshiashvili.to_doapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoItem(
    val title: String?,
    val isPinned: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    override fun toString(): String {
        return super.toString() + " " + title + ", " + (if (isPinned) "pinned" else "unpinned") +
                ", id=" + id.toString()
    }
}
