package ge.sshoshiashvili.to_doapp.main.edit_todo

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ge.sshoshiashvili.to_doapp.R
import ge.sshoshiashvili.to_doapp.adapter.EditableListItemsAdapter
import ge.sshoshiashvili.to_doapp.adapter.ListItemsControlInterface
import ge.sshoshiashvili.to_doapp.adapter.TodoItemDeleteInterface
import ge.sshoshiashvili.to_doapp.adapter.TodoItemPinInterface
import ge.sshoshiashvili.to_doapp.data.entity.ListItem
import ge.sshoshiashvili.to_doapp.data.entity.TodoItem
import ge.sshoshiashvili.to_doapp.databinding.ActivityEditTodoBinding
import ge.sshoshiashvili.to_doapp.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EditTodoActivity : AppCompatActivity(), IEditTodoView, TodoItemPinInterface,
    TodoItemDeleteInterface, ListItemsControlInterface {

    private lateinit var binding: ActivityEditTodoBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var checkedItemsAdapter: EditableListItemsAdapter
    private lateinit var uncheckedItemsAdapter: EditableListItemsAdapter

    private lateinit var checkedItemsRV: RecyclerView
    private lateinit var uncheckedItemsRV: RecyclerView
    private lateinit var itemAdder: LinearLayout

    private lateinit var openTodo: TodoItem
    private var openTodoId: Long = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkedItemsRV = binding.checkedItems
        uncheckedItemsRV = binding.uncheckedItems

        itemAdder = binding.itemAdder

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MainViewModel::class.java]

        itemAdder.setOnClickListener {
            onListItemAdd(ListItem("", false, openTodoId))
        }

        checkedItemsAdapter =
            EditableListItemsAdapter(this, this, this, this, mutableListOf())
        checkedItemsRV.adapter = checkedItemsAdapter
        uncheckedItemsAdapter =
            EditableListItemsAdapter(this, this, this, this, mutableListOf())
        uncheckedItemsRV.adapter = uncheckedItemsAdapter

        if (intent.getStringExtra("openType") == "edit") {
            var checkedListItems: MutableList<ListItem>
            var uncheckedListItems: MutableList<ListItem>
            openTodoId = intent.getLongExtra("todoId", -1)
            Thread {
                runBlocking {
                    Log.e("", "")
                    openTodo = viewModel.getTodoItemById(openTodoId)!!
                    Log.e("", "")
                    openTodo.id = openTodoId
                    viewModel.deleteTodoItem(openTodo)
                }
                val listItems = viewModel.getListItemsOfTodoItem(openTodoId)

                runOnUiThread {
                    binding.nameBar.setText(openTodo.title)
                    listItems.observe(this) { value ->
                        checkedListItems = value.filter { it.completed }.toMutableList()
                        uncheckedListItems = value.filter { !it.completed }.toMutableList()

                        checkedItemsAdapter.allListItems = checkedListItems
                        Log.e("", "")
                        checkedItemsAdapter.notifyDataSetChanged()
                        uncheckedItemsAdapter.allListItems =
                            uncheckedListItems
                        Log.e("", "")
                        uncheckedItemsAdapter.notifyDataSetChanged()

//                        Thread {
//                            for (listItem in value) {
//                                viewModel.deleteListItem(listItem)
//                                Log.e("DELETED LIST ITEM", listItem.toString())
//                            }
//                        }.start()
                    }
                }
            }.start()
            Log.e("", "")
            while (!::openTodo.isInitialized) {
                Log.e("", "")
                continue
            }
            if (openTodo.isPinned) {
                binding.ivPin.setImageResource(R.drawable.ic_pinned)
            } else {
                binding.ivPin.setImageResource(R.drawable.ic_pin)
            }
            Log.e("", "")
        } else {
            openTodo = TodoItem("", false)
            binding.ivPin.setImageResource(R.drawable.ic_pin)
        }
//        while (checkedListItems == null || uncheckedListItems == null) {
//            continue
//        }

        binding.ivPin.setOnClickListener {
            val newTodo = TodoItem(openTodo.title, !openTodo.isPinned)
            openTodo = newTodo
            onTodoItemPinChange(openTodo)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                updateData()
                finish()
            }
        })

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun updateData() {
        var done = false
        Thread {
            val newTodoItem = TodoItem(binding.nameBar.text.toString(), openTodo.isPinned)
            newTodoItem.id = openTodo.id
            if (newTodoItem.title != "" ||
                uncheckedItemsAdapter.allListItems.size + checkedItemsAdapter.allListItems.size > 0
            ) {
                val insertedTodoId = viewModel.insertTodoItem(newTodoItem)
                for (listItem in uncheckedItemsAdapter.allListItems + checkedItemsAdapter.allListItems) {
                    if (listItem.id != null)
                        viewModel.deleteListItem(listItem)
                    val realListItem = ListItem(listItem.name, listItem.completed, insertedTodoId)
                    viewModel.insertListItem(realListItem)
                }
            }
            done = true
        }.start()
        while (!done) {
            Log.e("", "")
            continue
        }
        setResult(RESULT_OK)
    }

    override fun onTodoItemPinChange(todoItem: TodoItem) {
        if (todoItem.isPinned) {
            binding.ivPin.setImageResource(R.drawable.ic_pinned)
        } else {
            binding.ivPin.setImageResource(R.drawable.ic_pin)
        }
    }

    override fun onTodoItemDelete(todoItem: TodoItem) {
        val act = this
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteTodoItem(todoItem)
//            val intent = Intent(this@EditTodoActivity, MainActivity::class.java)
//            startActivity(intent)
            act.finish()
        }
    }

    override fun onListItemAdd(listItem: ListItem) {
        Log.e("", "")
        uncheckedItemsAdapter.allListItems.add(listItem)
        uncheckedItemsAdapter.notifyItemInserted(uncheckedItemsAdapter.itemCount)
    }

    override fun onListItemEdit(position: Int, listItem: ListItem) {
        uncheckedItemsAdapter.allListItems[position] = listItem
        uncheckedItemsAdapter.notifyItemChanged(position)
    }

    override fun onListItemDeleteClick(position: Int) {
        val listItem = uncheckedItemsAdapter.allListItems[position]
        Thread {
            viewModel.deleteListItem(listItem)
        }.start()
        uncheckedItemsAdapter.allListItems.removeAt(position)
        uncheckedItemsAdapter.notifyItemRemoved(position)
    }

    override fun onListItemCheckChange(position: Int, oldItem: ListItem) {
        if (!oldItem.completed) {
            val newItem = ListItem(oldItem.name, true, oldItem.todoItemId)
            newItem.id = oldItem.id

            uncheckedItemsAdapter.allListItems.removeAt(position)
            checkedItemsAdapter.allListItems.add(newItem)

            runOnUiThread {
                uncheckedItemsAdapter.notifyItemRemoved(position)
                checkedItemsAdapter.notifyItemInserted(checkedItemsAdapter.itemCount)
            }
        } else {
            val newItem = ListItem(oldItem.name, false, oldItem.todoItemId)
            newItem.id = oldItem.id

            checkedItemsAdapter.allListItems.removeAt(position)
            uncheckedItemsAdapter.allListItems.add(newItem)

            runOnUiThread {
                checkedItemsAdapter.notifyItemRemoved(position)
                uncheckedItemsAdapter.notifyItemInserted(uncheckedItemsAdapter.itemCount)
            }
        }
    }

}
