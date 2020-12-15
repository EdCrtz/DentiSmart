package com.dreamteam.dentismart.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dreamteam.dentismart.R
import com.dreamteam.dentismart.models.LoginResult
import com.dreamteam.dentismart.utils.Rutinas
import com.dreamteam.dentismart.utils.SessionManagement
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var appSettingPrefs: SharedPreferences
    private lateinit var sharedPrefsEdit: SharedPreferences.Editor
    private var isNightModeOn: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        appSettingPrefs = getSharedPreferences("AppSettingPrefs", 0)
        sharedPrefsEdit = appSettingPrefs.edit()
        val mode =
            applicationContext?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                isNightModeOn = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                isNightModeOn = false
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                isNightModeOn = false
            }
        }
        isNightModeOn = appSettingPrefs.getBoolean("NightMode", isNightModeOn)

        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_citas, R.id.nav_tratamientos, R.id.nav_pagos
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val headerView = navView.getHeaderView(0)
        val email = headerView.findViewById<TextView>(R.id.txtEmail)
        val name = headerView.findViewById<TextView>(R.id.txtName)
        val userName = headerView.findViewById<TextView>(R.id.userName)
        val sessionManagement = SessionManagement(this)
        val currentUser: LoginResult? = sessionManagement.getCurrentUser()
        val image = headerView.findViewById<ImageView>(R.id.imgUser)
        if (currentUser!!.foto != null && currentUser.foto != "") {
            val img = Rutinas.convertBase64ToBitMap(currentUser.foto.split(',')[1])
            image.setImageBitmap(img)
            val miLayout = headerView.findViewById<LinearLayout>(R.id.layoutMenu)
            miLayout.setBackgroundColor(Rutinas.generateDominantColor(img))
        }
        name.text = "${currentUser.nombre} ${currentUser.apellidoPat} ${currentUser.apellidoMat}"
        email.text = currentUser.email
        userName.text = currentUser.nombreUsuario
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val item = menu.findItem(R.id.darkmode)
        if (isNightModeOn) {
            item.title = "Modo dia"
        } else {
            item.title = "Modo oscuro"
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.buttonLogout) {
            val session = SessionManagement(applicationContext)
            session.logoutUser()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        if (item.itemId == R.id.darkmode) {
            if (isNightModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefsEdit.putBoolean("NightMode", false)
                sharedPrefsEdit.apply()
                item.title = "Modo oscuro"
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefsEdit.putBoolean("NightMode", true)
                sharedPrefsEdit.apply()
                item.title = "Modo dia"
            }
        }
        return super.onOptionsItemSelected(item)
    }
}