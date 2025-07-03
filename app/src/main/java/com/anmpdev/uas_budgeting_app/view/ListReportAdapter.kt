package com.anmpdev.uas_budgeting_app.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anmpdev.uas_budgeting_app.databinding.ReportCardBinding
import com.anmpdev.uas_budgeting_app.model.Expense
import com.anmpdev.uas_budgeting_app.model.Report
import java.text.NumberFormat
import java.util.Locale

class ListReportAdapter (val reportList: ArrayList<Report>) : RecyclerView.Adapter<ListReportAdapter.ReportViewHolder>() {

    class ReportViewHolder(var binding: ReportCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReportCardBinding.inflate(inflater, parent, false)
        return ReportViewHolder(binding)
    }

    override fun getItemCount(): Int = reportList.size

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reportList[position]

        val name = report.name
        val totalBudget = report.nominal
        val totalExpense = report.used
        val budgetLeft = totalBudget - totalExpense

        holder.binding.txtBudgetName.text = name
        holder.binding.txtTotalBudget.text = "IDR ${formatRupiah(totalBudget)}"
        holder.binding.txtTotalExpense.text = "IDR ${formatRupiah(totalExpense)}"
        holder.binding.txtBudgetLeft.text = "Budget Left: IDR ${formatRupiah(budgetLeft)}"

        holder.binding.expenseProgress.max = totalBudget
        holder.binding.expenseProgress.progress = totalExpense
    }

    fun updateReportList(newList: List<Report>) {
        reportList.clear()
        reportList.addAll(newList)
        notifyDataSetChanged()
    }

    fun formatRupiah(value: Int): String {
        val formatter = NumberFormat.getInstance(Locale("in", "ID"))
        return formatter.format(value)
    }
}