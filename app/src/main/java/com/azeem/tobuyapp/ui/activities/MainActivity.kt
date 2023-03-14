package com.azeem.tobuyapp.ui.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.azeem.tobuyapp.R
import com.azeem.tobuyapp.arch.ToBuyViewModel
import com.azeem.tobuyapp.database.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: ToBuyViewModel by viewModels()
        viewModel.init(AppDatabase.getDatabase(this))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        //define top-level fragments
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.categorizationFragment))

        //setup to app bar
        setupActionBarWithNavController(navController, appBarConfiguration)

        //setup bottom nav bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        setupWithNavController(bottomNavigationView, navController)

        //add out destination change listener to show/hide the bottom nav bar
        navController.addOnDestinationChangedListener{ controller, destination, arguments ->
            if(appBarConfiguration.topLevelDestinations.contains(destination.id)){
                bottomNavigationView.isVisible = true
            }else{
                bottomNavigationView.isGone = true
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onNavigateUp()
    }

    fun hideKeyboard(view: View) {
        val imm: InputMethodManager =
            application.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}
