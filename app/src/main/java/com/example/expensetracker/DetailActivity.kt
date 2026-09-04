package com.example.expensetracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.expensetracker.databinding.ExpenseDetailBinding
import com.example.expensetracker.model.Expense
import java.time.format.DateTimeFormatter

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ExpenseDetailBinding
    private lateinit var expense: Expense

    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedExpense = result.data?.getParcelableExtra("expense", Expense::class.java)
            if (updatedExpense != null) {
                expense = updatedExpense
                displayExpenseDetails(expense)
                intent.putExtra("expense", expense)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ExpenseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val expense = intent.getParcelableExtra("expense", Expense::class.java)

        expense?.let {
            displayExpenseDetails(it)
            this.expense = it
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(RESULT_OK, Intent().apply {
                    putExtra("updated_expense", this@DetailActivity.expense)
                })
                finish()
            }
        })

        binding.editButton.setOnClickListener {
            val intent = Intent(this, ExpenseFormActivity::class.java).apply {
                putExtra("expense", this@DetailActivity.expense)
            }
            editLauncher.launch(intent)
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