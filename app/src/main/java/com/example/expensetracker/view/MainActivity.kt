package com.example.expensetracker.view

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.expensetracker.FormButtonFragment
import com.example.expensetracker.R
import com.example.expensetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isTablet = resources.getBoolean(R.bool.is_tablet)
        val isLandscape = resources.getBoolean(R.bool.is_landscape)

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val savedMode = prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        AppCompatDelegate.setDefaultNightMode(savedMode)

        if(isLandscape||isTablet){
            attach(ExpenseListFragment.newInstance(), R.id.list_pane)
            attach(FormButtonFragment.newInstance(), R.id.tab_fragment_container)
        }

        val cursor = contentResolver.query(
                Uri.parse("content://com.invoice_crud_android.provider/invoices"),
        null,null,null,null,
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow("_id"))
                val name = it.getString(it.getColumnIndexOrThrow("name"))
            }
        }
    }

    private fun attach(fragment: Fragment, id: Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(id,fragment)
        fragmentTransaction.commit()
    }
}