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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.TysCollectiveMembers
import com.example.ui.theme.Graphite
import com.example.ui.theme.GraphiteLight
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.PaperDark
import com.example.ui.theme.PaperSheet

@Composable
fun MembersDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = PaperBackground,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.2.dp, LineBorder, RoundedCornerShape(10.dp))
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
                        text = "The TYS Collective",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkPrimary
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("close_members_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = InkPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Artist quote block
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(PaperSheet)
                        .border(1.dp, LineBorder, RoundedCornerShape(6.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "\"I keep every sketchbook page that isn't a throwaway. TYS is where the ones worth keeping find someone else's wall instead of a drawer.\"",
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                            lineHeight = 20.sp,
                            color = InkPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "— The artists behind TYS",
                            fontSize = 12.sp,
                            color = Graphite,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "STUDIO MEMBERS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Graphite,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Members List
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TysCollectiveMembers.forEach { member ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(PaperSheet)
                                .border(0.8.dp, LineBorder, RoundedCornerShape(6.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PaperDark)
                                    .border(1.dp, LineBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = InkPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = member.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = InkPrimary
                                )
                                Text(
                                    text = member.role,
                                    fontSize = 12.sp,
                                    color = Graphite
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // How buying works
                Text(
                    text = "HOW BUYING WORKS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Graphite,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    StepItem(
                        number = "1",
                        title = "Pick a sketch",
                        desc = "Everything shown is one-of-one. What's pictured is the actual physical page you receive."
                    )
                    StepItem(
                        number = "2",
                        title = "Packed flat",
                        desc = "Sketches ship in rigid mailers between backing boards, never rolled or folded."
                    )
                    StepItem(
                        number = "3",
                        title = "Yours to frame",
                        desc = "No reproduction licensing restrictions — hang it, gift it, or keep it."
                    )
                }
            }
        }
    }
}

@Composable
private fun StepItem(number: String, title: String, desc: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(InkPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = PaperSheet,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = InkPrimary
            )
            Text(
                text = desc,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Graphite
            )
        }
    }
}
