package com.example.expensetracker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import android.app.DatePickerDialog
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.expensetracker.Util.SampleExpenses
import com.example.expensetracker.model.Expense
import com.example.expensetracker.model.Group
import java.time.LocalDate
import java.util.Calendar

class ExpenseFormActivity : AppCompatActivity() {

    private var selectedDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_form)

        val expenseReasonInput = findViewById<EditText>(R.id.expenseReasonInput)
        val expenseAmountInput = findViewById<EditText>(R.id.expenseAmountInput)
        val expenseCategorySpinner = findViewById<Spinner>(R.id.expenseCategorySpinner)
        val datePickerButton = findViewById<Button>(R.id.datePickerButton)
        val submitButton = findViewById<Button>(R.id.submitButton)

        val categories = Group.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        expenseCategorySpinner.adapter = adapter

        // Date picker
        datePickerButton.setOnClickListener {
            showDatePicker()
        }

        submitButton.setOnClickListener {
            val reason = expenseReasonInput.text.toString().trim()
            val amount = expenseAmountInput.text.toString().trim()
            val selectedCategory = expenseCategorySpinner.selectedItem.toString()

            if (reason.isNotEmpty() && amount.isNotEmpty()) {
                try {
                    val expense = Expense(
                        amount = amount.toDouble(),
                        date = selectedDate,
                        reason = reason,
                        group = Group.valueOf(selectedCategory)
                    )

                    SampleExpenses.expenses.add(expense)
                    Toast.makeText(this, "Expense added!", Toast.LENGTH_SHORT).show()

                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                findViewById<Button>(R.id.datePickerButton).text = "Date: $dayOfMonth/${month + 1}/$year"
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )
        datePickerDialog.show()
    }
    override fun onStart() {
        super.onStart()
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}