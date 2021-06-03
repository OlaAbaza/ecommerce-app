package com.example.shopy.datalayer.localdatabase.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shopy.AppContextUtility
import com.example.shopy.datalayer.entity.itemPojo.Product

@TypeConverters(Converter::class)
@Database(entities = [Product::class], version = 1,exportSchema = false)
abstract class RoomService : RoomDatabase() {
    companion object{
        @Volatile
        private var db : RoomService? =null

        fun getInstance(application: Application): RoomService? {
            synchronized(this) {
                if (db == null)
                    db = Room.databaseBuilder(
                        application, RoomService::class.java, "WishListDataBase"
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            return db
        }

    }


    abstract fun wishDao(): WishDao

}