package com.anmpdev.uas_budgeting_app.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao //dao ini dibuat utk declare hubungan dengan query
interface ExpenseDao {
    /*insert itu perintahnya, onconflict strategy replace itu
    maksudnya klo ad duplicate PK, maka data lama direplace dgn
    data yang baru*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg expense:Expense) //insert all nama methodnya

    //baca semua data expense, urutkan dari yang terkecil ke terbesar (paling baru paling atas)
    //cari juga berdasarkan uuid tertentu
    //filteringnya nnti trakhir aaja
    @Query("SELECT * from expense  where uuid=:uuid order by tanggal DESC")
    fun selectAllExpense(uuid:Int):List<Expense> //return berupa list

    //baca 1 data expense berdasarkan id yang dipilih
    @Query("SELECT * from expense where idExpense=:idExpense")
    fun selectExpense(idExpense:Int):Expense //expense itu type data yg direturn

    //baca total nominalnya berapa
    @Query("SELECT sum (nominal) from expense where idBudget=:idBudget")
    fun selectNominalBudget(idBudget: Int):Int? //return berupa nominal, nullable

    @Query("SELECT b.budget_name as name, b.nominal as nominal, SUM(e.nominal) as used FROM budget b LEFT JOIN expense e ON b.idBudget = e.idBudget WHERE b.uuid = :userId GROUP BY b.idbudget")
    fun getReportByUser(userId: Int): List<Report>
}