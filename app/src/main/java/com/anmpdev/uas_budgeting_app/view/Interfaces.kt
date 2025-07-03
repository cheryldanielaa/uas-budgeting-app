package com.anmpdev.uas_budgeting_app.view

import android.view.View


interface ProfileActionListener {
    fun onSignOutClick(v: View)
    fun onChangePasswordClick(v: View)
    fun loadUserData()
}