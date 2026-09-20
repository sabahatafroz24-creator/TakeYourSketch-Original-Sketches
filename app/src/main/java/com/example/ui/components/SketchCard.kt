package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SketchEntity
import com.example.ui.theme.Graphite
import com.example.ui.theme.GraphiteLight
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperSheet
import com.example.ui.theme.WaxRed

@Composable
fun SketchCard(
    sketch: SketchEntity,
    isInCart: Boolean,
    onSelect: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = PaperSheet),
        modifier = modifier
            .testTag("sketch_card_${sketch.id}")
            .border(1.2.dp, LineBorder, RoundedCornerShape(8.dp))
            .clickable { onSelect() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Artwork viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.25f)
                    .border(width = 0.8.dp, color = LineBorder)
            ) {
                SketchArtView(
                    sketch = sketch,
                    modifier = Modifier.matchParentSize()
                )

                // Sold indicator badge on top left
                if (sketch.isSold) {
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .border(1.dp, WaxRed, RoundedCornerShape(4.dp))
                            .background(PaperSheet.copy(alpha = 0.92f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Sold",
                            color = WaxRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                } else if (sketch.isCustomUpload) {
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(InkPrimary.copy(alpha = 0.85f))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Fresh Sheet",
                            color = PaperSheet,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Information block
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Text(
                    text = sketch.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = InkPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = sketch.meta,
                    fontSize = 12.sp,
                    color = Graphite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Price and action row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Price tag
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (sketch.originalPrice != null && sketch.originalPrice > sketch.price) {
                            Text(
                                text = "₹${sketch.originalPrice.toInt()}",
                                fontSize = 12.sp,
                                textDecoration = TextDecoration.LineThrough,
                                color = GraphiteLight,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                        Text(
                            text = "₹${sketch.price.toInt()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = InkPrimary
                        )
                    }

                    // Button
                    if (sketch.isSold) {
                        Box(
                            modifier = Modifier
                                .border(1.dp, LineBorder, RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Sold Out",
                                fontSize = 12.sp,
                                color = GraphiteLight,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else if (isInCart) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE8F0E8))
                                .border(1.dp, Color(0xFF5B8A5B), RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "In Cart",
                                    tint = Color(0xFF2E632E),
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "In Cart",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2E632E)
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .testTag("add_to_cart_${sketch.id}")
                                .clip(RoundedCornerShape(6.dp))
                                .border(1.dp, InkPrimary, RoundedCornerShape(6.dp))
                                .clickable { onAddToCart() }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = InkPrimary
                                )
                                Text(
                                    text = "Add to cart",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = InkPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
