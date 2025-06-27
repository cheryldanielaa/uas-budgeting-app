package com.anmpdev.uas_budgeting_app.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BudgetDao {
    //data dummy gue
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg budget: Budget) //insert all nama methodnya

    @Query("SELECT * FROM budget WHERE uuid = :uuid")
    fun selectAllBudget(uuid: Int): List<Budget>

    @Query("SELECT * from budget where budget_name=:name")
    fun selectidBudget(name:String):Budget?

    @Query("SELECT * from budget where idBudget=:id")
    fun selectNameBudget(id:Int):Budget?

    @Query("Update budget set budget_name=:name, nominal=:nominal where idBudget =:id")
    fun updateBudget(name:String, nominal:Int, id: Int)
}