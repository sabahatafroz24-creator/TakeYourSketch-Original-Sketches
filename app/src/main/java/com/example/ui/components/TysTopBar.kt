package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SessionEntity
import com.example.ui.theme.Graphite
import com.example.ui.theme.GraphiteLight
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.PaperDark
import com.example.ui.theme.PaperSheet
import com.example.ui.theme.WaxRed

@Composable
fun TysTopBar(
    cartCount: Int,
    currentUser: SessionEntity? = null,
    onOpenAuth: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenUpload: () -> Unit,
    onOpenMembers: () -> Unit,
    onOpenCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = PaperBackground.copy(alpha = 0.96f),
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .border(width = 1.dp, color = LineBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.testTag("brand_logo")
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(InkPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "TYS Logo",
                        tint = Color(0xFFF1EDE1),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "TYS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = InkPrimary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "TAKE YOUR SKETCH",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp,
                        color = Graphite
                    )
                }
            }

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Auth / Profile Button
                if (currentUser != null) {
                    Box(
                        modifier = Modifier
                            .testTag("user_profile_button")
                            .clip(RoundedCornerShape(6.dp))
                            .background(PaperDark)
                            .border(1.dp, LineBorder, RoundedCornerShape(6.dp))
                            .clickable { onOpenProfile() }
                            .padding(horizontal = 9.dp, vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(InkPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentUser.displayName.take(1).uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PaperSheet
                                )
                            }
                            Text(
                                text = currentUser.displayName.split(" ").firstOrNull() ?: currentUser.displayName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = InkPrimary,
                                maxLines = 1
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .testTag("sign_in_button")
                            .clip(RoundedCornerShape(6.dp))
                            .border(1.dp, LineBorder, RoundedCornerShape(6.dp))
                            .clickable { onOpenAuth() }
                            .padding(horizontal = 9.dp, vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Sign In",
                                modifier = Modifier.size(14.dp),
                                tint = InkPrimary
                            )
                            Text(
                                text = "Sign In",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = InkPrimary
                            )
                        }
                    }
                }

                // Upload button
                Box(
                    modifier = Modifier
                        .testTag("upload_button")
                        .clip(RoundedCornerShape(6.dp))
                        .border(1.dp, GraphiteLight, RoundedCornerShape(6.dp))
                        .clickable { onOpenUpload() }
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Artist Upload",
                            modifier = Modifier.size(13.dp),
                            tint = InkPrimary
                        )
                        Text(
                            text = "Upload",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = InkPrimary
                        )
                    }
                }

                // Members button
                Box(
                    modifier = Modifier
                        .testTag("members_button")
                        .clip(RoundedCornerShape(6.dp))
                        .border(1.dp, GraphiteLight, RoundedCornerShape(6.dp))
                        .clickable { onOpenMembers() }
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = "Collective Members",
                            modifier = Modifier.size(13.dp),
                            tint = InkPrimary
                        )
                        Text(
                            text = "Members",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = InkPrimary
                        )
                    }
                }

                // Cart button with badge
                Box(
                    modifier = Modifier
                        .testTag("cart_button")
                        .clip(RoundedCornerShape(6.dp))
                        .background(InkPrimary)
                        .clickable { onOpenCart() }
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Shopping Cart",
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFFF1EDE1)
                        )
                        Text(
                            text = "Cart",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF1EDE1)
                        )
                        if (cartCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(17.dp)
                                    .clip(CircleShape)
                                    .background(WaxRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cartCount.toString(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
