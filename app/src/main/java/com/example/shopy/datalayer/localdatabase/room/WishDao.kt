package com.example.shopy.datalayer.localdatabase.room

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopy.datalayer.entity.WishItem

interface WishDao {
    @Query("SELECT * FROM WishItem LIMIT 4")
    fun getAllWishList(): LiveData<List<WishItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWishList(withItem: WishItem)

    @Query("DELETE FROM WishItem")
    suspend fun deleteOneWithItem(withItem: WishItem)
}