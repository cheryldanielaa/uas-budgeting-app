package com.anmpdev.uas_budgeting_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.anmpdev.uas_budgeting_app.model.Expense
import com.anmpdev.uas_budgeting_app.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListExpenseViewModel(application: Application):
    AndroidViewModel(application), CoroutineScope{
    //ini buat klo pake coroutine scope
    //nanti bljr lagi pala saya puyeng TT
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get()=job + Dispatchers.IO

    val expenseLD = MutableLiveData<List<Expense>>() //utk nampung list expense
    //utk cek apakah ada error waktu load data
    val expenseLoadErrorLD =MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>() //buat cek status msi load atau gak

    fun refresh(){
        //function utk refresh data
        loadingLD.value = true //pertama kali refresh pasti statusnya true
        expenseLoadErrorLD.value = false
        launch {
            val db = buildDb(getApplication())
            //code dibawah buat ambil semua data dr database
            //ini mksdnya value dr expense diisi dgn hasil query
            expenseLD.postValue(db.ExpenseDao().selectAllExpense())
            loadingLD.postValue(false) //set utk loadingnya false krn sudah stop
        }
    }
    //single object buat return hasil fetching
    val expenseData = MutableLiveData<Expense>()
    //id expense itu parameter expensenya buat ambil datanya
    //ditampilin di popup
    fun fetch(id:Int){
        launch {
            val db = buildDb(getApplication())
            //select to do fungsinya buat ambil single todo
            //dan return function tsb
            expenseData.postValue(db.ExpenseDao().selectExpense(id))
        }
    }
}