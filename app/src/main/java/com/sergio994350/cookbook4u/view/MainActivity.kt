package com.sergio994350.cookbook4u.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sergio994350.cookbook4u.CookBookApplication
import com.sergio994350.cookbook4u.R
import com.sergio994350.cookbook4u.db.FoodDatabase
import com.sergio994350.cookbook4u.repository.FoodRepository
import com.sergio994350.cookbook4u.viewModel.FoodViewModel
import com.sergio994350.cookbook4u.viewModel.FoodViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: FoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val foodRepository = FoodRepository(FoodDatabase(this))
        val viewModelProviderFactory = FoodViewModelProviderFactory(application as CookBookApplication, foodRepository)

        navView.setupWithNavController(navController)

        viewModel = ViewModelProvider(this, viewModelProviderFactory)[FoodViewModel::class.java]
    }
}