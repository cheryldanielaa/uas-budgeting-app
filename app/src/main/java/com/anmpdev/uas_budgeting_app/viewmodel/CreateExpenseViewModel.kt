package com.anmpdev.uas_budgeting_app.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.anmpdev.uas_budgeting_app.model.Budget
import com.anmpdev.uas_budgeting_app.model.Expense
import com.anmpdev.uas_budgeting_app.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CreateExpenseViewModel(application: Application)
    : AndroidViewModel(application), CoroutineScope {
    private val job=Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    //ini fungsinya utk menambahkan expense baru
    fun addExpense(expense: Expense, sisa_uang:Int){
        launch {
            val db = buildDb(getApplication());
            //jika nominal yang diinput >= sisa uang, maka insert
            //jika tdk exception
            if(expense.nominal<=sisa_uang){
                statusInsert.postValue(true)
                db.ExpenseDao().insertAll(expense)
            }
            else{
                //kirim gagal klo gak memenuhi
                statusInsert.postValue(false)
            }
        }
    }
    //tampung total pengeluaran utk idbudget tertentu
    val totalPengeluaran = MutableLiveData<Int>()
    //nanti direturn postvavlue disini sekaliaaann TT
    val sisaUang = MutableLiveData<Int?>()
    val statusInsert=MutableLiveData<Boolean>(); //true or false
    fun fetchNominal(id:Int){
        launch {
            val db = buildDb(getApplication())
            //klo dia blm ada, maka datanya 0 expensenya
            val nominalPengeluaran = db.ExpenseDao().selectNominalBudget(id)?: 0
            totalPengeluaran.postValue(nominalPengeluaran)
            //lakukan perhitungan disinii
            val budget = db.BudgetDao().selectNameBudget(id)?.nominal
            val sisa_uang = budget?.minus(nominalPengeluaran)
            //kirim value
            sisaUang.postValue(sisa_uang)
            //buat debugging
            //Log.d("NOMINAL",nominalPengeluaran.toString())
        }
    }
}