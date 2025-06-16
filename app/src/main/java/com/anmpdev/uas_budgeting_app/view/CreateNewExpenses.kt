package com.anmpdev.uas_budgeting_app.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.anmpdev.uas_budgeting_app.databinding.FragmentCreateNewExpensesBinding
import com.anmpdev.uas_budgeting_app.model.Budget
import com.anmpdev.uas_budgeting_app.model.Expense
import com.anmpdev.uas_budgeting_app.viewmodel.CreateExpenseViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateNewExpenses : Fragment(){

    private lateinit var binding:FragmentCreateNewExpensesBinding
    private lateinit var viewModel:CreateExpenseViewModel
    private lateinit var selectedBudget:Budget //buat tampung objek yg dipilih
    private lateinit var listBudget:ArrayList<Budget>
    private var sisa_uang:Int = 0 //ini sisa uang bakal dideclare buat checking

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateNewExpensesBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //deklarasikan viewmodel disini, dimana vm utk class ini dr createviewmodel
        viewModel = ViewModelProvider(this).get(CreateExpenseViewModel::class.java)

        //nanti diganti pake vmbudget
        viewModel.readBudget() //baca budgetnya
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

        binding.btnAddExpense.setOnClickListener {
            var nominal = binding.txtNominal.text.toString().toInt()
            var notes = binding.txtNotes.text.toString()

            //pengecekan backstack smua hrs di view model?!
            //buat objek expense
            var expense = Expense(1,selectedBudget.idBudget,timestamp,nominal,notes)

            //masukin ke dalam list >> yang dimasukin berupa list
            //krn kita pake vararg
            viewModel.addExpense(expense,sisa_uang) //kirim param expense dan sisa uang
            /*setelah add expense ini dilakukan pengecekan di view model,
            baru nanti di observe hasilnya gimana TT >> MAMA PUSING


            Toast.makeText(context,"New Expense Created!",Toast.LENGTH_SHORT).show()
            balikin ke page sebelumnya otomatis
            Navigation.findNavController(it).popBackStack()
            cek apakah sisa uang > nominal, jika ya baru toast
           if(nominal>=sisa_uang){

            }
            else{
                Toast.makeText(context,"Sorry, your budget isn't enough for this expense!",Toast.LENGTH_SHORT).show()
            }*/
           //Log.d("NOMINAL",selectedBudget.idBudget.toString())
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

        binding.budgetType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedBudget = listBudget[position]
                val totalBudget = selectedBudget.nominal
                val formatCurrency = NumberFormat.getCurrencyInstance(Locale
                    ("id", "ID"))
                formatCurrency.maximumFractionDigits = 0
                binding.txtTotalBudget.text = formatCurrency.format(totalBudget)
                binding.expenseProgress.max = totalBudget
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    binding.expenseProgress.min = 0
                }
                viewModel.fetchNominal(selectedBudget.idBudget)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        observeViewModel()
    }
    //baca viewmodel
    fun observeViewModel(){
        //baca list budget yang ada
        viewModel.budgetLD.observe(viewLifecycleOwner, Observer {
            listBudget = it as ArrayList<Budget> //tampung value dari it di arraylist
            //perbarui data adapter scr terus menerus
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            //set value dari dropdown (sppinner)
            binding.budgetType.adapter = adapter
        });
        viewModel.totalPengeluaran.observe(viewLifecycleOwner, Observer {
            val formatCurrency = NumberFormat.getCurrencyInstance(Locale("id","ID"))
            formatCurrency.maximumFractionDigits = 0
            //yang terjadi adalah dia update valuenya
            binding.txtTotalExpense.text = formatCurrency.format(it)
            //set value dari expense progressnya seberapa
            binding.expenseProgress.progress = it
        });

        viewModel.sisaUang.observe(viewLifecycleOwner, Observer {
            //update di text input layout
            if (it != null) {
                sisa_uang = it //value dari sisa uang
            }
            val formatCurrency = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            formatCurrency.maximumFractionDigits = 0
            val formatted = formatCurrency.format(sisa_uang)
            binding.textInputLayout.hint = "Nominal ("+formatted+" left)";
        });

        viewModel.statusInsert.observe(viewLifecycleOwner, Observer {
            if(it==true){
                //jika return true, maka toast dan muncul back ke backstack
                Toast.makeText(context,"New Expense Created!",Toast.LENGTH_SHORT).show()
                //balikin ke page sebelumnya otomatis
                //karena dia di dlm viewmodel jd pake require view, krn it itu hasilnya bool
                Navigation.findNavController(requireView()).popBackStack()
            }
            else{
                Toast.makeText(context,"Sorry, your budget isn't enough for this expense!",Toast.LENGTH_SHORT).show()
            }
        })
    }
}