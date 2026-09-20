package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.OrderEntity
import com.example.ui.components.FlatPackedDeliveryStatusCard
import com.example.ui.theme.Graphite
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.PaperDark
import com.example.ui.theme.PaperSheet

@Composable
fun OrderSuccessDialog(
    order: OrderEntity,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = PaperBackground,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, LineBorder, RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(InkPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = PaperSheet,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Originals Claimed",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkPrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Artwork summary box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(PaperSheet)
                        .border(1.dp, LineBorder, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Artworks: ${order.itemsSummary}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkPrimary
                        )
                        Text(
                            text = "Dispatching to: ${order.customerName}, ${order.address}",
                            fontSize = 12.sp,
                            color = Graphite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Flat-Packed Delivery Status Card
                FlatPackedDeliveryStatusCard(
                    orderId = order.orderId,
                    currentStatus = order.status,
                    customerName = order.customerName,
                    deliveryAddress = order.address
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = InkPrimary,
                        contentColor = PaperSheet
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("dismiss_order_success")
                ) {
                    Text(
                        text = "Back to Gallery Wall",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
