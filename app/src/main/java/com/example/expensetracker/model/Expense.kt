package com.example.expensetracker.model

import java.time.LocalDate

data class Expense(
    val id: Long = 0,
    val amount: Double,
    val date: LocalDate,
    val reason: String,
    val group: Group
)

enum class Group {
    FOOD,
    EDUCATION,
    GROCERY,
    OTHERS
}