package com.example.shopy.datalayer.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WishItem(
    @PrimaryKey val id: Long,
    val image: String?,
    val title: String?,
    val description: String?
)
