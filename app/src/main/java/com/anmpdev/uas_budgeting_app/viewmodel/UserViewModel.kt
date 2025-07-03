package com.anmpdev.uas_budgeting_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.anmpdev.uas_budgeting_app.model.User
import com.anmpdev.uas_budgeting_app.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UserViewModel (application: Application) :
    AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    val userLD = MutableLiveData<User?>()
    val changeSuccess = MutableLiveData<Boolean>()

    fun readUser(username: String) {
        launch {
            val db = buildDb(getApplication())
            val user = db.userDao().getUserByUsername(username)
            userLD.postValue(user)
        }
    }

    fun changePassword(username: String, oldPassword: String, newPassword: String) {
        launch {
            val db = buildDb(getApplication())
            val user = db.userDao().login(username, oldPassword)
            if (user != null) {
                db.userDao().updatePassword(username, newPassword)
                changeSuccess.postValue(true)
            } else {
                changeSuccess.postValue(false)
            }
        }
    }

}