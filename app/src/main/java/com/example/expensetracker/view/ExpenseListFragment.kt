package com.example.expensetracker.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.ExpenseDetailFragment
import com.example.expensetracker.ExpenseFormFragment
import com.example.expensetracker.R
import com.example.expensetracker.adapter.ExpenseAdapter
import com.example.expensetracker.contract.ExpenseListContract
import com.example.expensetracker.databinding.FragmentExpenseListBinding
import com.example.expensetracker.model.Expense
import com.example.expensetracker.model.ExpenseDatabase
import com.example.expensetracker.presenter.ExpenseListPresenter
import java.util.Locale

class ExpenseListFragment : Fragment(R.layout.fragment_expense_list), ExpenseListContract.View {

    companion object {
        fun newInstance(): ExpenseListFragment {
            return ExpenseListFragment()
        }
    }

    private lateinit var binding: FragmentExpenseListBinding
    private lateinit var presenter: ExpenseListContract.Presenter

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

        val db = ExpenseDatabase(requireContext())
        val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        presenter = ExpenseListPresenter(this, db, prefs)

        isTablet = resources.getBoolean(R.bool.is_tablet)
        isLandscape = resources.getBoolean(R.bool.is_landscape)

        parentFragmentManager.setFragmentResultListener("expense_list_updated", viewLifecycleOwner) { _, _ ->
            presenter.loadExpenses()
        }

        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        val isDarkMode = when (currentNightMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> true
            AppCompatDelegate.MODE_NIGHT_NO -> false
            else -> (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        }

        updateThemeButtonText(isDarkMode)
        presenter.loadExpenses()


        setupFab()
        setupLanguageDropdown()
        setupThemeToggle(isDarkMode)
    }

    override fun showExpenses(expenses: List<Expense>) {
        binding.expenseRecyclerView.visibility = View.VISIBLE
        binding.empty.visibility = View.GONE

        binding.expenseRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ExpenseAdapter(expenses) { expense ->
                presenter.onExpenseClicked(expense)
            }
        }
    }

    override fun showEmptyState() {
        binding.expenseRecyclerView.visibility = View.GONE
        binding.empty.visibility = View.VISIBLE
    }

    override fun updateThemeButtonText(isDarkMode: Boolean) {
        binding.themeToggleButton.text = if (isDarkMode) {
            getString(R.string.switch_to_light_mode)
        } else {
            getString(R.string.switch_to_dark_mode)
        }
    }

    override fun setSelectedLanguage(languageName: String) {
        binding.languageDropdown.setText(languageName, false)
    }

    override fun navigateToForm(isTabletOrLandscape: Boolean) {
        val formFragment = ExpenseFormFragment.newInstance()
        replaceFragment(formFragment)
    }

    override fun navigateToDetail(expense: Expense, isTabletOrLandscape: Boolean) {
        val expenseDetailFragment = ExpenseDetailFragment.newInstance()
        expenseDetailFragment.arguments = Bundle().apply {
            putParcelable("expense", expense)
        }
        replaceFragment(expenseDetailFragment)
    }

    private fun setupThemeToggle(isDarkMode: Boolean) {
        binding.themeToggleButton.setOnClickListener {
            presenter.onThemeToggleClicked(isDarkMode)
        }
    }

    private fun setupFab() {
        binding.addExpenseFab.setOnClickListener {
            presenter.onAddExpenseClicked()
        }
    }

    private fun setupLanguageDropdown() {
        val languages = resources.getStringArray(R.array.languages)
        val languageCodes = resources.getStringArray(R.array.language_codes)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, languages)
        binding.languageDropdown.setAdapter(adapter)

        val currentLocale = AppCompatDelegate.getApplicationLocales().get(0)?.language
            ?: Locale.getDefault().language
        val index = languageCodes.indexOf(currentLocale)

        if (index != -1) {
            setSelectedLanguage(languages[index])
        }

        binding.languageDropdown.setOnClickListener {
            adapter.filter.filter(null)
        }

        binding.languageDropdown.setOnItemClickListener { parent, _, position, _ ->
            val selectedLanguage = parent.getItemAtPosition(position) as String
            val selectedIndex = languages.indexOf(selectedLanguage)

            if (selectedIndex != -1) {
                presenter.onLanguageSelected(selectedIndex, languageCodes[selectedIndex])
            }
        }
    }



    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            if (isTablet || isLandscape) {
                replace(R.id.tab_fragment_container, fragment)
            } else {
                replace(R.id.fragment_container, fragment)
                addToBackStack(null)
            }
            commit()
        }
    }
}