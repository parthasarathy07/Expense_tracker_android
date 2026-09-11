package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.example.expensetracker.databinding.FragmentExpenseDetailBinding
import com.example.expensetracker.model.Expense
import java.time.format.DateTimeFormatter

class ExpenseDetailFragment : Fragment(R.layout.fragment_expense_detail) {
    companion object {
        fun newInstance(): ExpenseDetailFragment {
            return ExpenseDetailFragment()
        }
    }
    private lateinit var binding: FragmentExpenseDetailBinding
    private lateinit var expense: Expense
    var isTablet = false
    var isLandscape = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener("expense_detail_updated", viewLifecycleOwner) { _, bundle ->
            val updatedExpense = bundle.getParcelable<Expense>("expense")
            if (updatedExpense != null && updatedExpense.id == this.expense.id) {
                displayExpenseDetails(updatedExpense)
                this.expense = updatedExpense
            }
        }

        val newExpense = arguments?.getParcelable<Expense>("expense")
        newExpense?.let {
            displayExpenseDetails(it)
            this.expense = it
        }

        isTablet = resources.getBoolean(R.bool.is_tablet)
        isLandscape = resources.getBoolean(R.bool.is_landscape)

        if(isTablet || isLandscape){
            binding.toolbar.updatePadding(top = 0)
        }

        binding.editButton.setOnClickListener {
            expense.let { expenseToEdit ->

                val editFormFragment = ExpenseFormFragment.newInstance()

                val args = Bundle().apply {
                    putParcelable("expense", expenseToEdit)
                }
                editFormFragment.arguments = args

                replace(editFormFragment)
            }
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

    private fun replace(fragment: Fragment) {

        parentFragmentManager.beginTransaction().apply {

            if( isTablet || isLandscape ){
                replace(R.id.tab_fragment_container, fragment)
            }else{
                replace(R.id.fragment_container, fragment)
                addToBackStack(null)
            }
            commit()
        }
    }
}