package com.anmpdev.uas_budgeting_app.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anmpdev.uas_budgeting_app.util.DB_NAME

class UserDatabase {
@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var instance:UserDatabase?=null
        private val LOCK = Any()
        fun buildDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java,
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

}