package com.example.expensetracker.Util
import com.example.expensetracker.model.Expense
import com.example.expensetracker.model.Group
import java.time.LocalDate

object SampleExpenses {

    val expenses = listOf(
        Expense(
            id = 1,
            amount = 250.0,
            date = LocalDate.of(2026, 8, 31),
            reason = "Lunch",
            group = Group.FOOD
        ),
        Expense(
            id = 2,
            amount = 120.0,
            date = LocalDate.of(2026, 8, 30),
            reason = "Vegetables",
            group = Group.GROCERY
        ),
        Expense(
            id = 3,
            amount = 1500.0,
            date = LocalDate.of(2026, 8, 29),
            reason = "School Fees",
            group = Group.EDUCATION
        ),
        Expense(
            id = 4,
            amount = 80.0,
            date = LocalDate.of(2026, 8, 28),
            reason = "Tea and Snacks",
            group = Group.FOOD
        ),
        Expense(
            id = 5,
            amount = 650.0,
            date = LocalDate.of(2026, 8, 27),
            reason = "Groceries",
            group = Group.GROCERY
        ),
        Expense(
            id = 6,
            amount = 300.0,
            date = LocalDate.of(2026, 8, 26),
            reason = "Books",
            group = Group.EDUCATION
        ),
        Expense(
            id = 7,
            amount = 450.0,
            date = LocalDate.of(2026, 8, 25),
            reason = "Dinner",
            group = Group.FOOD
        ),
        Expense(
            id = 8,
            amount = 200.0,
            date = LocalDate.of(2026, 8, 24),
            reason = "Cleaning Items",
            group = Group.GROCERY
        ),
        Expense(
            id = 9,
            amount = 100.0,
            date = LocalDate.of(2026, 8, 23),
            reason = "Bus Fare",
            group = Group.OTHERS
        ),
        Expense(
            id = 10,
            amount = 750.0,
            date = LocalDate.of(2026, 8, 22),
            reason = "Online Course",
            group = Group.EDUCATION
        )
    )
}