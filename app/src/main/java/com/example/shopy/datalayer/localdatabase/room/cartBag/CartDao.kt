package com.example.shopy.datalayer.localdatabase.room.cartBag

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule

@Dao
interface CartDao {
    @Query("SELECT * FROM Cart LIMIT 4")
    fun getAllCartList(): LiveData<List<ProductCartModule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCartList(withItem: ProductCartModule)

    @Query("DELETE FROM Cart WHERE id LIKE:id")
    suspend fun deleteOnCartItem(id: Long)

}