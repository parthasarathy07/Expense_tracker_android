package com.example.expensetracker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import android.app.DatePickerDialog
import android.content.Intent
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.expensetracker.model.Expense
import com.example.expensetracker.model.Group
import java.time.LocalDate

class ExpenseFormActivity : AppCompatActivity() {

    private var selectedDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expense_form)

        val expenseReasonInput = findViewById<EditText>(R.id.expenseReasonInput)
        val expenseAmountInput = findViewById<EditText>(R.id.expenseAmountInput)
        val expenseCategorySpinner = findViewById<Spinner>(R.id.expenseCategorySpinner)
        val datePickerButton = findViewById<Button>(R.id.datePickerButton)
        val submitButton = findViewById<Button>(R.id.submitButton)

        val categories = Group.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        expenseCategorySpinner.adapter = adapter

        val expense = intent.getParcelableExtra("expense", Expense::class.java)

        expense?.let{
            expenseReasonInput.setText(it.reason)
            expenseAmountInput.setText(it.amount.toString())
            expenseCategorySpinner.setSelection(
                categories.indexOf(it.group.name)
            )
            selectedDate = it.date
            datePickerButton.text = "Date: ${it.date.dayOfMonth}/${it.date.monthValue}/${it.date.year}"
            submitButton.text = "Update"
        }

        datePickerButton.setOnClickListener {
            showDatePicker()
        }

        submitButton.setOnClickListener {
            val reason = expenseReasonInput.text.toString().trim()
            val amount = expenseAmountInput.text.toString().trim()
            val selectedCategory = expenseCategorySpinner.selectedItem.toString()

            if (reason.isNotEmpty() && amount.isNotEmpty()) {
                try {
                    val newExpense = Expense(
                        amount = amount.toDouble(),
                        date = selectedDate,
                        reason = reason,
                        group = Group.valueOf(selectedCategory)
                    ).apply {
                        expense?.let { this.id = it.id }
                    }

                    val resultIntent = Intent()
                    resultIntent.putExtra("expense", newExpense)
                    setResult(RESULT_OK, resultIntent)

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