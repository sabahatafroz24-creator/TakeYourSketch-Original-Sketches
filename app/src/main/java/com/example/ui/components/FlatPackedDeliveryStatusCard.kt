package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MarkunreadMailbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Graphite
import com.example.ui.theme.GraphiteLight
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.PaperDark
import com.example.ui.theme.PaperSheet
import com.example.ui.theme.WaxRed

enum class DeliveryStage(
    val stepIndex: Int,
    val title: String,
    val subtitle: String,
    val detailedStatus: String,
    val icon: ImageVector
) {
    PROCESSING(
        stepIndex = 0,
        title = "Processing",
        subtitle = "Archival Flat Packing",
        detailedStatus = "Artwork is being secured between rigid acid-free boards.",
        icon = Icons.Default.Inventory2
    ),
    IN_TRANSIT(
        stepIndex = 1,
        title = "In Transit",
        subtitle = "Flat Mailer on Route",
        detailedStatus = "Handed to priority courier in a reinforced do-not-bend envelope.",
        icon = Icons.Default.LocalShipping
    ),
    DELIVERED(
        stepIndex = 2,
        title = "Delivered",
        subtitle = "Arrived Ready to Frame",
        detailedStatus = "Delivered flat and uncreased to the collector's address.",
        icon = Icons.Default.DoneAll
    );

    companion object {
        fun fromString(status: String?): DeliveryStage {
            return when (status?.lowercase()?.trim()) {
                "in transit", "transit", "shipping", "shipped" -> IN_TRANSIT
                "delivered", "completed", "arrived" -> DELIVERED
                else -> PROCESSING
            }
        }
    }
}

/**
 * A status card component tracking the flat-packed delivery process for TYS Sketches.
 * Clearly visualizes steps: Processing -> In Transit -> Delivered.
 */
@Composable
fun FlatPackedDeliveryStatusCard(
    orderId: String,
    currentStatus: String = "Processing",
    customerName: String? = null,
    deliveryAddress: String? = null,
    allowInteractiveStepToggle: Boolean = true,
    modifier: Modifier = Modifier
) {
    var activeStage by remember(currentStatus) {
        mutableStateOf(DeliveryStage.fromString(currentStatus))
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = PaperSheet,
        modifier = modifier
            .fillMaxWidth()
            .border(1.2.dp, LineBorder, RoundedCornerShape(12.dp))
            .testTag("flat_packed_delivery_status_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Flat-Packed Delivery Badge & Order ID
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(InkPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MarkunreadMailbox,
                            contentDescription = "Flat-packed delivery icon",
                            tint = PaperSheet,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "FLAT-PACKED DELIVERY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Graphite,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Order #$orderId",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkPrimary
                        )
                    }
                }

                // Stage Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (activeStage) {
                                DeliveryStage.PROCESSING -> PaperDark
                                DeliveryStage.IN_TRANSIT -> WaxRed.copy(alpha = 0.12f)
                                DeliveryStage.DELIVERED -> Color(0xFFE8F5E9)
                            }
                        )
                        .border(
                            0.8.dp,
                            when (activeStage) {
                                DeliveryStage.PROCESSING -> LineBorder
                                DeliveryStage.IN_TRANSIT -> WaxRed.copy(alpha = 0.4f)
                                DeliveryStage.DELIVERED -> Color(0xFF81C784)
                            },
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = activeStage.title.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = when (activeStage) {
                            DeliveryStage.PROCESSING -> InkPrimary
                            DeliveryStage.IN_TRANSIT -> WaxRed
                            DeliveryStage.DELIVERED -> Color(0xFF2E7D32)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Stepper timeline
            val stages = DeliveryStage.values()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                stages.forEachIndexed { index, stage ->
                    val isReached = stage.stepIndex <= activeStage.stepIndex
                    val isCurrent = stage == activeStage

                    val circleColor by animateColorAsState(
                        targetValue = when {
                            isCurrent -> InkPrimary
                            isReached -> Color(0xFF2E7D32)
                            else -> PaperDark
                        },
                        animationSpec = tween(300),
                        label = "circleColor"
                    )

                    val contentColor by animateColorAsState(
                        targetValue = when {
                            isReached -> PaperSheet
                            else -> GraphiteLight
                        },
                        animationSpec = tween(300),
                        label = "contentColor"
                    )

                    // Step Node
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .testTag("step_${stage.name.lowercase()}")
                            .then(
                                if (allowInteractiveStepToggle) {
                                    Modifier.clickable { activeStage = stage }
                                } else Modifier
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(circleColor)
                                .border(
                                    width = if (isCurrent) 2.dp else 1.dp,
                                    color = if (isCurrent) InkPrimary else LineBorder,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isReached && !isCurrent) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "${stage.title} completed",
                                    tint = contentColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = stage.icon,
                                    contentDescription = stage.title,
                                    tint = contentColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = stage.title,
                            fontSize = 11.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                            color = if (isCurrent) InkPrimary else Graphite,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Connector line between nodes
                    if (index < stages.size - 1) {
                        val linePassed = index < activeStage.stepIndex
                        val lineColor by animateColorAsState(
                            targetValue = if (linePassed) Color(0xFF2E7D32) else LineBorder,
                            animationSpec = tween(300),
                            label = "lineColor"
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(2.5.dp)
                                .padding(horizontal = 4.dp)
                                .background(lineColor)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Active Stage Detailed Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(PaperBackground)
                    .border(0.8.dp, LineBorder, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Current Status:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Graphite
                        )
                        Text(
                            text = activeStage.subtitle,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkPrimary
                        )
                    }

                    Text(
                        text = activeStage.detailedStatus,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = InkPrimary
                    )

                    if (!deliveryAddress.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Destination: ${customerName?.let { "$it • " } ?: ""}$deliveryAddress",
                            fontSize = 11.sp,
                            color = Graphite,
                            lineHeight = 15.sp,
                            maxLines = 2
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Archival Flat-Packing Guarantee Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "📦",
                    fontSize = 12.sp
                )
                Text(
                    text = "Never rolled or folded. Shipped flat between 300gsm archival backing boards.",
                    fontSize = 10.5.sp,
                    color = Graphite,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
