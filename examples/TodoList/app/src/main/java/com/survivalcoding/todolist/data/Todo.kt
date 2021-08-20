package com.survivalcoding.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity     // 1
data class Todo(    // 2
    val title: String,
    val date: Long = Calendar.getInstance().timeInMillis,
) {
    @PrimaryKey(autoGenerate = true)    // 3
    var id: Long = 0
}