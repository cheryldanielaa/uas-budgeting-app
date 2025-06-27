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
import com.anmpdev.uas_budgeting_app.model.Budget
import com.anmpdev.uas_budgeting_app.model.Expense
import com.anmpdev.uas_budgeting_app.viewmodel.BudgetViewModel
import com.anmpdev.uas_budgeting_app.viewmodel.CreateExpenseViewModel

class AddBudget : Fragment() {
    private lateinit var binding :FragmentAddBudgetBinding
    private lateinit var viewModel :BudgetViewModel
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddBudgetBinding.inflate (layoutInflater ,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("SETTING", Context.MODE_PRIVATE)
        val uuid = sharedPreferences.getInt("user_id", 0) //kasih default valuenya 0
        viewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)

        binding.btnAddBudget.setOnClickListener {
            val namaBudget =binding.txtBudgetName.text.toString()

            val nominal=binding.txtNominal.text.toString().toInt()
            if(nominal>0 && namaBudget!=""){
                var budgetBaru = Budget(uuid, namaBudget, nominal)
                viewModel.insertBudget(budgetBaru)
                Navigation.findNavController(requireView()).popBackStack()
            }
            else{
                Toast.makeText(context, "Nominal tidak boleh bernilai negatif", Toast.LENGTH_SHORT).show()
            }

        }


    }



}