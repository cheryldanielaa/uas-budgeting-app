package com.anmpdev.uas_budgeting_app.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anmpdev.uas_budgeting_app.R
import com.anmpdev.uas_budgeting_app.databinding.DialogCardBinding
import com.anmpdev.uas_budgeting_app.viewmodel.BudgetViewModel
import com.anmpdev.uas_budgeting_app.viewmodel.ListExpenseViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DialogFragment : DialogFragment() {
    private lateinit var binding:DialogCardBinding
    private lateinit var viewModel:ListExpenseViewModel
    private lateinit var vmBudget:BudgetViewModel
    private var idBudget:Int = 0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DialogCardBinding.inflate(layoutInflater,container,false)

        //ambil id dari bundle
        //dialog fragment args itu mksdnya adalah dialogfragment tu fragment skrg
        val idExpenses = DialogFragmentArgs.fromBundle(requireArguments()).idExpense

        viewModel = ViewModelProvider(this).get(ListExpenseViewModel::class.java)
        vmBudget = ViewModelProvider(this).get(BudgetViewModel::class.java)
        
        viewModel.fetch(idExpenses) //ambil data id expense tertentu

        observeViewModel()

        //jika diklik maka close
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
    fun observeViewModel(){
        viewModel.expenseData.observe(viewLifecycleOwner, Observer {
            idBudget = it.idBudget //set disini
//            Log.d("ID_BUDGET",idBudget.toString())

            vmBudget.selectABudget(idBudget)

            val timestamp = it.tanggal
            //convert ke tgl biasa
            val tanggal = Date(timestamp)
            //ubah ke format hh:mm a >> contoh 12 mei 2025 10.15 am
            val dateFormat = SimpleDateFormat("dd MMMM yyyy hh:mm a",  Locale("id", "ID"))
            val str_tanggal = dateFormat.format(tanggal)

            //tampilin tanggal
            binding.txtTanggal.text = str_tanggal
            binding.txtNotes.text=it.notes

            //tampilin nominal pengeluaran
            val formatCurrency = NumberFormat.getInstance(Locale("id","ID"))
            formatCurrency.maximumFractionDigits = 0  //biar tdk ada desimal di belakangnya jadi gak ada kayak 1.000,00
            val nominal = it.nominal

            binding.txtHarga.text = "IDR ${formatCurrency.format(nominal)}"
        });

        //panggil view model dari budget untuk baca nama dari budget dgn
        //id tertentu
        vmBudget.budgetData.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                binding.chipCategory.text = it.budget_name
            }
        })
    }
}
