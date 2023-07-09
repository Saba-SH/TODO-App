package ge.sshoshiashvili.to_doapp.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ge.sshoshiashvili.to_doapp.adapter.TodoItemClickInterface
import ge.sshoshiashvili.to_doapp.adapter.TodoItemsAdapter
import ge.sshoshiashvili.to_doapp.data.entity.ListItem
import ge.sshoshiashvili.to_doapp.data.entity.TodoItem
import ge.sshoshiashvili.to_doapp.databinding.ActivityMainBinding
import ge.sshoshiashvili.to_doapp.main.edit_todo.EditTodoActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), IMainView, TodoItemClickInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var pinnedItemsAdapter: TodoItemsAdapter
    private lateinit var otherItemsAdapter: TodoItemsAdapter

    private lateinit var pinnedTodoItemsRV: RecyclerView
    private lateinit var otherTodoItemsRV: RecyclerView
    private lateinit var addFAB: FloatingActionButton

    private var resultContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult? ->
            if (result?.resultCode == RESULT_OK) {
                Log.e("", "")
                binding.searchBar.setText("")
                refreshTodos("")
                Thread {
                    Thread.sleep(350)
                    runOnUiThread {
                        binding.searchBar.requestFocus()
                    }
                }.start()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pinnedTodoItemsRV = binding.pinnedItems
        otherTodoItemsRV = binding.otherItems

        pinnedItemsAdapter = TodoItemsAdapter(this, this)
        pinnedTodoItemsRV.adapter = pinnedItemsAdapter
        otherItemsAdapter = TodoItemsAdapter(this, this)
        otherTodoItemsRV.adapter = otherItemsAdapter

        addFAB = binding.floatingActionButton

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MainViewModel::class.java]

        addFAB.setOnClickListener {
            binding.searchBar.clearFocus()
            CoroutineScope(Dispatchers.IO).launch {
                val intent = Intent(this@MainActivity, EditTodoActivity::class.java)
                intent.putExtra("openType", "add")
                resultContract.launch(intent)
            }
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable) {
                val text = binding.searchBar.text.toString()
                refreshTodos(text)
            }
        })
        binding.searchBar.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                refreshTodos(binding.searchBar.text.toString())
            }
        }

        refreshTodos("")
    }

    private fun refreshTodos(text: String) {
        var pinnedTodoItems: MutableList<TodoItem> = mutableListOf()
        var otherTodoItems: MutableList<TodoItem> = mutableListOf()
        var allListItems: MutableList<ListItem> = mutableListOf()

        Thread {
            val allTodoItems: LiveData<List<TodoItem>> = if (text == "")
                viewModel.getAllTodoItems()
            else
                viewModel.searchTodoItems(text)
            val listItems = viewModel.getAllListItems()

            runOnUiThread {
                allTodoItems.observe(this) { value ->
                    pinnedTodoItems.clear()
                    otherTodoItems.clear()
                    pinnedTodoItems.addAll(value.filter { it.isPinned })
                    otherTodoItems.addAll(value.filter { !it.isPinned })

                    pinnedItemsAdapter.allTodoItems = pinnedTodoItems
                    otherItemsAdapter.allTodoItems = otherTodoItems

                    Log.e("", "")
                    Log.e("", "")

                    if (pinnedTodoItems.size == 0) {
                        binding.tvPinned.visibility = GONE
                        binding.tvOther.visibility = GONE
                        binding.pinnedItems.visibility = GONE
                    } else {
                        binding.tvPinned.visibility = VISIBLE
                        binding.tvOther.visibility = VISIBLE
                        binding.pinnedItems.visibility = VISIBLE
                    }

                    pinnedItemsAdapter.notifyDataSetChanged()
                    otherItemsAdapter.notifyDataSetChanged()
                }

                listItems.observe(this) { value ->
                    allListItems.clear()
                    allListItems.addAll(value)

                    pinnedItemsAdapter.allListItems = allListItems
                    otherItemsAdapter.allListItems = allListItems

                    pinnedItemsAdapter.notifyDataSetChanged()
                    otherItemsAdapter.notifyDataSetChanged()
                }
            }

            binding.otherItems.visibility = VISIBLE
        }.start()
    }

    override fun onTodoItemClick(todoItem: TodoItem) {
        binding.searchBar.clearFocus()
        val intent = Intent(this@MainActivity, EditTodoActivity::class.java)
        intent.putExtra("openType", "edit")
        intent.putExtra("todoId", todoItem.id)
        intent.putExtra("todoTitle", todoItem.title)
        intent.putExtra("todoPinned", todoItem.isPinned)
        resultContract.launch(intent)
    }
}
