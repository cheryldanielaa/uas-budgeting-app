package com.anmpdev.uas_budgeting_app.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.anmpdev.uas_budgeting_app.databinding.ExpenseCardBinding
import com.anmpdev.uas_budgeting_app.model.Budget
import com.anmpdev.uas_budgeting_app.model.Expense
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//terima parameter berupa list expense
class ListExpenseAdapter(val expenseList:ArrayList<Expense>,
    val budgetList:ArrayList<Budget>):
RecyclerView.Adapter<ListExpenseAdapter.ListExpenseViewHolder>(){
    //ini mksdnya adalah buat ui dari cardnya sesuai sama expense card binding
    class ListExpenseViewHolder(var binding:ExpenseCardBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListExpenseViewHolder {
       //atur binding disini
        var binding = ExpenseCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListExpenseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return expenseList.count()
    }

    override fun onBindViewHolder(holder: ListExpenseViewHolder, position: Int) {
        //tampung value dari tanggal yang dibaca disini
        //unix timestamp pake current time millis jadi hrs dikali 1000
        val timestamp = expenseList[position].tanggal
        //convert ke tanggal biasa disini
        val tanggal = Date(timestamp)
        //hh:mm a >> biar formatnya am pm, trs hh itu buat format 12 hour, klo HH format 24 hour
        //locale id biar pake bahasa indo formatnya
        val dateFormat = SimpleDateFormat("dd MMM yyyy hh:mm a",  Locale("id", "ID"))
        val str_tanggal = dateFormat.format(tanggal)
        //atur apa yang terjadi di tiap card disini
        holder.binding.txtTanggal.text=str_tanggal.toString()
        //buat format penulisan angka (currency)
        val formatCurrency = NumberFormat.getCurrencyInstance(Locale("id","ID"))
        formatCurrency.maximumFractionDigits = 0  //biar tdk ada desimal di belakangnya jadi gak ada kayak 1.000,00
        val nominal = expenseList[position].nominal
        holder.binding.txtHarga.text = formatCurrency.format(nominal) //ubah ke format Rp

        //klo misal klik harganya
        holder.binding.txtHarga.setOnClickListener {
            //klo diklik btn kategorinya auto kebuka navnya
            val action = ListExpensesFragmentDirections.actionDetailExpenses(expenseList[position].idExpense)
            Navigation.findNavController(it).navigate(action) //pergila ke action itu
        }

        //cari objek dimana idbudget dr budget list = idbugdet di expense list
        //buat tiap data, klo ada maka print budget name, klo gak ada unknown
        val matchedBudget = budgetList.find { it.idBudget == expenseList[position].idBudget }
        holder.binding.chipExpenseCategory.text = matchedBudget?.budget_name ?: "Unknown"
    }

    fun updateListExpense(newListExpense:List<Expense>){
        expenseList.clear()
        expenseList.addAll(newListExpense) //tambahin yang baru
        notifyDataSetChanged() //kasih tau klo ada perubahan data
    }

    fun updateListBudget(newListBudget:List<Budget>){
        budgetList.clear()
        budgetList.addAll(newListBudget) //tambahin yang baru
        notifyDataSetChanged()
    }
}