package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SketchDao {
    @Query("SELECT * FROM sketches ORDER BY dateAdded DESC")
    fun getAllSketches(): Flow<List<SketchEntity>>

    @Query("SELECT * FROM sketches WHERE id = :id")
    suspend fun getSketchById(id: String): SketchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSketch(sketch: SketchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSketches(sketches: List<SketchEntity>)

    @Update
    suspend fun updateSketch(sketch: SketchEntity)

    @Query("UPDATE sketches SET isSold = 1 WHERE id IN (:sketchIds)")
    suspend fun markSketchesSold(sketchIds: List<String>)

    // Cart operations
    @Query("SELECT * FROM cart")
    fun getCartItems(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToCart(cartItem: CartEntity)

    @Query("DELETE FROM cart WHERE sketchId = :sketchId")
    suspend fun removeFromCart(sketchId: String)

    @Query("DELETE FROM cart")
    suspend fun clearCart()

    // Orders
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)
}
