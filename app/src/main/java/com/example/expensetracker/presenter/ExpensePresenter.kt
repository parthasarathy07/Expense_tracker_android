package com.example.expensetracker.presenter

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import com.example.expensetracker.contract.ExpenseListContract
import com.example.expensetracker.model.Expense
import com.example.expensetracker.model.ExpenseDatabase

class ExpenseListPresenter(
    private val view: ExpenseListContract.View,
    private val db: ExpenseDatabase,
    private val prefs: SharedPreferences
) : ExpenseListContract.Presenter {

    override fun calculateTotal(): Double {
        return db.getAllExpenses().sumOf { it.amount }
    }

    override fun loadExpenses() {
        val expenses = db.getAllExpenses()
        if (expenses.isEmpty()) {
            view.showEmptyState()
        } else {
            view.showExpenses(expenses)
        }
    }

    override fun onExpenseClicked(expense: Expense) {
        view.navigateToDetail(expense, false)
    }

    override fun onAddExpenseClicked() {
        view.navigateToForm(false)
    }

    override fun onThemeToggleClicked(isCurrentlyDark: Boolean) {
        val newMode = if (isCurrentlyDark) {
            AppCompatDelegate.MODE_NIGHT_NO
        } else {
            AppCompatDelegate.MODE_NIGHT_YES
        }

        AppCompatDelegate.setDefaultNightMode(newMode)
        prefs.edit { putInt("theme_mode", newMode) }

        view.updateThemeButtonText(!isCurrentlyDark)
    }

    override fun onLanguageSelected(selectedLanguageIndex: Int, languageCode: String) {
        val appLocale = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}