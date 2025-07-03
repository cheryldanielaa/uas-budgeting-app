package com.anmpdev.uas_budgeting_app.viewmodel

import android.app.Application
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

class ListExpenseViewModel(application: Application): AndroidViewModel(application), CoroutineScope{

    private var job = Job()
    val expenseLD = MutableLiveData<List<Expense>>() //utk nampung list expense
    val expenseLoadErrorLD =MutableLiveData<Boolean>()  //utk cek apakah ada error waktu load data
    val loadingLD = MutableLiveData<Boolean>() //buat cek status msi load atau gak
    val expenseData = MutableLiveData<Expense>() //single object buat return hasil fetching

    override val coroutineContext: CoroutineContext
        get()=job + Dispatchers.IO

    fun refresh(uuid:Int) {
        //function utk refresh data
        loadingLD.value = true //pertama kali refresh pasti statusnya true
        expenseLoadErrorLD.value = false
        launch {
            val db = buildDb(getApplication())
            //code dibawah buat ambil semua data dr database
            expenseLD.postValue(db.ExpenseDao().selectAllExpense(uuid))
            loadingLD.postValue(false)
        }
    }

    fun fetch(id:Int){
        launch {
            val db = buildDb(getApplication())
            expenseData.postValue(db.ExpenseDao().selectExpense(id))
        }
    }
}