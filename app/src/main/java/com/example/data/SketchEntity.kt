package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sketches")
data class SketchEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val meta: String,
    val price: Double,
    val originalPrice: Double? = null,
    val isSold: Boolean = false,
    val medium: String = "Original Sketch",
    val dimensions: String = "",
    val description: String = "",
    val customImageUri: String? = null,
    val isCustomUpload: Boolean = false,
    val drawingKey: String = "",
    val dateAdded: Long = System.currentTimeMillis()
)

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey
    val sketchId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val orderId: String,
    val itemsSummary: String,
    val totalAmount: Double,
    val customerName: String,
    val address: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Packed Flat"
)
