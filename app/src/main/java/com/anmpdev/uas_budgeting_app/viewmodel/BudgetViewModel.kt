package com.anmpdev.uas_budgeting_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.anmpdev.uas_budgeting_app.model.Budget
import com.anmpdev.uas_budgeting_app.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BudgetViewModel (application: Application)
    : AndroidViewModel(application), CoroutineScope {
    private val job= Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val budgetLD = MutableLiveData<List<Budget>>()
    val budgetLoadErrorLD =MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    val budgetData = MutableLiveData<Budget>()

    fun readBudget(uuid:Int){
        launch{
            val db = buildDb(getApplication())
            budgetLD.postValue(db.BudgetDao().selectAllBudget(uuid))
        }
    }

    fun insertBudget(budget: Budget){
        launch{
            val db = buildDb(getApplication())
            db.BudgetDao().insertAll(budget)
        }
    }
    fun selectABudget(id:Int){
        launch{
            val db = buildDb(getApplication())
            //cari budget berdasarkan idnya
            budgetData.postValue(db.BudgetDao().selectNameBudget(id))
        }
    }

    fun editBudget(name:String, nominal:Int, id:Int){
        launch{
            val db = buildDb(getApplication())
            db.BudgetDao().updateBudget(name,nominal,id)
        }
    }

    fun refresh(uuid:Int){
        loadingLD.value = true
        budgetLoadErrorLD.value = false
        launch {
            val db = buildDb(getApplication())
            budgetLD.postValue(db.BudgetDao().selectAllBudget(uuid))
            loadingLD.postValue(false)
        }
    }

    fun fetch(id:Int){
        launch {
            val db = buildDb(getApplication())
            budgetData.postValue(db.BudgetDao().selectNameBudget(id))

        }
    }


}