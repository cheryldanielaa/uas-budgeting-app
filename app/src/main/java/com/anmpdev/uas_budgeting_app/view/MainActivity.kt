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
        changeIconFromAdd() //pastiin panggil handlingnya disini
    }
    //ini fungsinya biar klo sekarang dia lagi di add expense, maka dia jg
    //navbarnya bakal brubah ke expense jg, soale awalnya dee bakal klo misal lagi di add
    //expense, pinda budget, balik ke expense, dee gabakal nandai expense e krn id e ga kedaftar di
    //navbar
    fun changeIconFromAdd(){
        //buat mapping fragment, jadi fragment yg anak dari fragment utama
        //bakal dilist disini
        //a to b >> a itu child, b parent nya
        val fragmentMap = mapOf(
            R.id.CreateNewExpenses to R.id.itemExpenses,
            R.id.addBudget to R.id.itemBudgeting,
            R.id.editBudget to R.id.itemBudgeting
            )
        //buat listenernya
        navController.addOnDestinationChangedListener { _, destination, _ ->
            //cari dulu item yang child ini dari hasil mapping
            val childItem = fragmentMap[destination.id]
            //klo semisal skrg lagi di item child, maka ya apnggil item itu,
            //klo gk ada ya ambil parentnya dan anggap sebagai fragment dengan IdAktif
            val navItemId = childItem ?: destination.id
            binding.bottomNav.menu.findItem(navItemId)?.isChecked = true
        }
    }
}