package com.example

import com.example.ui.components.DeliveryStage
import org.junit.Assert.assertEquals
import org.junit.Test

class FlatPackedDeliveryTest {

    @Test
    fun `delivery stages map properly from status string`() {
        assertEquals(DeliveryStage.PROCESSING, DeliveryStage.fromString("Processing"))
        assertEquals(DeliveryStage.PROCESSING, DeliveryStage.fromString("packed flat"))
        assertEquals(DeliveryStage.PROCESSING, DeliveryStage.fromString("unknown"))

        assertEquals(DeliveryStage.IN_TRANSIT, DeliveryStage.fromString("In Transit"))
        assertEquals(DeliveryStage.IN_TRANSIT, DeliveryStage.fromString("transit"))
        assertEquals(DeliveryStage.IN_TRANSIT, DeliveryStage.fromString("shipped"))

        assertEquals(DeliveryStage.DELIVERED, DeliveryStage.fromString("Delivered"))
        assertEquals(DeliveryStage.DELIVERED, DeliveryStage.fromString("completed"))
        assertEquals(DeliveryStage.DELIVERED, DeliveryStage.fromString("arrived"))
    }

    @Test
    fun `delivery stage ordering and details are consistent`() {
        assertEquals(0, DeliveryStage.PROCESSING.stepIndex)
        assertEquals("Processing", DeliveryStage.PROCESSING.title)

        assertEquals(1, DeliveryStage.IN_TRANSIT.stepIndex)
        assertEquals("In Transit", DeliveryStage.IN_TRANSIT.title)

        assertEquals(2, DeliveryStage.DELIVERED.stepIndex)
        assertEquals("Delivered", DeliveryStage.DELIVERED.title)
    }
}
