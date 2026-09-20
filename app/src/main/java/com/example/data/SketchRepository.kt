package com.example.data

import kotlinx.coroutines.flow.Flow

class SketchRepository(private val sketchDao: SketchDao) {
    val allSketches: Flow<List<SketchEntity>> = sketchDao.getAllSketches()
    val cartItems: Flow<List<CartEntity>> = sketchDao.getCartItems()
    val allOrders: Flow<List<OrderEntity>> = sketchDao.getAllOrders()

    suspend fun getSketchById(id: String): SketchEntity? {
        return sketchDao.getSketchById(id)
    }

    suspend fun addSketch(sketch: SketchEntity) {
        sketchDao.insertSketch(sketch)
    }

    suspend fun ensureDefaultSketches() {
        sketchDao.insertSketches(DefaultSketches.initialSketches)
    }

    suspend fun addToCart(sketchId: String) {
        sketchDao.addToCart(CartEntity(sketchId = sketchId))
    }

    suspend fun removeFromCart(sketchId: String) {
        sketchDao.removeFromCart(sketchId)
    }

    suspend fun clearCart() {
        sketchDao.clearCart()
    }

    suspend fun checkout(
        customerName: String,
        address: String,
        cartSketches: List<SketchEntity>
    ): OrderEntity {
        val orderId = "TYS-" + (10000..99999).random()
        val total = cartSketches.sumOf { it.price }
        val titles = cartSketches.joinToString(", ") { it.title }
        val order = OrderEntity(
            orderId = orderId,
            itemsSummary = titles,
            totalAmount = total,
            customerName = customerName,
            address = address
        )
        sketchDao.insertOrder(order)
        sketchDao.markSketchesSold(cartSketches.map { it.id })
        sketchDao.clearCart()
        return order
    }
}
