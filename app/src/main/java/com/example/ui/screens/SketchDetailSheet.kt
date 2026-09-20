package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.TapeBeige
import com.example.ui.theme.WaxRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SketchDetailSheet(
    sketch: SketchEntity,
    isInCart: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onAddToCart: (SketchEntity) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PaperBackground,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header bar with close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Original",
                        tint = InkPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "1-of-1 Original Sheet",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Graphite,
                        letterSpacing = 1.sp
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_sketch_detail")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = InkPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Artwork framed on paper with simulated masking tape
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PaperSheet)
                    .border(1.5.dp, LineBorder, RoundedCornerShape(8.dp))
            ) {
                SketchArtView(
                    sketch = sketch,
                    modifier = Modifier.matchParentSize()
                )

                // Masking tape decorative strip
                Box(
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .width(60.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(TapeBeige)
                        .border(1.dp, Color(0x33B4AA8C), RoundedCornerShape(2.dp))
                )

                if (sketch.isSold) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .border(1.5.dp, WaxRed, RoundedCornerShape(4.dp))
                            .background(PaperSheet)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Sold to a Collector",
                            color = WaxRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title & Details
            Text(
                text = sketch.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = InkPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = sketch.meta,
                fontSize = 14.sp,
                color = Graphite
            )

            if (sketch.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = sketch.description,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = InkPrimary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Flat-pack guarantee card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(PaperDark)
                    .border(1.dp, LineBorder, RoundedCornerShape(8.dp))
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = "Packaging",
                        tint = InkPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "Packed Flat — Never Rolled",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Sketches ship flat in a rigid mailer between heavy backing boards so graphite and ink never crease. No prints or reprints are made.",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = Graphite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Price for Original",
                        fontSize = 11.sp,
                        color = Graphite
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (sketch.originalPrice != null && sketch.originalPrice > sketch.price) {
                            Text(
                                text = "₹${sketch.originalPrice.toInt()}",
                                fontSize = 14.sp,
                                textDecoration = TextDecoration.LineThrough,
                                color = GraphiteLight,
                                modifier = Modifier.padding(end = 6.dp)
                            )
                        }
                        Text(
                            text = "₹${sketch.price.toInt()}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkPrimary
                        )
                    }
                }

                if (sketch.isSold) {
                    Button(
                        onClick = {},
                        enabled = false,
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = PaperDark,
                            disabledContentColor = GraphiteLight
                        )
                    ) {
                        Text("Sold Out")
                    }
                } else if (isInCart) {
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PaperDark,
                            contentColor = InkPrimary
                        )
                    ) {
                        Text("Already in Cart")
                    }
                } else {
                    Button(
                        onClick = {
                            onAddToCart(sketch)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = InkPrimary,
                            contentColor = PaperSheet
                        ),
                        modifier = Modifier.testTag("detail_add_to_cart")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Add to Cart",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
