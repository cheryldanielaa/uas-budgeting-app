package com.anmpdev.uas_budgeting_app.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.anmpdev.uas_budgeting_app.databinding.BudgetCardBinding
import com.anmpdev.uas_budgeting_app.model.Budget
import com.anmpdev.uas_budgeting_app.model.Expense
import com.anmpdev.uas_budgeting_app.view.ListExpenseAdapter.ListExpenseViewHolder
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListBudgetingAdapter(val budgetList:ArrayList<Budget>):
    RecyclerView.Adapter<ListBudgetingAdapter.ListBudgetViewHolder>(){
    //ini mksdnya adalah buat ui dari cardnya sesuai sama expense card binding
    class ListBudgetViewHolder(var binding: BudgetCardBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListBudgetViewHolder {
        var binding = BudgetCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListBudgetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return budgetList.count()
    }

    override fun onBindViewHolder(holder: ListBudgetViewHolder, position: Int) {
        holder.binding.txtName.text=budgetList[position].budget_name
        val formatCurrency = NumberFormat.getInstance(Locale("id","ID"))
        formatCurrency.maximumFractionDigits = 0
        val nominal = budgetList[position].nominal
        holder.binding.txtNominal.text = "IDR ${formatCurrency.format(nominal)}"
        //klo misal klik harganya
        holder.binding.txtName.setOnClickListener {
            val action = ListBudgetingFragmentDirections.actionToEditBudget(budgetList[position].idBudget)
            Navigation.findNavController(it).navigate(action)
        }
    }

    fun updateListBudget(newListBudget: List<Budget>){
        budgetList.clear()
        budgetList.addAll(newListBudget)
        notifyDataSetChanged()
    }

}