package com.example.shopy.datalayer.localdatabase.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shopy.AppContextUtility
import com.example.shopy.datalayer.entity.WishItem


@Database(entities = [WishItem::class], version = 1,exportSchema = false)
abstract class RoomService : RoomDatabase() {
    companion object{
        @Volatile
        private var db : RoomService? =null

        fun getInstance(): RoomService? {
            synchronized(this) {
                if (db == null)
                    db = Room.databaseBuilder(
                        AppContextUtility.getAppContext(), RoomService::class.java, "WishListDataBase"
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            return db
        }

    }


    abstract fun wishDao(): WishDao

}