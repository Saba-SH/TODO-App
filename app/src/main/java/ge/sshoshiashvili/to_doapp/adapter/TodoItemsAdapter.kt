package ge.sshoshiashvili.to_doapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.sshoshiashvili.to_doapp.data.entity.ListItem
import ge.sshoshiashvili.to_doapp.data.entity.TodoItem
import ge.sshoshiashvili.to_doapp.databinding.ItemTodoBinding

class TodoItemsAdapter(
    val context: Context,
    val todoItemClickInterface: TodoItemClickInterface,
    var allTodoItems: MutableList<TodoItem> = mutableListOf(),
    var allListItems: MutableList<ListItem> = mutableListOf()
) : RecyclerView.Adapter<TodoItemsAdapter.TodoItemViewHolder>() {

    inner class TodoItemViewHolder(val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleTv = binding.textViewTitle
        val item1 = binding.item1
        val item2 = binding.item2
        val item3 = binding.item3
        val dotsTv = binding.tvDots
        val checkedItemsTv = binding.tvCheckedItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTodoBinding.inflate(layoutInflater, parent, false)
        return TodoItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        Log.e("", "")
        val currTodoItem: TodoItem = allTodoItems[position]
        holder.titleTv.text = currTodoItem.title
        val listItems: List<ListItem> = allListItems.filter { it.todoItemId == currTodoItem.id }
        val uncheckedListItems = listItems.filter { !it.completed }

        Log.e("", "")
        val checkedItemsCount: Int = listItems.filter { it.completed }.size
        if (checkedItemsCount > 0) {
            holder.checkedItemsTv.visibility = VISIBLE
            holder.checkedItemsTv.text = "+${checkedItemsCount} checked items"
        } else {
            holder.checkedItemsTv.visibility = GONE
        }

        holder.item1.isEnabled = false
        holder.item2.isEnabled = false
        holder.item3.isEnabled = false

        holder.dotsTv.visibility = VISIBLE
        holder.item3.visibility = VISIBLE
        holder.item2.visibility = VISIBLE
        holder.item1.visibility = VISIBLE

        if (uncheckedListItems.size < 4) {
            holder.dotsTv.visibility = GONE
        }
        if (uncheckedListItems.size < 3) {
            holder.item3.visibility = GONE
        } else {
            holder.item3.text = uncheckedListItems[2].name
        }
        if (uncheckedListItems.size < 2) {
            holder.item2.visibility = GONE
        } else {
            holder.item2.text = uncheckedListItems[1].name
        }
        if (uncheckedListItems.size < 1) {
            holder.item1.visibility = GONE
        } else {
            holder.item1.text = uncheckedListItems[0].name
        }

        holder.itemView.setOnClickListener {
            todoItemClickInterface.onTodoItemClick(allTodoItems[position])
        }
    }

    override fun getItemCount(): Int {
        return allTodoItems.size
    }

    fun updateTodoItemList(newList: List<TodoItem>) {
        allTodoItems.clear()
        allTodoItems.addAll(newList)
        notifyDataSetChanged()
    }


    fun updateListItemList(newList: List<ListItem>) {
        allListItems.clear()
        allListItems.addAll(newList)
        notifyDataSetChanged()
    }
}

interface TodoItemClickInterface {
    fun onTodoItemClick(todoItem: TodoItem)
}
