package com.example.expensetracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.Util.SampleExpenses
import com.example.expensetracker.adapter.ExpenseAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val expenses = SampleExpenses.expenses

        val expenseRecyclerView = findViewById<RecyclerView>(R.id.expenseRecyclerView)
        val addExpenseFab = findViewById<FloatingActionButton>(R.id.addExpenseFab)

        expenseAdapter = ExpenseAdapter(expenses)

        expenseRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = expenseAdapter
        }

        addExpenseFab.setOnClickListener {
            startActivity(Intent(this, ExpenseFormActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
    }
    override fun onResume() {
        super.onResume()
        expenseAdapter.notifyDataSetChanged()
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