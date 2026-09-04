package com.example.expensetracker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.util.UUID

@Parcelize
data class Expense(
    var id: String = UUID.randomUUID().toString(),
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