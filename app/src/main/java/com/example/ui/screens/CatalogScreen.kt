package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FlatPackedDeliveryStatusCard
import com.example.ui.components.SketchCard
import com.example.ui.components.SketchVectorCanvas
import com.example.ui.components.TysTopBar
import com.example.ui.theme.Graphite
import com.example.ui.theme.GraphiteLight
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.PaperDark
import com.example.ui.theme.PaperSheet
import com.example.ui.theme.TapeBeige
import com.example.ui.theme.WaxRed
import com.example.viewmodel.FilterType
import com.example.viewmodel.TysViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: TysViewModel,
    modifier: Modifier = Modifier
) {
    val filteredSketches by viewModel.filteredSketches.collectAsState()
    val allSketches by viewModel.allSketches.collectAsState()
    val cartEntities by viewModel.cartEntities.collectAsState()
    val cartSketches by viewModel.cartSketches.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    val selectedSketchForDetail by viewModel.selectedSketchForDetail.collectAsState()
    val isCartOpen by viewModel.isCartOpen.collectAsState()
    val isUploadOpen by viewModel.isUploadOpen.collectAsState()
    val isMembersOpen by viewModel.isMembersOpen.collectAsState()
    val isChatOpen by viewModel.isChatOpen.collectAsState()
    val isCheckoutFormOpen by viewModel.isCheckoutFormOpen.collectAsState()
    val isAuthOpen by viewModel.isAuthOpen.collectAsState()
    val isProfileOpen by viewModel.isProfileOpen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isAuthLoading by viewModel.isAuthLoading.collectAsState()
    val authErrorMessage by viewModel.authErrorMessage.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    val lastCompletedOrder by viewModel.lastCompletedOrder.collectAsState()

    val chatMessages by viewModel.chatMessages.collectAsState()
    val isChatThinking by viewModel.isChatThinking.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val cartSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearToast()
        }
    }

    val inCartIds = remember(cartEntities) {
        cartEntities.map { it.sketchId }.toSet()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = PaperBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TysTopBar(
                cartCount = cartEntities.size,
                currentUser = currentUser,
                onOpenAuth = { viewModel.isAuthOpen.value = true },
                onOpenProfile = { viewModel.isProfileOpen.value = true },
                onOpenUpload = { viewModel.isUploadOpen.value = true },
                onOpenMembers = { viewModel.isMembersOpen.value = true },
                onOpenCart = { viewModel.isCartOpen.value = true }
            )
        },
        floatingActionButton = {
            // Floating Sketch Picker AI Button
            Box(
                modifier = Modifier
                    .testTag("open_chat_fab")
                    .clip(CircleShape)
                    .background(InkPrimary)
                    .border(1.5.dp, LineBorder, CircleShape)
                    .clickable { viewModel.isChatOpen.value = true }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Sketch Picker Assistant",
                        tint = PaperSheet,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Sketch Picker",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PaperSheet
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 165.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 80.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. HERO SECTION
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Text(
                        text = "Sketches, straight\noff the page.",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 36.sp,
                        color = InkPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "TYS sells original pencil and ink sketches, one drawing at a time. No prints run twice — what you buy is the actual physical sheet the artist drew on.",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Graphite
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Hero sketchpad illustration preview card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(PaperSheet)
                            .border(1.2.dp, InkPrimary, RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        // Tape strip at top
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .width(54.dp)
                                .height(16.dp)
                                .rotate(-3f)
                                .background(TapeBeige)
                                .border(1.dp, Color(0x33B4AA8C))
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Studio Wall Archive",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = InkPrimary
                                )
                                Text(
                                    text = "${allSketches.count { !it.isSold }} original pieces currently on display",
                                    fontSize = 12.sp,
                                    color = Graphite
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(0.8.dp, LineBorder, RoundedCornerShape(4.dp))
                            ) {
                                SketchVectorCanvas(drawingKey = "cafe_scene")
                            }
                        }
                    }
                }
            }

            // FLAT-PACKED DELIVERY TRACKER (When order placed)
            if (allOrders.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    val activeOrder = allOrders.first()
                    FlatPackedDeliveryStatusCard(
                        orderId = activeOrder.orderId,
                        currentStatus = activeOrder.status,
                        customerName = activeOrder.customerName,
                        deliveryAddress = activeOrder.address,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }

            // 2. SEARCH & FILTER BAR
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.searchQuery.value = it },
                        placeholder = { Text("Search by title, medium, or paper…", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Graphite,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = Graphite,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InkPrimary,
                            unfocusedBorderColor = LineBorder,
                            focusedContainerColor = PaperSheet,
                            unfocusedContainerColor = PaperSheet
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Filter chips row
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(FilterType.values()) { filter ->
                            val isSelected = selectedFilter == filter
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) InkPrimary else PaperSheet)
                                    .border(
                                        1.dp,
                                        if (isSelected) InkPrimary else LineBorder,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable { viewModel.selectedFilter.value = filter }
                                    .padding(horizontal = 14.dp, vertical = 7.dp)
                                    .testTag("filter_${filter.name}")
                            ) {
                                Text(
                                    text = filter.label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) PaperSheet else InkPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Wall section header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "Currently on the wall",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = InkPrimary
                            )
                            Text(
                                text = "Each piece is 1-of-1. Once claimed, it's gone.",
                                fontSize = 12.sp,
                                color = Graphite
                            )
                        }

                        Text(
                            text = "${filteredSketches.size} sketches",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Graphite
                        )
                    }
                }
            }

            // 3. EMPTY STATE
            if (filteredSketches.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
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
                                text = "No sketches match your filter",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = InkPrimary
                            )
                            Text(
                                text = "Try adjusting your search or clearing active filters.",
                                fontSize = 12.sp,
                                color = Graphite
                            )
                        }
                    }
                }
            }

            // 4. SKETCHES GRID
            items(filteredSketches, key = { it.id }) { sketch ->
                SketchCard(
                    sketch = sketch,
                    isInCart = sketch.id in inCartIds,
                    onSelect = { viewModel.selectedSketchForDetail.value = sketch },
                    onAddToCart = { viewModel.addToCart(sketch) }
                )
            }
        }
    }

    // Modal Sheets & Dialogs
    selectedSketchForDetail?.let { sketch ->
        SketchDetailSheet(
            sketch = sketch,
            isInCart = sketch.id in inCartIds,
            sheetState = detailSheetState,
            onDismiss = { viewModel.selectedSketchForDetail.value = null },
            onAddToCart = { viewModel.addToCart(it) }
        )
    }

    if (isCartOpen) {
        CartSheet(
            cartSketches = cartSketches,
            sheetState = cartSheetState,
            onDismiss = { viewModel.isCartOpen.value = false },
            onRemove = { viewModel.removeFromCart(it) },
            onProceedToCheckout = {
                viewModel.isCartOpen.value = false
                viewModel.isCheckoutFormOpen.value = true
            }
        )
    }

    if (isCheckoutFormOpen) {
        CheckoutDialog(
            cartSketches = cartSketches,
            currentUser = currentUser,
            onDismiss = { viewModel.isCheckoutFormOpen.value = false },
            onConfirmOrder = { name, address ->
                viewModel.submitCheckout(name, address)
            }
        )
    }

    lastCompletedOrder?.let { order ->
        OrderSuccessDialog(
            order = order,
            onDismiss = { viewModel.lastCompletedOrder.value = null }
        )
    }

    if (isAuthOpen) {
        AuthDialog(
            isLoading = isAuthLoading,
            errorMessage = authErrorMessage,
            onDismiss = {
                viewModel.isAuthOpen.value = false
                viewModel.clearAuthError()
            },
            onLogin = { email, password ->
                viewModel.login(email, password)
            },
            onSignUp = { email, password, name ->
                viewModel.signUp(email, password, name)
            },
            onClearError = { viewModel.clearAuthError() }
        )
    }

    if (isProfileOpen && currentUser != null) {
        UserProfileDialog(
            user = currentUser!!,
            orders = allOrders,
            onDismiss = { viewModel.isProfileOpen.value = false },
            onLogout = { viewModel.logout() }
        )
    }

    if (isUploadOpen) {
        ArtistUploadDialog(
            onDismiss = { viewModel.isUploadOpen.value = false },
            onUpload = { title, meta, price, imageUri ->
                viewModel.uploadSketch(title, meta, price, imageUri)
            }
        )
    }

    if (isMembersOpen) {
        MembersDialog(
            onDismiss = { viewModel.isMembersOpen.value = false }
        )
    }

    if (isChatOpen) {
        AiChatDialog(
            messages = chatMessages,
            isThinking = isChatThinking,
            catalog = allSketches,
            onDismiss = { viewModel.isChatOpen.value = false },
            onSendMessage = { viewModel.sendChatMessage(it) },
            onSelectSketch = { viewModel.selectedSketchForDetail.value = it }
        )
    }
}
