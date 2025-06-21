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

class SignInViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val loginResult = MutableLiveData<User?>()

    fun login(username: String, password: String) {
        launch {
            val db = buildDb(getApplication());
            val user = db.userDao().login(username, password)
            loginResult.postValue(user)
        }
    }
}
