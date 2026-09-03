package com.example.expensetracker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Expense(
    val id: Long = 0,
    val amount: Double,
    val date: LocalDate,
    val reason: String,
    val group: Group
): Parcelable

enum class Group {
    FOOD,
    EDUCATION,
    GROCERY,
    OTHERS
}