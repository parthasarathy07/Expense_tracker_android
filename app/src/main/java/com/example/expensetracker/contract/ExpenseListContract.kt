package com.example.expensetracker.contract

import com.example.expensetracker.model.Expense

interface ExpenseListContract {

    interface View {
        fun showExpenses(expenses: List<Expense>)
        fun showEmptyState()
        fun updateThemeButtonText(isDarkMode: Boolean)
        fun setSelectedLanguage(languageName: String)
        fun navigateToForm(isTabletOrLandscape: Boolean)
        fun navigateToDetail(expense: Expense, isTabletOrLandscape: Boolean)
    }

    interface Presenter {
        fun calculateTotal(): Double
        fun loadExpenses()
        fun onExpenseClicked(expense: Expense)
        fun onAddExpenseClicked()
        fun onThemeToggleClicked(isCurrentlyDark: Boolean)
        fun onLanguageSelected(selectedLanguageIndex: Int, languageCode: String)
    }
}