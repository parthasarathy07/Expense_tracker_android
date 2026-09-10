package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expensetracker.databinding.FragmentFormButtonBinding

class FormButtonFragment : Fragment(R.layout.fragment_form_button) {
    companion object {
        fun newInstance() = FormButtonFragment()
    }


    private lateinit var binding: FragmentFormButtonBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormButtonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
    }

    private fun setupButton(){
        binding.addExpenseButton.setOnClickListener {
            val formFragment = ExpenseFormFragment.newInstance()
            replace(formFragment)
        }
    }


    private fun replace(fragment: Fragment) {

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.tab_fragment_container, fragment)
            commit()
        }
    }
}