package com.survivalcoding.todolist.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.survivalcoding.todolist.data.Todo
import com.survivalcoding.todolist.databinding.ItemTodoBinding

class TodoListAdapter(
    private val onClick: (Todo) -> Unit,    // ①
) : ListAdapter<Todo, TodoListAdapter.TodoViewHolder>(TodoDiffUtilCallback()) {     // ②

    private lateinit var binding: ItemTodoBinding   // ③

    // ④
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        binding =
            ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)   // ⑤
        return TodoViewHolder(binding, onClick)     // ⑥
    }

    // ⑦
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.setOnClickListener(getItem(position))
    }

    class TodoViewHolder(
        private val binding: ItemTodoBinding,
        private val onClick: (Todo) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo) {
            binding.text1.text = todo.title
            binding.text2.text = DateFormat.format("yyyy/MM/dd", todo.date)
        }

        fun setOnClickListener(todo: Todo) {
            binding.root.setOnClickListener {
                onClick(todo)
            }
        }
    }
}