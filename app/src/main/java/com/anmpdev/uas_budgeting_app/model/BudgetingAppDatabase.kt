package com.anmpdev.uas_budgeting_app.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anmpdev.uas_budgeting_app.util.DB_NAME

//tulis semua entity disini
@Database(entities = [Budget::class, User::class, Expense::class], version = 1)
abstract class BudgetingAppDatabase:RoomDatabase(){
    abstract fun userDao():UserDao
    abstract fun BudgetDao():BudgetDao
    abstract fun ExpenseDao():ExpenseDao
    companion object{
        //buat singleton object untuk menjamin hanya ada 1 objek
        @Volatile private var instance:BudgetingAppDatabase?=null
        private val LOCK = Any()
        fun buildDatabase(context: Context)=
        //sekarang kita buat supaya build databasenya
            //sifatnya dinamis, jd jgn di hardcode
            Room.databaseBuilder(
                context.applicationContext,
                BudgetingAppDatabase::class.java,
                DB_NAME).build()
        operator fun invoke(context: Context){
            if(instance==null){
                synchronized(LOCK){
                    instance?: buildDatabase(context).also{
                        instance=it
                    }
                }
            }
        }
    }
}