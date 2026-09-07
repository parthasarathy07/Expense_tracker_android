package com.example.expensetracker

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.expensetracker.databinding.ExpenseFormBinding
import com.example.expensetracker.model.Expense
import com.example.expensetracker.model.Group
import java.time.LocalDate

class ExpenseFormActivity : AppCompatActivity() {

    private lateinit var binding: ExpenseFormBinding
    private var selectedDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.expense_form)

        val categories = Group.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.expenseCategorySpinner.adapter = adapter

        val expense = intent.getParcelableExtra("expense", Expense::class.java)

        expense?.let{
            binding.expense = it
            binding.expenseCategorySpinner.setSelection(categories.indexOf(it.group.name))
            selectedDate = it.date
            binding.datePickerButton.text = "Date: ${it.date.dayOfMonth}/${it.date.monthValue}/${it.date.year}"
            binding.submitButton.text = "Update"
        }

        binding.datePickerButton.setOnClickListener {
            showDatePicker()
        }

        binding.submitButton.setOnClickListener {
            val reason = binding.expenseReasonInput.text.toString().trim()
            val amount = binding.expenseAmountInput.text.toString().trim()
            val selectedCategory = binding.expenseCategorySpinner.selectedItem.toString()

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
                binding.datePickerButton.text = "Date: $dayOfMonth/${month + 1}/$year"
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )
        datePickerDialog.show()
    }
}