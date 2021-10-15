package com.ederchua.tomorrowapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.content.Intent





class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        var drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        var mainToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)

        var actionBarDrawerToggle = ActionBarDrawerToggle(this,  drawerLayout, mainToolbar, R.string.openNavDrawer, R.string.closeNavDrawer)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_letterA-> {
                val `in` = Intent(applicationContext, LetterAActivity::class.java)
                startActivity(`in`)
            }
            R.id.nav_letterB -> {
                val `in` = Intent(applicationContext, LetterBActivity::class.java)
                startActivity(`in`)
            }
        }
        return true
    }
}