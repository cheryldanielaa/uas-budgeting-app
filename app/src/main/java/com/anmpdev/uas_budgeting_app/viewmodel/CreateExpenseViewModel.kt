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

class CreateExpenseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job=Job()
    //tampung total pengeluaran utk idbudget tertentu
    val totalPengeluaran = MutableLiveData<Int>()
    val sisaUang = MutableLiveData<Int?>()
    val statusInsert=MutableLiveData<Boolean>();

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun addExpense(expense: Expense, sisa_uang:Int){
        launch {
            val db = buildDb(getApplication());

            //hanya bisa diinsert jika nominal yang diinput >= sisa uang
            if(expense.nominal <= sisa_uang){
                statusInsert.postValue(true)
                db.ExpenseDao().insertAll(expense)
            }
            else{
                statusInsert.postValue(false)
            }
        }
    }

    fun fetchNominal(id:Int){
        launch {
            val db = buildDb(getApplication())
            //klo dia blm ada, maka datanya 0 expensenya
            val nominalPengeluaran = db.ExpenseDao().selectNominalBudget(id)?: 0
            totalPengeluaran.postValue(nominalPengeluaran)

            //hitung sisa uang per budget
            val budget = db.BudgetDao().selectNameBudget(id)?.nominal
            //hitung selisih nominal budget dengan nominal pengeluaran budget itu
            val sisa_uang = budget?.minus(nominalPengeluaran)
            sisaUang.postValue(sisa_uang)
        }
    }
}