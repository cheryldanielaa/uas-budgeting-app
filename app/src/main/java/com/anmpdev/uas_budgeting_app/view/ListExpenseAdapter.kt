package com.anmpdev.uas_budgeting_app.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.anmpdev.uas_budgeting_app.databinding.ExpenseCardBinding
import com.anmpdev.uas_budgeting_app.model.Expense
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//terima parameter berupa list expense
class ListExpenseAdapter (val expenseList:ArrayList<Expense>):
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

        //klo misal klik maka arahin ke detail expense
        holder.binding.btnCategoryExpenses.setOnClickListener {
            //klo diklik btn kategorinya auto kebuka navnya
            val action = ListExpensesFragmentDirections.actionDetailExpenses(expenseList[position].idExpense)
            Navigation.findNavController(it).navigate(action) //pergila ke action itu
        }
    }

    fun updateListExpense(newListExpense:List<Expense>){
        //baca todolist yg skrg trus add yang baru
        expenseList.clear()
        expenseList.addAll(newListExpense) //tambahin yang baru
        notifyDataSetChanged() //kasih tau klo ada perubahan data
    }
}