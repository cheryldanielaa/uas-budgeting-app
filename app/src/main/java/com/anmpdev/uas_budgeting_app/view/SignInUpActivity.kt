package com.anmpdev.uas_budgeting_app.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.anmpdev.uas_budgeting_app.R
import com.anmpdev.uas_budgeting_app.databinding.ActivityMainBinding
import com.anmpdev.uas_budgeting_app.databinding.ActivitySignInUpBinding

class SignInUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("SETTING", Context.MODE_PRIVATE)
        val loginState = sharedPreferences.getBoolean("LOGIN_STATE", false)

        if (loginState) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        Log.d("LOGIN_STATE", loginState.toString())
    }
}