package com.anmpdev.uas_budgeting_app.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.anmpdev.uas_budgeting_app.R
import com.anmpdev.uas_budgeting_app.databinding.FragmentListBudgetingBinding
import com.anmpdev.uas_budgeting_app.databinding.FragmentListExpensesBinding
import com.anmpdev.uas_budgeting_app.viewmodel.BudgetViewModel
import com.anmpdev.uas_budgeting_app.viewmodel.ListExpenseViewModel


class ListBudgetingFragment : Fragment() {
    private lateinit var binding: FragmentListBudgetingBinding
    private lateinit var viewModel: BudgetViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private var budgetListadapter = ListBudgetingAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBudgetingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("SETTING", Context.MODE_PRIVATE)
        val uuid = sharedPreferences.getInt("user_id", 0)
        viewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)
        viewModel.refresh(uuid) //kalau refresh itu untuk loadError juga
        binding.recView.layoutManager = LinearLayoutManager(context)
        binding.recView.adapter = budgetListadapter

        //klo misal diklik fabnya, maka dia akan pindah ke create expense (fragment)
        binding.btnAddBudget.setOnClickListener {
            //arahkan ke fragment tsb pke navigation
            //Toast.makeText(context, "Nominal tidak boleh bernilai negatif", Toast.LENGTH_SHORT).show()
            val action = ListBudgetingFragmentDirections.actionToAddBudget()
            Navigation.findNavController(it).navigate(action)
        }
        observeViewModel()

    }
    fun observeViewModel(){
        viewModel.budgetLD.observe(viewLifecycleOwner, Observer {
            budgetListadapter.updateListBudget(it)
            if(it.isEmpty()){
                binding.recView?.visibility = View.GONE
                binding.txtError.text = "Belum ada Budget yang tersimpan"
            }
            else{
                binding.recView?.visibility = View.VISIBLE
            }
        })

        //atur loading ld
        viewModel.loadingLD.observe(viewLifecycleOwner,Observer{
            if(it==false){
                binding.progressLoad.visibility = View.GONE
            }
            else{
                binding.progressLoad.visibility = View.VISIBLE
            }
        })

        //atur error ldnya
        viewModel.budgetLoadErrorLD.observe(viewLifecycleOwner,Observer{
            if(it==false){
                binding.txtError.visibility = View.GONE
            }
            else{
                binding.txtError.visibility = View.VISIBLE
            }
        });

        //perbarui adapter utk data dr budget ld
        viewModel.budgetLD.observe(viewLifecycleOwner, Observer {
            budgetListadapter.updateListBudget(it)
        })
    }


}