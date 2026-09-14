package com.example.expensetracker

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.Util.ExpenseDatabase
import com.example.expensetracker.adapter.ExpenseAdapter
import com.example.expensetracker.databinding.FragmentExpenseListBinding
import com.example.expensetracker.model.Expense
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale
import androidx.core.content.edit

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

    private lateinit var db: ExpenseDatabase


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
        db = ExpenseDatabase(requireContext())

        parentFragmentManager.setFragmentResultListener("expense_list_updated", viewLifecycleOwner) { _, _ ->
            refreshList()
        }

        isTablet = resources.getBoolean(R.bool.is_tablet)
        isLandscape = resources.getBoolean(R.bool.is_landscape)


        refreshList()
        setupFab()
        setupLanguageDropdown()
        setupThemeToggle()
    }

    private fun setupThemeToggle() {
        val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        val isDarkMode = when (currentNightMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> true
            AppCompatDelegate.MODE_NIGHT_NO -> false
            else -> (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        }

        binding.themeToggleButton.text = if (isDarkMode) {
            getString(R.string.switch_to_light_mode)
        } else {
            getString(R.string.switch_to_dark_mode)
        }

        binding.themeToggleButton.setOnClickListener {
            val newMode = if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }

            AppCompatDelegate.setDefaultNightMode(newMode)

            prefs.edit { putInt("theme_mode", newMode) }
        }
    }

    private fun refreshList() {
        expenses = db.getAllExpenses()

        if (expenses.isEmpty()) {
            binding.expenseRecyclerView.visibility = View.GONE
            binding.empty.visibility = View.VISIBLE
        } else {
            setupRecyclerView()
            binding.expenseRecyclerView.visibility = View.VISIBLE
            binding.empty.visibility = View.GONE
        }
    }

    private fun setupFab(){
        binding.addExpenseFab.setOnClickListener {
            val formFragment = ExpenseFormFragment.newInstance()
            replace(formFragment)
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
            binding.languageDropdown.setText(languages[index], false)
        }

        binding.languageDropdown.setOnClickListener {
            adapter.filter.filter(null)
        }

        binding.languageDropdown.setOnItemClickListener { parent, _, position, _ ->
            val selectedLanguage = parent.getItemAtPosition(position) as String
            val selectedIndex = languages.indexOf(selectedLanguage)

            if (selectedIndex != -1) {
                val selectedLocale = languageCodes[selectedIndex]
                val appLocale = LocaleListCompat.forLanguageTags(selectedLocale)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
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