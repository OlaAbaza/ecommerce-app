package com.example.shopy.datalayer.localdatabase.room.wishBag

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopy.datalayer.entity.itemPojo.Product

@Dao
interface WishDao {

    @Query("SELECT * FROM wishList LIMIT 4")
    fun getAllWishList(): LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWishList(withItem: Product)

    @Query("DELETE FROM wishList WHERE id LIKE:id")
    suspend fun deleteOneWithItem(id: Long)



    @Query("SELECT * FROM wishList WHERE id LIKE:id LIMIT 1")
    fun getOneWithItem(id: Long) : LiveData<Product>
}