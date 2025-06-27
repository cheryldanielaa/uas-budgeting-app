package com.anmpdev.uas_budgeting_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.anmpdev.uas_budgeting_app.model.User
import com.anmpdev.uas_budgeting_app.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val statusInsert = MutableLiveData<Boolean>()

    fun insertUser(user: User) {
        launch {
            val db = buildDb(getApplication());
            val existing = db.userDao().getUserByUsername(user.username)

            if (existing == null) {
                db.userDao().insertUser(user)
                statusInsert.postValue(true)
            } else {
                statusInsert.postValue(false)
            }
        }
    }
}

