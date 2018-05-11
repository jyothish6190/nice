package com.nice.nice.todo.models

import java.util.*


data class Task(val id: String, val task: String, val desc: String, val done: Boolean, val createdAt: Date, val due: Date, val completed: Date) {
    companion object {
        const val COLLECTION_KEY = "Tasks"
        const val TASK_KEY = "Task"
        const val DESC_KEY = "Desc"
        const val DONE_KEY = "Done"
        const val CREATED_KEY = "CreatedAt"
        const val COMPLETED_KEY = "CompletedAt"
        const val DUE_KEY = "DueDate"
    }
}