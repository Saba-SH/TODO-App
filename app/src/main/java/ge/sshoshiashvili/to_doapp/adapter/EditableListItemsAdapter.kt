package ge.sshoshiashvili.to_doapp.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.sshoshiashvili.to_doapp.data.entity.ListItem
import ge.sshoshiashvili.to_doapp.data.entity.TodoItem
import ge.sshoshiashvili.to_doapp.databinding.EditableListItemBinding

class EditableListItemsAdapter(
    val context: Context,
    val todoItemPinInterface: TodoItemPinInterface,
    val todoItemDeleteInterface: TodoItemDeleteInterface,
    private val listItemsControlInterface: ListItemsControlInterface,
    var allListItems: MutableList<ListItem>
) : RecyclerView.Adapter<EditableListItemsAdapter.EditableListItemViewHolder>() {


    inner class EditableListItemViewHolder(val binding: EditableListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val checkBox = binding.checkBox
        val editText = binding.editText
        val deleteButton = binding.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditableListItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EditableListItemBinding.inflate(layoutInflater, parent, false)
        return EditableListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditableListItemViewHolder, position: Int) {
        val currListItem: ListItem = allListItems[holder.adapterPosition]
        holder.checkBox.isChecked = currListItem.completed
        holder.editText.setText(currListItem.name)
        holder.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable) {
                val it = allListItems[holder.adapterPosition]
                val newItem =
                    ListItem(holder.editText.text.toString(), it.completed, it.todoItemId)
                newItem.id = it.id
                allListItems[holder.adapterPosition] = newItem
            }
        })

        holder.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                holder.deleteButton.visibility = VISIBLE
            } else {
                holder.deleteButton.visibility = GONE
            }
        }

        holder.checkBox.setOnClickListener {
            listItemsControlInterface.onListItemCheckChange(
                holder.adapterPosition,
                allListItems[holder.adapterPosition]
            )
        }

        holder.deleteButton.setOnClickListener {
            listItemsControlInterface.onListItemDeleteClick(holder.adapterPosition)
        }

        holder.deleteButton.visibility = GONE
        if (position >= itemCount - 1) {
            holder.editText.requestFocus()
        }
        if (currListItem.completed) {
            holder.editText.isEnabled = false
            holder.deleteButton.visibility = GONE
        }
    }

    override fun getItemCount(): Int {
        return allListItems.size
    }

}

interface TodoItemPinInterface {
    fun onTodoItemPinChange(todoItem: TodoItem)
}

interface TodoItemDeleteInterface {
    fun onTodoItemDelete(todoItem: TodoItem)
}

interface ListItemsControlInterface {
    fun onListItemAdd(listItem: ListItem)
    fun onListItemEdit(position: Int, listItem: ListItem)
    fun onListItemDeleteClick(position: Int)
    fun onListItemCheckChange(position: Int, oldItem: ListItem)
}
