package com.example.expensetracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R
import com.example.expensetracker.model.Expense
import java.time.format.DateTimeFormatter

class ExpenseAdapter(private val expenses: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense_card, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]

        holder.categoryText.text = expense.group.name
        holder.reasonText.text = expense.reason
        holder.dateText.text = expense.date.format(
            DateTimeFormatter.ofPattern("MMM dd, yyyy")
        )
        holder.amountText.text = "₹" + String.format("%.2f", expense.amount)
        holder.receipt.setImageResource(R.drawable.img)
    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryText: TextView = itemView.findViewById(R.id.categoryText)
        val reasonText: TextView = itemView.findViewById(R.id.reasonText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val amountText: TextView = itemView.findViewById(R.id.amountText)
        val receipt: ImageView = itemView.findViewById(R.id.receipt)
    }
}