package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TodoApp()
            }
        }
    }
}

data class TodoItem(val id: Int, var text: String, var isCompleted: Boolean = false)

@Composable
fun TodoApp() {
    var todoItems by remember { mutableStateOf(listOf<TodoItem>()) }
    var nextId by remember { mutableStateOf(0) }
    var newTodoText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Todo App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                label = { Text("New Todo") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newTodoText.isNotBlank()) {
                    todoItems = todoItems + TodoItem(nextId++, newTodoText)
                    newTodoText = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(todoItems) { item ->
                TodoItem(
                    item = item,
                    onToggle = {
                        todoItems = todoItems.map {
                            if (it.id == item.id) it.copy(isCompleted = !it.isCompleted) else it
                        }
                    },
                    onRemove = {
                        todoItems = todoItems.filter { it.id != item.id }
                    }
                )
            }
        }
    }
}

@Composable
fun TodoItem(item: TodoItem, onToggle: () -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = { onToggle() }
        )
        Text(
            text = item.text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onRemove) {
            Text("X")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoAppPreview() {
    MaterialTheme {
        TodoApp()
    }
}

@Preview(showBackground = true)
@Composable
fun TodoItemPreview() {
    MaterialTheme {
        TodoItem(
            item = TodoItem(1, "Sample Todo Item", false),
            onToggle = {},
            onRemove = {}
        )
    }
}