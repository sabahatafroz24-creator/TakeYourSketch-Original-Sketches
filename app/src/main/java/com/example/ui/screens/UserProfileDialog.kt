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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.OrderEntity
import com.example.data.SessionEntity
import com.example.ui.components.FlatPackedDeliveryStatusCard
import com.example.ui.theme.Graphite
import com.example.ui.theme.GraphiteLight
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.PaperDark
import com.example.ui.theme.PaperSheet
import com.example.ui.theme.WaxRed

@Composable
fun UserProfileDialog(
    user: SessionEntity,
    orders: List<OrderEntity>,
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = PaperBackground,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.2.dp, LineBorder, RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Collector Account",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkPrimary
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("close_profile_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close dialog",
                            tint = InkPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Profile card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(PaperSheet)
                        .border(1.dp, LineBorder, RoundedCornerShape(8.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(InkPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.displayName.take(1).uppercase(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = PaperSheet
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.displayName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkPrimary
                        )
                        Text(
                            text = user.email,
                            fontSize = 12.sp,
                            color = Graphite
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Authenticated Member",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Order History
                Text(
                    text = "ORDER HISTORY (${orders.size})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Graphite,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (orders.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(PaperSheet)
                            .border(0.8.dp, LineBorder, RoundedCornerShape(6.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No orders yet. Any original sketch you acquire will be listed here.",
                            fontSize = 12.sp,
                            color = Graphite,
                            lineHeight = 16.sp
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        val latest = orders.first()
                        FlatPackedDeliveryStatusCard(
                            orderId = latest.orderId,
                            currentStatus = latest.status,
                            customerName = latest.customerName,
                            deliveryAddress = latest.address
                        )

                        orders.take(5).forEach { order ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PaperSheet)
                                    .border(0.8.dp, LineBorder, RoundedCornerShape(6.dp))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = order.orderId,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = InkPrimary
                                    )
                                    Text(
                                        text = order.itemsSummary,
                                        fontSize = 11.sp,
                                        color = Graphite,
                                        maxLines = 1
                                    )
                                }
                                Text(
                                    text = "₹${order.totalAmount.toInt()}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = InkPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Security Note
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(PaperDark)
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Graphite,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "User credentials are protected using salted PBKDF2 hashing with an encrypted session key.",
                            fontSize = 11.sp,
                            lineHeight = 14.sp,
                            color = Graphite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Log Out Button
                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("logout_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WaxRed.copy(alpha = 0.12f),
                        contentColor = WaxRed
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WaxRed.copy(alpha = 0.4f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Log Out",
                            modifier = Modifier.size(16.dp),
                            tint = WaxRed
                        )
                        Text(
                            text = "Log Out",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = WaxRed
                        )
                    }
                }
            }
        }
    }
}
