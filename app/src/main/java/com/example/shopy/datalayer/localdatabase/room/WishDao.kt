package com.example.shopy.datalayer.localdatabase.room

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopy.datalayer.entity.itemPojo.Product

interface WishDao {

    @Query("SELECT * FROM wishList LIMIT 4")
    fun getAllWishList(): LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWishList(withItem: Product)

    @Query("DELETE FROM wishList")
    suspend fun deleteOneWithItem(withItem: Product)
}