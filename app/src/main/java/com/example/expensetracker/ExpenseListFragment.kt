package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.adapter.ExpenseAdapter
import com.example.expensetracker.databinding.FragmentExpenseListBinding
import com.example.expensetracker.model.Expense

class ExpenseListFragment : Fragment(R.layout.fragment_expense_list) {
    companion object {
        fun newInstance(): ExpenseListFragment {
            return ExpenseListFragment()
        }
    }

    private lateinit var binding: FragmentExpenseListBinding
    private lateinit var adapter: ExpenseAdapter
    private lateinit var expenses:List<Expense>

    private var isTablet = false
    private var isLandscape = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isTablet = resources.getBoolean(R.bool.is_tablet)
        isLandscape = resources.getBoolean(R.bool.is_landscape)

        if(isTablet || isLandscape){
            binding.toolbar.updatePadding(top = 0)
        }

        val mainActivity = activity as MainActivity
        expenses = mainActivity.expenses

        setupRecyclerView()
        setupFab()
    }
    private fun setupFab(){
        binding.addExpenseFab.setOnClickListener {
            val formFragment = ExpenseFormFragment.newInstance()
            replace(formFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = ExpenseAdapter(expenses) { expense ->
            onClick(expense)
        }

        binding.expenseRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ExpenseListFragment.adapter
        }
    }
    fun onClick(expense: Expense){

        val expenseDetailFragment = ExpenseDetailFragment.newInstance()

        val args = Bundle().apply {
            putParcelable("expense", expense)
        }
        expenseDetailFragment.arguments = args

        replace(expenseDetailFragment)
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