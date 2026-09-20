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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.SketchEntity
import com.example.model.ChatMessage
import com.example.model.MessageSender
import com.example.ui.theme.Graphite
import com.example.ui.theme.GraphiteLight
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.PaperDark
import com.example.ui.theme.PaperSheet
import com.example.ui.theme.WaxRed

@Composable
fun AiChatDialog(
    messages: List<ChatMessage>,
    isThinking: Boolean,
    catalog: List<SketchEntity>,
    onDismiss: () -> Unit,
    onSendMessage: (String) -> Unit,
    onSelectSketch: (SketchEntity) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val sampleSuggestions = listOf(
        "Calm piece for a hallway",
        "Something with cats or animals",
        "Urban architectural sketch",
        "Under ₹350 budget",
        "Ink drawing of nature"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = PaperBackground,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 480.dp, max = 560.dp)
                .border(1.2.dp, InkPrimary, RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header
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
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(InkPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = PaperSheet,
                                modifier = Modifier.size(15.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Sketch Picker",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = InkPrimary
                            )
                            Text(
                                text = "Curator for one-of-a-kind originals",
                                fontSize = 11.sp,
                                color = Graphite
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("close_chat_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = InkPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Suggestion chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(sampleSuggestions) { suggestion ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(PaperSheet)
                                .border(0.8.dp, LineBorder, RoundedCornerShape(14.dp))
                                .clickable { onSendMessage(suggestion) }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = suggestion,
                                fontSize = 11.sp,
                                color = InkPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Messages list
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(messages, key = { it.id }) { msg ->
                        val isUser = msg.sender == MessageSender.USER
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 10.dp,
                                            topEnd = 10.dp,
                                            bottomStart = if (isUser) 10.dp else 2.dp,
                                            bottomEnd = if (isUser) 2.dp else 10.dp
                                        )
                                    )
                                    .background(if (isUser) InkPrimary else PaperDark)
                                    .border(
                                        1.dp,
                                        if (isUser) InkPrimary else LineBorder,
                                        RoundedCornerShape(
                                            topStart = 10.dp,
                                            topEnd = 10.dp,
                                            bottomStart = if (isUser) 10.dp else 2.dp,
                                            bottomEnd = if (isUser) 2.dp else 10.dp
                                        )
                                    )
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .fillMaxWidth(0.85f)
                            ) {
                                Text(
                                    text = msg.text,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = if (isUser) PaperSheet else InkPrimary
                                )
                            }

                            // If assistant linked a sketch recommendation
                            if (!isUser && msg.recommendedSketchId != null) {
                                val linked = catalog.find { it.id == msg.recommendedSketchId }
                                if (linked != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(PaperSheet)
                                            .border(1.dp, LineBorder, RoundedCornerShape(6.dp))
                                            .clickable {
                                                onSelectSketch(linked)
                                                onDismiss()
                                            }
                                            .padding(horizontal = 10.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = InkPrimary,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = "View \"${linked.title}\" (₹${linked.price.toInt()})",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = InkPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (isThinking) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp,
                                    color = InkPrimary
                                )
                                Text(
                                    text = "Consulting studio wall…",
                                    fontSize = 12.sp,
                                    color = Graphite
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Input row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Ask about mood, size, budget…", fontSize = 12.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InkPrimary,
                            unfocusedBorderColor = LineBorder,
                            focusedContainerColor = PaperSheet,
                            unfocusedContainerColor = PaperSheet
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input")
                    )

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(InkPrimary)
                            .clickable(enabled = inputText.isNotBlank() && !isThinking) {
                                val textToSend = inputText
                                inputText = ""
                                onSendMessage(textToSend)
                            }
                            .testTag("chat_send_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = PaperSheet,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
