package com.survivalcoding.todolist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.survivalcoding.todolist.data.Todo

class TodoDiffUtilCallback : DiffUtil.ItemCallback<Todo>() {

    // 아이템이 같음을 판단하는 규칙 ①
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }

    // 내용을 비교하는 규칙 ②
    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }
}