package com.anmpdev.uas_budgeting_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.anmpdev.uas_budgeting_app.model.Expense
import com.anmpdev.uas_budgeting_app.model.Report
import com.anmpdev.uas_budgeting_app.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ReportViewModel (application: Application): AndroidViewModel(application), CoroutineScope {
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val reportLD = MutableLiveData<List<Report>>() //utk nampung list expense
    //utk cek apakah ada error waktu load data
    val reportLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>() //buat cek status msi load atau gak

    //terima id dari user tsb
    fun refresh(uuid:Int){
        //function utk refresh data
        loadingLD.value = true //pertama kali refresh pasti statusnya true
        reportLoadErrorLD.value = false
        launch {
            val db = buildDb(getApplication())
            reportLD.postValue(db.ExpenseDao().getReportByUser(uuid))
            loadingLD.postValue(false) //set utk loadingnya false krn sudah stop
        }
    }
}