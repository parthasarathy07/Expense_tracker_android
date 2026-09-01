package com.example.expensetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.Util.SampleExpenses
import com.example.expensetracker.adapter.ExpenseAdapter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val expenses = SampleExpenses.expenses

        val expenseRecyclerView = findViewById<RecyclerView>(R.id.expenseRecyclerView)

        expenseRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ExpenseAdapter(expenses)
        }
    }
}