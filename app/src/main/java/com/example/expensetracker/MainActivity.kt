package com.example.expensetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.expensetracker.Util.SampleExpenses
import com.example.expensetracker.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var expenses = SampleExpenses().expenses

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isTablet = resources.getBoolean(R.bool.is_tablet)
        val isLandscape = resources.getBoolean(R.bool.is_landscape)

        if(isLandscape||isTablet){
            attach(ExpenseListFragment.newInstance(),R.id.list_pane)
            attach(FormButtonFragment.newInstance(),R.id.tab_fragment_container)
        }
    }

    private fun attach(fragment: Fragment,id: Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(id,fragment)
        fragmentTransaction.commit()
    }
}