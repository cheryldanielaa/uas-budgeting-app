package com.anmpdev.uas_budgeting_app.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BudgetDao {
    //data dummy gue
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg budget: Budget) //insert all nama methodnya
    @Query("SELECT * from budget")
    fun selectAllBudget():List<Budget>

    @Query("SELECT * from budget where budget_name=:name")
    fun selectidBudget(name:String):Budget?

    @Query("SELECT * from budget where idBudget=:id")
    fun selectNameBudget(id:Int):Budget?
}