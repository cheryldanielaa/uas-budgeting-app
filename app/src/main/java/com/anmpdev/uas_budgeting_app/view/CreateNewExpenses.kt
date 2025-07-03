package com.anmpdev.uas_budgeting_app.view

import android.content.Context
import android.content.SharedPreferences
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
import com.anmpdev.uas_budgeting_app.viewmodel.BudgetViewModel
import com.anmpdev.uas_budgeting_app.viewmodel.CreateExpenseViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateNewExpenses : Fragment(){

    private lateinit var binding: FragmentCreateNewExpensesBinding
    private lateinit var viewModel: CreateExpenseViewModel
    private lateinit var vmBudget: BudgetViewModel
    private lateinit var selectedBudget: Budget
    private lateinit var listBudget: ArrayList<Budget>
    private var sisa_uang: Int = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateNewExpensesBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ambil data shared preferences
        sharedPreferences = requireActivity().getSharedPreferences("SETTING", Context.MODE_PRIVATE)
        val uuid = sharedPreferences.getInt("user_id", 0)

        viewModel = ViewModelProvider(this).get(CreateExpenseViewModel::class.java)
        vmBudget = ViewModelProvider(this).get(BudgetViewModel::class.java)

        //baca budget berdasarkan id user yang sedang login
        vmBudget.readBudget(uuid)

        //set initial valuenya dlu buat tanggalnya gimana
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

        //waktu create nex expense, yang ditampilin cuman tanggalnya aja
        binding.txtTanggal.text = dateFormat.format(currentDate)

        //dapetin unix timestamp dari jam sekarang -> waktu details dan di list yang ditampilin sama menitnya juga
        val timestamp = System.currentTimeMillis()

        binding.btnAddExpense.setOnClickListener {
            var nominal = 0;

            if(binding.txtNominal.text?.isNotBlank() == true){
                nominal = binding.txtNominal.text.toString().toIntOrNull() ?: 0
            }

            if(binding.txtNotes.text?.isNotBlank() == true && nominal > 0 && listBudget.count() > 0){
                var notes = binding.txtNotes.text.toString()

                var expense = Expense(uuid, selectedBudget.idBudget, timestamp, nominal, notes)

                //expense berupa list dimasukin berupa list krn pake vararg
                viewModel.addExpense(expense, sisa_uang)
            }
            else{
                Toast.makeText(context,"Isi semua kolom! Nominal tidak boleh negatif!",Toast.LENGTH_SHORT).show()
            }
        }

        //buat spinner
        binding.budgetType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                //baca spinner sekarang
                selectedBudget = listBudget[position]

                val totalBudget = selectedBudget.nominal
                val formatCurrency = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
                formatCurrency.maximumFractionDigits = 0

                binding.txtTotalBudget.text = formatCurrency.format(totalBudget)
                binding.expenseProgress.max = totalBudget

                //cek sdk
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

    fun observeViewModel(){
        //baca list budget yang ada
        vmBudget.budgetLD.observe(viewLifecycleOwner, Observer {
            //tampung value dari it di arraylist
            listBudget = it as ArrayList<Budget>

            //perbarui data adapter secara terus menerus
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            //set value dari dropdown (sppinner)
            binding.budgetType.adapter = adapter
        });

        viewModel.totalPengeluaran.observe(viewLifecycleOwner, Observer {
            val formatCurrency = NumberFormat.getInstance(Locale("id","ID"))
            formatCurrency.maximumFractionDigits = 0
            binding.txtTotalExpense.text = "IDR ${formatCurrency.format(it)}"

            //set value dari expense progressnya seberapa
            binding.expenseProgress.progress = it
        });

        viewModel.sisaUang.observe(viewLifecycleOwner, Observer {
            //update di text input layout
            if (it != null) {
                sisa_uang = it
            }

            val formatCurrency = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            formatCurrency.maximumFractionDigits = 0
            val formatted = "IDR ${formatCurrency.format(sisa_uang)}"

            binding.txtNominal.hint = "Nominal ("+formatted+" left)";
        });

        viewModel.statusInsert.observe(viewLifecycleOwner, Observer {
            if(it==true){
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