package com.anmpdev.uas_budgeting_app.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.anmpdev.uas_budgeting_app.R
import com.anmpdev.uas_budgeting_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //inisialisasi navcontroller
        navController = (supportFragmentManager.findFragmentById(R.id.fragmentHost) as NavHostFragment).navController
        //buat koneksi untuk menghubungkan fragment dengan bottom nav
        binding.bottomNav.setupWithNavController(navController)

        val sharedPreferences = getSharedPreferences("SETTING", Context.MODE_PRIVATE)
        val loginState = sharedPreferences.getBoolean("LOGIN_STATE", false)

        //meskipun udh hapus sharedPreferences di signout, ttp hrs checking lg loginState apa biar ga lgsg msk mainActivity otomatis
        if (!loginState) {
            val intent = Intent(this, SignInUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}