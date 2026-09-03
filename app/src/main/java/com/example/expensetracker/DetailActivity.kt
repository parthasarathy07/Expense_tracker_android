package com.example.expensetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.expensetracker.databinding.ExpenseDetailBinding
import com.example.expensetracker.model.Expense
import java.time.format.DateTimeFormatter

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ExpenseDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ExpenseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val expense = intent.getParcelableExtra("expense", Expense::class.java)

        expense?.let {
            displayExpenseDetails(it)
        }
    }

    private fun displayExpenseDetails(expense: Expense) {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        binding.apply {
            receipt.setImageResource(R.drawable.img)
            categoryDetailText.text = expense.group.name
            dateDetailText.text = expense.date.format(formatter)
            reasonText.text = expense.reason
            descriptionDetailText.text = expense.reason
            amountText.text = "₹${String.format("%.2f", expense.amount)}"
        }
    }
}