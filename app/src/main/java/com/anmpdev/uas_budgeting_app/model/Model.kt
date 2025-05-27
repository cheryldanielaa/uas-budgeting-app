package com.anmpdev.uas_budgeting_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//implement room biar bisa terhubung sama database
@Entity //buat bikin entity scr otomatis
data class User(
    @ColumnInfo(name="username")
    var username:String,
    @ColumnInfo(name="password")
    var password:String,
){
    //primary keynya auto generate >> each have unique id
    @PrimaryKey(autoGenerate = true)
    //initial valuenya 0, jadi id pertama adalah 0
    //trs nnti dia bakalan auto increment
    var uuid:Int=0 //dimisalkan idnya 0
}
@Entity
//data class budget
data class Budget(
    @ColumnInfo(name="uuid")
    var uuid: Int,
    @ColumnInfo(name="budget_name")
    var budget_name:String,
    @ColumnInfo(name="nominal")
    var nominal:Int,
){
    @PrimaryKey(autoGenerate = true)
    var idBudget:Int=0 //dimisalkan idnya 0
}

@Entity
//data class budget
data class Expense(
    @ColumnInfo(name="uuid")
    var uuid: Int,
    @ColumnInfo(name="idBudget")
    var idBudget: Int,
    @ColumnInfo(name="tanggal") //buat insert data, nanti dr spinner yg dipilih
    var tanggal:String, //tanggal transaksi
    @ColumnInfo(name="nominal")
    var nominal:Int,
    @ColumnInfo(name="notes")
    var notes:String,
){
    @PrimaryKey(autoGenerate = true)
    var idExpense:Int=0 //dimisalkan idnya 0
}
