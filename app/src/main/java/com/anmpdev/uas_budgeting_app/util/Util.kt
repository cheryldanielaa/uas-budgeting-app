package com.anmpdev.uas_budgeting_app.util

import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anmpdev.uas_budgeting_app.model.BudgetingAppDatabase

val DB_NAME="budgetingdb"
//harus dikasih context
fun buildDb(context: Context): BudgetingAppDatabase {
    val db = BudgetingAppDatabase.buildDatabase(context)
    return db
}