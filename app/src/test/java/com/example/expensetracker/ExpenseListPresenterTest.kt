package com.example.expensetracker

import android.content.SharedPreferences
import com.example.expensetracker.contract.ExpenseListContract
import com.example.expensetracker.model.Expense
import com.example.expensetracker.model.ExpenseDatabase
import com.example.expensetracker.model.Group
import com.example.expensetracker.presenter.ExpenseListPresenter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import java.time.LocalDate
import kotlin.collections.emptyList

class ExpenseListPresenterTest {

    private val view: ExpenseListContract.View = mock()
    private val db: ExpenseDatabase = mock()
    private val prefs: SharedPreferences = mock()

    private lateinit var presenter: ExpenseListPresenter

    @BeforeEach
    fun setUp() {
        presenter = ExpenseListPresenter(view, db, prefs)
    }

    @Test
    fun `calculateTotal should return correct sum when expenses exist`() {
        // Given
        val mockExpenses = listOf(
            Expense(
                id = "1", amount = 10.50,
                date = LocalDate.now(),
                reason = "demo",
                group = Group.GROCERY
            ),
            Expense(id = "2", amount = 20.25,date = LocalDate.now(),
                reason = "demo",
                group = Group.GROCERY),
            Expense(id = "3", amount = 5.25,date = LocalDate.now(),
                reason = "demo",
                group = Group.GROCERY)
        )
        `when`(db.getAllExpenses()).thenReturn(mockExpenses)

        // When
        val result = presenter.calculateTotal()

        // Then
        assertEquals(36.00, result, 0.001)
    }

    @Test
    fun `calculateTotal should return zero when expense list is empty`() {
        // Given
        `when`(db.getAllExpenses()).thenReturn(emptyList())

        // When
        val result = presenter.calculateTotal()

        // Then
        assertEquals(0.0, result, 0.001)
    }
}