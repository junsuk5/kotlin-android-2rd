package com.survivalcoding.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity     // 1
data class Todo(    // 2
    var title: String,
    var date: Long = Calendar.getInstance().timeInMillis,
) {
    @PrimaryKey(autoGenerate = true)    // 3
    var id: Long = 0
}