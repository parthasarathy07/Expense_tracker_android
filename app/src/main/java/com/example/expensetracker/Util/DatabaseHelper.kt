package com.example.expensetracker.Util

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.expensetracker.model.Expense
import com.example.expensetracker.model.Group
import java.time.LocalDate

class ExpenseDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "expense_tracker.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "expenses"
        const val id_Column = "id"
        const val amount = "amount"
        const val date = "date"
        const val reason = "reason"
        const val group_type = "group_type"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE  $TABLE_NAME(
                $id_Column TEXT PRIMARY KEY,
                $amount REAL NOT NULL,
                $date TEXT NOT NULL,
                $reason TEXT NOT NULL,
                $group_type TEXT NOT NULL
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS expenses")
        onCreate(db)
    }

    fun insertExpense(expense: Expense) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(id_Column, expense.id)
            put(amount, expense.amount)
            put(date, expense.date.toString())
            put(reason, expense.reason)
            put(group_type, expense.group.name)
        }
        db.insert("expenses", null, values)
        db.close()
    }

    fun getAllExpenses(): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM expenses ORDER BY date DESC", null)

        while (cursor.moveToNext()) {
            val id = cursor.getString(0)
            val amount = cursor.getDouble(1)
            val date = LocalDate.parse(cursor.getString(2))
            val reason = cursor.getString(3)
            val group = Group.valueOf(cursor.getString(4))
            expenses.add(Expense(id, amount, date, reason, group))
        }
        cursor.close()
        db.close()
        return expenses
    }

    fun getExpenseById(id: String): Expense? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM expenses WHERE id = ?", arrayOf(id))

        var expense: Expense? = null
        if (cursor.moveToNext()) {
            val amount = cursor.getDouble(1)
            val date = LocalDate.parse(cursor.getString(2))
            val reason = cursor.getString(3)
            val group = Group.valueOf(cursor.getString(4))
            expense = Expense(id, amount, date, reason, group)
        }
        cursor.close()
        db.close()
        return expense
    }

    fun updateExpense(expense: Expense) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(amount, expense.amount)
            put(date, expense.date.toString())
            put(reason, expense.reason)
            put(group_type, expense.group.name)
        }
        db.update("expenses", values, "id = ?", arrayOf(expense.id))
        db.close()
    }

    fun deleteExpense(id: String) {
        val db = writableDatabase
        db.delete("expenses", "id = ?", arrayOf(id))
        db.close()
    }

    fun deleteAllExpenses() {
        val db = writableDatabase
        db.delete("expenses", null, null)
        db.close()
    }

    fun getTotalExpense(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM(amount) FROM expenses", null)
        cursor.moveToNext()
        val total = cursor.getDouble(0)
        cursor.close()
        db.close()
        return total
    }

}