package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.Graphite
import com.example.ui.theme.GraphiteLight
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.PaperDark
import com.example.ui.theme.PaperSheet
import com.example.ui.theme.WaxRed

enum class AuthMode {
    LOGIN,
    SIGN_UP
}

@Composable
fun AuthDialog(
    isLoading: Boolean,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onLogin: (email: String, password: String) -> Unit,
    onSignUp: (email: String, password: String, displayName: String) -> Unit,
    onClearError: () -> Unit
) {
    var mode by remember { mutableStateOf(AuthMode.LOGIN) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var localValidationMessage by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(mode) {
        onClearError()
        localValidationMessage = null
    }

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
                    Column {
                        Text(
                            text = if (mode == AuthMode.LOGIN) "Sign In" else "Create Account",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkPrimary
                        )
                        Text(
                            text = "TYS Sketches Collector Account",
                            fontSize = 11.sp,
                            color = Graphite,
                            letterSpacing = 0.5.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("close_auth_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close dialog",
                            tint = InkPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mode Tabs (Sign In / Sign Up)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(PaperDark)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (mode == AuthMode.LOGIN) InkPrimary else Color.Transparent)
                            .clickable {
                                mode = AuthMode.LOGIN
                            }
                            .padding(vertical = 8.dp)
                            .testTag("tab_signin"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Log In",
                            fontSize = 13.sp,
                            fontWeight = if (mode == AuthMode.LOGIN) FontWeight.Bold else FontWeight.Medium,
                            color = if (mode == AuthMode.LOGIN) PaperSheet else Graphite
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (mode == AuthMode.SIGN_UP) InkPrimary else Color.Transparent)
                            .clickable {
                                mode = AuthMode.SIGN_UP
                            }
                            .padding(vertical = 8.dp)
                            .testTag("tab_signup"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sign Up",
                            fontSize = 13.sp,
                            fontWeight = if (mode == AuthMode.SIGN_UP) FontWeight.Bold else FontWeight.Medium,
                            color = if (mode == AuthMode.SIGN_UP) PaperSheet else Graphite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Error Message Banner (if any)
                val activeError = errorMessage ?: localValidationMessage
                AnimatedVisibility(
                    visible = activeError != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    activeError?.let { err ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(WaxRed.copy(alpha = 0.1f))
                                .border(1.dp, WaxRed.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("auth_error_text")
                        ) {
                            Text(
                                text = err,
                                fontSize = 12.sp,
                                color = WaxRed,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // If Sign Up: Name field
                if (mode == AuthMode.SIGN_UP) {
                    Text(
                        text = "FULL NAME",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Graphite,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = displayName,
                        onValueChange = {
                            displayName = it
                            localValidationMessage = null
                        },
                        placeholder = { Text("e.g. Sabahat Afroz", fontSize = 13.sp, color = GraphiteLight) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Graphite,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InkPrimary,
                            unfocusedBorderColor = LineBorder,
                            focusedContainerColor = PaperSheet,
                            unfocusedContainerColor = PaperSheet
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Email field
                Text(
                    text = "EMAIL ADDRESS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Graphite,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        localValidationMessage = null
                    },
                    placeholder = { Text("your.email@example.com", fontSize = 13.sp, color = GraphiteLight) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = Graphite,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_email_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InkPrimary,
                        unfocusedBorderColor = LineBorder,
                        focusedContainerColor = PaperSheet,
                        unfocusedContainerColor = PaperSheet
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Password field
                Text(
                    text = "PASSWORD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Graphite,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        localValidationMessage = null
                    },
                    placeholder = { Text("At least 6 characters", fontSize = 13.sp, color = GraphiteLight) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Graphite,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = Graphite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = if (mode == AuthMode.SIGN_UP) ImeAction.Next else ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                        onDone = {
                            focusManager.clearFocus()
                            if (mode == AuthMode.LOGIN) {
                                if (email.isBlank()) {
                                    localValidationMessage = "Please enter your email."
                                } else if (password.isBlank()) {
                                    localValidationMessage = "Please enter your password."
                                } else {
                                    onLogin(email, password)
                                }
                            }
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_password_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InkPrimary,
                        unfocusedBorderColor = LineBorder,
                        focusedContainerColor = PaperSheet,
                        unfocusedContainerColor = PaperSheet
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                // If Sign Up: Confirm password
                if (mode == AuthMode.SIGN_UP) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "CONFIRM PASSWORD",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Graphite,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            localValidationMessage = null
                        },
                        placeholder = { Text("Re-enter password", fontSize = 13.sp, color = GraphiteLight) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Graphite,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_confirm_password_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InkPrimary,
                            unfocusedBorderColor = LineBorder,
                            focusedContainerColor = PaperSheet,
                            unfocusedContainerColor = PaperSheet
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Submit Button
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        if (mode == AuthMode.LOGIN) {
                            if (email.isBlank()) {
                                localValidationMessage = "Please enter your email."
                            } else if (password.isBlank()) {
                                localValidationMessage = "Please enter your password."
                            } else {
                                onLogin(email, password)
                            }
                        } else {
                            if (displayName.isBlank()) {
                                localValidationMessage = "Please enter your name."
                            } else if (email.isBlank()) {
                                localValidationMessage = "Please enter your email."
                            } else if (password.length < 6) {
                                localValidationMessage = "Password must be at least 6 characters."
                            } else if (password != confirmPassword) {
                                localValidationMessage = "Passwords do not match."
                            } else {
                                onSignUp(email, password, displayName)
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("auth_submit_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = InkPrimary,
                        contentColor = PaperSheet
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = PaperSheet,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (mode == AuthMode.LOGIN) "Log In" else "Create Account",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Security Note
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(PaperSheet)
                        .border(0.8.dp, LineBorder, RoundedCornerShape(6.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Secure credential storage",
                            tint = Graphite,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Credentials securely salted & hashed with PBKDF2 (10,000 rounds). Never stored in plain text.",
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = Graphite
                        )
                    }
                }
            }
        }
    }
}
