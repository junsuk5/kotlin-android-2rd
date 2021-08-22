package com.survivalcoding.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.survivalcoding.todolist.data.Todo
import com.survivalcoding.todolist.data.TodoDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

// AndroidViewModel은 액티비티와 수명을 같이한다 ①
class MainViewModel(application: Application) : AndroidViewModel(application) {
    // Room 데이터베이스 ②
    private val db = Room.databaseBuilder(
        application,
        TodoDatabase::class.java, "todo"
    ).build()

    // DB의 결과를 관찰할 수 있도록 하는 방법 ③
    private val _items = MutableStateFlow<List<Todo>>(emptyList())
    val items: StateFlow<List<Todo>> = _items

    var selectedTodo: Todo? = null

    // 초기화시 모든 데이터를 읽어 옴 ④
    init {
        // ⑤
        // ViewModel과 AndroidViewModel 클래스는 viewModelScope 코루틴 스코프를 제공
        // launch 함수 내에서 suspend 메서드를 실행할 수 있고 이는 비동기로 동작함
        viewModelScope.launch {
            // Flow 객체는 collect로 현재 값을 가져올 수 있음 ⑥
            db.todoDao().getAll().collect { todos ->
                // StateFlow 객체는 value 프로퍼티로 현재 상태값을 읽거나 쓸 수 있음 ⑦
                _items.value = todos
            }
        }
    }

    // ⑧
    fun addTodo(text: String) {
        viewModelScope.launch {
            db.todoDao().insert(Todo(text))
        }
    }

    fun updateTodo(text: String) {
        selectedTodo?.let { todo ->
            todo.apply {                    // ③
                title = text
                date = Calendar.getInstance().timeInMillis
            }

            viewModelScope.launch {
                db.todoDao().update(todo)
            }
            selectedTodo = null
        }
    }

    // ⑨
    fun deleteTodo(id: Long) {
        _items.value
            .find { todo -> todo.id == id }
            ?.let { todo ->  // ⑩
                viewModelScope.launch {
                    db.todoDao().delete(todo)
                }
                selectedTodo = null
            }
    }
}