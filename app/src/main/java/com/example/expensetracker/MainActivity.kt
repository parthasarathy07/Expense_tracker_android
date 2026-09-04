package com.example.expensetracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.Util.SampleExpenses
import com.example.expensetracker.adapter.ExpenseAdapter
import com.example.expensetracker.model.Expense
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var expenseAdapter: ExpenseAdapter
    private var expenses = mutableListOf<Expense>()

    private val startExpenseForm = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data
        if (result.resultCode == RESULT_OK) {
            val expense = result.data?.getParcelableExtra<Expense>("expense")
            if (expense != null) {
                expenses.add(expense)
                expenseAdapter.notifyDataSetChanged()
            }
        }
    }
    private val startDetailPage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data
        if (result.resultCode == RESULT_OK) {
            val updatedExpense = result.data?.getParcelableExtra<Expense>("updated_expense")
            if (updatedExpense != null) {
                val index = expenses.indexOfFirst { it.id == updatedExpense.id }
                if (index != -1) {
                    expenses[index] = updatedExpense
                    expenseAdapter.notifyItemChanged(index)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expense_list)

        val sampleExpenses = SampleExpenses()
        expenses = sampleExpenses.expenses

        val expenseRecyclerView = findViewById<RecyclerView>(R.id.expenseRecyclerView)
        val addExpenseFab = findViewById<FloatingActionButton>(R.id.addExpenseFab)

        expenseAdapter = ExpenseAdapter(expenses){ expense ->
            onClick(expense)
        }

        expenseRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = expenseAdapter
        }

        addExpenseFab.setOnClickListener {
            startExpenseForm.launch(Intent(this, ExpenseFormActivity::class.java))
        }
    }
    fun onClick(expense: Expense){
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("expense",expense)
        }
        startDetailPage.launch(intent)
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