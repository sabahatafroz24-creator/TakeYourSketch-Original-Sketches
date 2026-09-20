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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.SessionEntity
import com.example.data.SketchEntity
import com.example.ui.theme.Graphite
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.PaperDark
import com.example.ui.theme.PaperSheet
import com.example.ui.theme.WaxRed

@Composable
fun CheckoutDialog(
    cartSketches: List<SketchEntity>,
    currentUser: SessionEntity? = null,
    onDismiss: () -> Unit,
    onConfirmOrder: (name: String, address: String) -> Unit
) {
    var name by remember { mutableStateOf(currentUser?.displayName ?: "") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val total = cartSketches.sumOf { it.price }

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
                        text = "Flat-Pack Shipping",
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

                Spacer(modifier = Modifier.height(8.dp))

                // Summary of pieces
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(PaperDark)
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "Originals to dispatch:",
                            fontSize = 12.sp,
                            color = Graphite,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        cartSketches.forEach {
                            Text(
                                text = "• ${it.title} (₹${it.price.toInt()})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = InkPrimary
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = LineBorder
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total Amount", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = InkPrimary)
                            Text(text = "₹${total.toInt()}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = InkPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "DELIVERY DETAILS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Graphite,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Recipient Full Name") },
                    placeholder = { Text("e.g. Maya Sen") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InkPrimary,
                        unfocusedBorderColor = LineBorder,
                        focusedContainerColor = PaperSheet,
                        unfocusedContainerColor = PaperSheet
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_name")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Contact Phone") },
                    placeholder = { Text("+91 98765 43210") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InkPrimary,
                        unfocusedBorderColor = LineBorder,
                        focusedContainerColor = PaperSheet,
                        unfocusedContainerColor = PaperSheet
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_phone")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Street Address / Apt") },
                    placeholder = { Text("Flat 4B, Heritage Heights") },
                    maxLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InkPrimary,
                        unfocusedBorderColor = LineBorder,
                        focusedContainerColor = PaperSheet,
                        unfocusedContainerColor = PaperSheet
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_address")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        placeholder = { Text("Bengaluru") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InkPrimary,
                            unfocusedBorderColor = LineBorder,
                            focusedContainerColor = PaperSheet,
                            unfocusedContainerColor = PaperSheet
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = pincode,
                        onValueChange = { pincode = it },
                        label = { Text("PIN Code") },
                        placeholder = { Text("560001") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InkPrimary,
                            unfocusedBorderColor = LineBorder,
                            focusedContainerColor = PaperSheet,
                            unfocusedContainerColor = PaperSheet
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = WaxRed,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isBlank()) {
                            errorMessage = "Please enter your name."
                            return@Button
                        }
                        if (address.isBlank() || city.isBlank()) {
                            errorMessage = "Please enter complete address details."
                            return@Button
                        }

                        val fullAddress = "$address, $city ${if (pincode.isNotBlank()) "- $pincode" else ""}".trim()
                        onConfirmOrder(name.trim(), fullAddress)
                    },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = InkPrimary,
                        contentColor = PaperSheet
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("confirm_order_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Confirm & Ship Flat (₹${total.toInt()})",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }
        }
    }
}
