package com.anmpdev.uas_budgeting_app.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.anmpdev.uas_budgeting_app.R
import com.anmpdev.uas_budgeting_app.databinding.FragmentAddBudgetBinding
import com.anmpdev.uas_budgeting_app.databinding.FragmentEditBudgetBinding
import com.anmpdev.uas_budgeting_app.model.Budget
import com.anmpdev.uas_budgeting_app.viewmodel.BudgetViewModel
import com.anmpdev.uas_budgeting_app.viewmodel.CreateExpenseViewModel

class EditBudget : Fragment() {
    private lateinit var binding : FragmentEditBudgetBinding
    private lateinit var viewModelBudget : BudgetViewModel
    private lateinit var viewModelExpense : CreateExpenseViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentEditBudgetBinding.inflate (layoutInflater ,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("SETTING", Context.MODE_PRIVATE)
        val uuid = sharedPreferences.getInt("user_id", 0) //kasih default valuenya 0
        viewModelBudget = ViewModelProvider(this).get(BudgetViewModel::class.java)
        viewModelExpense = ViewModelProvider(this).get(CreateExpenseViewModel::class.java)
        //baca id dr argument yang diikirim dari navigation
        val idBudget = EditBudgetArgs.fromBundle(requireArguments()).idBudget

        viewModelBudget.selectABudget(idBudget)
        viewModelBudget.budgetData.observe(viewLifecycleOwner) { value ->
            binding.txtBudgetName.setText(value.budget_name.toString())
            binding.txtNominal.setText(value.nominal.toString())
        }
        //Panggil fetch nominal buat dapetin total expenseskrng, idinya diisi id budget
        viewModelExpense.fetchNominal(idBudget)
        var totalPengeluaran = 0

        //observe salah satu return yaitu total pengeluaran, simpan di var global
        viewModelExpense.totalPengeluaran.observe(viewLifecycleOwner) { value ->
            totalPengeluaran = value
        }
        binding.btnEditBudget.setOnClickListener {
            val namaBudget =binding.txtBudgetName.text.toString()
            val nominal=binding.txtNominal.text.toString().toInt()
            //cek dulu apakah expense lebih kecil dr budget baru, ya maka
            if(totalPengeluaran<=nominal){
                if(nominal>0 && namaBudget!=""){
//                    val budgetBaru = Budget(namaBudget, nominal)
                    viewModelBudget.editBudget(namaBudget,nominal, idBudget)
                    Navigation.findNavController(requireView()).popBackStack()
                }
                else{
                    Toast.makeText(context, "Nominal tidak boleh bernilai negatif", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(context, "Nominal tidak boleh lebih kecil dari $totalPengeluaran", Toast.LENGTH_SHORT).show()
            }


        }


    }



}