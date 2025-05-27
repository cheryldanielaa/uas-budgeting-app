package com.anmpdev.uas_budgeting_app.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
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
    fun addExpense(list:List<Expense>){
        launch {
            val db = buildDb(getApplication())
            db.ExpenseDao().insertAll(*list.toTypedArray())
        }
    }

    //read expense ini buat debugging doang sih
    fun readExpense(){
        launch {
            val db = buildDb(getApplication())
            val expenses = db.ExpenseDao().selectAllExpense()
            //coba logcat
            expenses.forEach{
                Log.d("DB_CHECK",it.toString())
            }
        }
    }
}