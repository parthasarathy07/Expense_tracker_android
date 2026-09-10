package com.example.expensetracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.example.expensetracker.Util.ExpenseDatabase
import com.example.expensetracker.databinding.ExpenseFormBinding
import com.example.expensetracker.model.Expense
import com.example.expensetracker.model.Group
import java.time.LocalDate

class ExpenseFormFragment : Fragment() {
    companion object{
        fun newInstance() = ExpenseFormFragment()
    }

    private lateinit var binding: ExpenseFormBinding
    private var selectedDate = LocalDate.now()
    private var expenseToEdit: Expense? = null

    private var isTablet = false
    private var isLandscape = false

    private lateinit var db: ExpenseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.expense_form, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = ExpenseDatabase(requireContext())
        isTablet = resources.getBoolean(R.bool.is_tablet)
        isLandscape = resources.getBoolean(R.bool.is_landscape)

        setupCategorySpinner()

        expenseToEdit = arguments?.getParcelable<Expense>("expense")

        expenseToEdit?.let{
            binding.expense = it
            val categories = Group.values().map { it.name }
            binding.expenseCategorySpinner.setSelection(categories.indexOf(it.group.name))
            selectedDate = it.date
            binding.datePickerButton.text = "Date: ${it.date.dayOfMonth}/${it.date.monthValue}/${it.date.year}"
            binding.submitButton.text = "Update"
        }

        binding.datePickerButton.setOnClickListener {
            showDatePicker()
        }

        binding.submitButton.setOnClickListener {
            submitExpense()
        }
    }

    private fun setupCategorySpinner() {
        val categories = Group.values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.expenseCategorySpinner.adapter = adapter
    }

    private fun submitExpense() {
        val reason = binding.expenseReasonInput.text.toString().trim()
        val amount = binding.expenseAmountInput.text.toString().trim()
        val selectedCategory = binding.expenseCategorySpinner.selectedItem.toString()

        if (reason.isEmpty() || amount.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val newExpense = Expense(
                amount = amount.toDouble(),
                date = selectedDate,
                reason = reason,
                group = Group.valueOf(selectedCategory)
            ).apply {
                expenseToEdit?.let { this.id = it.id }
            }

            expenseSubmitListener(newExpense)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
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
    fun expenseSubmitListener(newExpense: Expense){
        parentFragmentManager.popBackStack()

        if(expenseToEdit == null){
            db.insertExpense(newExpense)

            val expenseDetailFragment = ExpenseDetailFragment.newInstance()

            val args = Bundle().apply {
                putParcelable("expense", newExpense)
            }
            expenseDetailFragment.arguments = args

            replace(expenseDetailFragment)

        }else{
            db.updateExpense(newExpense)
        }
    }

    private fun replace(fragment: Fragment) {

        parentFragmentManager.beginTransaction().apply {

            if( isTablet || isLandscape ){
                replace(R.id.tab_fragment_container, fragment)
            }else{
                replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
            }

            commit()
        }
    }
}