package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.ui.theme.Graphite
import com.example.ui.theme.GraphiteLight
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.PaperDark
import com.example.ui.theme.PaperSheet
import com.example.ui.theme.WaxRed

private const val ARTIST_PASSCODE = "30156"

@Composable
fun ArtistUploadDialog(
    onDismiss: () -> Unit,
    onUpload: (title: String, meta: String, price: Double, imageUri: String?) -> Unit
) {
    var isAuthenticated by remember { mutableStateOf(false) }
    var enteredPasscode by remember { mutableStateOf("") }
    var passcodeError by remember { mutableStateOf(false) }

    // Upload fields
    var title by remember { mutableStateOf("") }
    var meta by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var formError by remember { mutableStateOf<String?>(null) }

    // Zero-permission Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

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
                // Top header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (!isAuthenticated) "Artist Access" else "Add a Sketch",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkPrimary
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = InkPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (!isAuthenticated) {
                    // Passcode stage
                    Text(
                        text = "Enter the upload password to add a new original sketch to the shop.",
                        fontSize = 13.sp,
                        color = Graphite,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = enteredPasscode,
                        onValueChange = {
                            enteredPasscode = it
                            passcodeError = false
                        },
                        label = { Text("Studio Passcode") },
                        placeholder = { Text("e.g. 30156") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InkPrimary,
                            unfocusedBorderColor = LineBorder,
                            focusedContainerColor = PaperSheet,
                            unfocusedContainerColor = PaperSheet
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("passcode_input")
                    )

                    if (passcodeError) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Incorrect password. Hint: 30156",
                            color = WaxRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (enteredPasscode.trim() == ARTIST_PASSCODE) {
                                isAuthenticated = true
                                passcodeError = false
                            } else {
                                passcodeError = true
                            }
                        },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = InkPrimary,
                            contentColor = PaperSheet
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("submit_passcode")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Unlock Studio", fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Upload form
                    Text(
                        text = "Fill in the artwork details. It will be added immediately as a 1-of-1 original.",
                        fontSize = 12.sp,
                        color = Graphite
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Image picker section
                    Text(
                        text = "Artwork Sheet Image",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = InkPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(PaperSheet)
                            .border(1.dp, LineBorder, RoundedCornerShape(6.dp))
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Selected Sketch Preview",
                                modifier = Modifier.matchParentSize(),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = "Pick photo",
                                    tint = Graphite,
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(
                                    text = "Tap to choose artwork photo",
                                    fontSize = 12.sp,
                                    color = Graphite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        placeholder = { Text("e.g. Rainy Window") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InkPrimary,
                            unfocusedBorderColor = LineBorder,
                            focusedContainerColor = PaperSheet,
                            unfocusedContainerColor = PaperSheet
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upload_title")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = meta,
                        onValueChange = { meta = it },
                        label = { Text("Medium & Paper") },
                        placeholder = { Text("e.g. Ink on 8×10\" cold press") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InkPrimary,
                            unfocusedBorderColor = LineBorder,
                            focusedContainerColor = PaperSheet,
                            unfocusedContainerColor = PaperSheet
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upload_meta")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Price (₹)") },
                        placeholder = { Text("e.g. 350") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InkPrimary,
                            unfocusedBorderColor = LineBorder,
                            focusedContainerColor = PaperSheet,
                            unfocusedContainerColor = PaperSheet
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upload_price")
                    )

                    if (formError != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = formError ?: "",
                            color = WaxRed,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                formError = "Please enter a sketch title."
                                return@Button
                            }
                            val priceVal = priceText.toDoubleOrNull()
                            if (priceVal == null || priceVal <= 0) {
                                formError = "Please enter a valid price."
                                return@Button
                            }

                            onUpload(
                                title.trim(),
                                meta.trim(),
                                priceVal,
                                selectedImageUri?.toString()
                            )
                        },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = InkPrimary,
                            contentColor = PaperSheet
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("submit_upload_button")
                    ) {
                        Text(
                            text = "Add to Shop Wall",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}
