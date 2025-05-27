package com.anmpdev.uas_budgeting_app.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.anmpdev.uas_budgeting_app.R
import com.anmpdev.uas_budgeting_app.databinding.FragmentCreateNewExpensesBinding
import com.anmpdev.uas_budgeting_app.model.Expense
import com.anmpdev.uas_budgeting_app.viewmodel.CreateExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CreateNewExpenses : Fragment() {

    private lateinit var binding:FragmentCreateNewExpensesBinding
    private lateinit var viewModel:CreateExpenseViewModel
    /*var totalExpense = 1000000; //buat setting awal
    var totalBudget = 2500000; //buat setting awal*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateNewExpensesBinding.inflate(layoutInflater, container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set initial valuenya dlu buat tanggalnya gimana
        val currentDate = Date()
        //ambil data tanggal sesuai hari ini, pake ID biar dia namanya mei :) not may
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        //waktu create nex expense, yang ditampilin cuman tanggalnya aja
        binding.txtTanggal.text = dateFormat.format(currentDate)
        //dapetin timestamp dari jam sekarang
        val timestamp = System.currentTimeMillis()
        //waktu details dan di list yang ditampilin sama menitnya juga
        //pakai unix timestamp
        //deklarasikan viewmodel disini, dimana vm utk class ini dr createviewmodel
        viewModel = ViewModelProvider(this).get(CreateExpenseViewModel::class.java)
        //viewModel.readExpense() //buat debugging
        binding.btnAddExpense.setOnClickListener {
            var nominal = binding.txtNominal.text.toString().toInt()
            var notes = binding.txtNotes.text.toString()
            //buat objek expense
            var expense = Expense(1,1,timestamp,nominal,notes)
            //masukin ke dalam list >> yang dimasukin berupa list
            //krn kita pake vararg
            viewModel.addExpense(listOf(expense))
            Toast.makeText(context,"New Expense Created!",Toast.LENGTH_SHORT).show()
            //balikin ke page sebelumnya otomatis
            Navigation.findNavController(it).popBackStack()
        }
        /*val currentDate = Date()
        //ambil data tanggal sesuai hari ini, pake ID biar dia namanya mei :) not may
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        binding.txtTanggal.text = dateFormat.format(currentDate)
        //set pengeluaran skrg brp >> ambil dari db
        binding.txtTotalExpense.text = "Rp." + totalExpense.toString()
        binding.txtTotalBudget.text = "Rp."+totalBudget.toString()

        //set buat progress bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.expenseProgress.min= 0 //set biar dia minimum progressnya 0
            //hrs cek SDK krn fitur ini bru bisa di minimal sdk 26 :)
        };
        binding.expenseProgress.max = totalBudget; //maxnya total budget
        binding.expenseProgress.progress=totalExpense //buat progressnya gimana

        //hitung selisih antara min dan max >> harusnya di vm??
        binding.textInputLayout.hint = "Nominal (IDR "+(totalBudget-totalExpense).toString()+" left)";*/
    }
}