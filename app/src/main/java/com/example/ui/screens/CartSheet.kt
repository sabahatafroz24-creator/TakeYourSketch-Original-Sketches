package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SketchEntity
import com.example.ui.components.SketchArtView
import com.example.ui.theme.Graphite
import com.example.ui.theme.GraphiteLight
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.PaperDark
import com.example.ui.theme.PaperSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartSheet(
    cartSketches: List<SketchEntity>,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onRemove: (String) -> Unit,
    onProceedToCheckout: () -> Unit
) {
    val totalAmount = cartSketches.sumOf { it.price }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PaperBackground,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Cart",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkPrimary
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_cart")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Cart",
                        tint = InkPrimary
                    )
                }
            }

            HorizontalDivider(color = LineBorder, thickness = 1.dp)

            Spacer(modifier = Modifier.height(14.dp))

            if (cartSketches.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Your cart is empty.",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = InkPrimary
                        )
                        Text(
                            text = "Original sketches go quickly — take a look at what's currently on the wall.",
                            fontSize = 13.sp,
                            color = Graphite,
                            modifier = Modifier.padding(horizontal = 24.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(cartSketches, key = { it.id }) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(PaperSheet)
                                .border(1.dp, LineBorder, RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Thumbnail
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(0.8.dp, LineBorder, RoundedCornerShape(4.dp))
                            ) {
                                SketchArtView(
                                    sketch = item,
                                    modifier = Modifier.matchParentSize()
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Details
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = InkPrimary
                                )
                                Text(
                                    text = "₹${item.price.toInt()}",
                                    fontSize = 14.sp,
                                    color = Graphite,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            // Remove
                            IconButton(
                                onClick = { onRemove(item.id) },
                                modifier = Modifier.testTag("remove_item_${item.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Remove",
                                    tint = GraphiteLight
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Packaging notice
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(PaperDark)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = InkPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Packed flat in rigid mailers between acid-free backing boards.",
                        fontSize = 11.sp,
                        color = Graphite
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Total and Checkout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total (${cartSketches.size} original${if (cartSketches.size > 1) "s" else ""})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Graphite
                    )
                    Text(
                        text = "₹${totalAmount.toInt()}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkPrimary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onProceedToCheckout,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = InkPrimary,
                        contentColor = PaperSheet
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_button")
                ) {
                    Text(
                        text = "Proceed to Flat-Pack Delivery",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
