package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.SketchPickerAssistant
import com.example.data.AppDatabase
import com.example.data.AuthRepository
import com.example.data.AuthResult
import com.example.data.CartEntity
import com.example.data.OrderEntity
import com.example.data.SessionEntity
import com.example.data.SketchEntity
import com.example.data.SketchRepository
import com.example.model.ChatMessage
import com.example.model.MessageSender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FilterType(val label: String) {
    ALL("All"),
    AVAILABLE("Available"),
    SOLD("Sold"),
    UNDER_350("Under ₹350"),
    PEN_INK("Pen & Ink"),
    GRAPHITE_CHARCOAL("Graphite & Charcoal")
}

class TysViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SketchRepository
    private val authRepository: AuthRepository
    private val assistant = SketchPickerAssistant()

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = SketchRepository(database.sketchDao())
        authRepository = AuthRepository(database.userDao())
        viewModelScope.launch {
            repository.ensureDefaultSketches()
        }
    }

    val currentUser: StateFlow<SessionEntity?> = authRepository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allSketches: StateFlow<List<SketchEntity>> = repository.allSketches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartEntities: StateFlow<List<CartEntity>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchQuery = MutableStateFlow("")
    val selectedFilter = MutableStateFlow(FilterType.ALL)

    // Filtered sketches
    val filteredSketches: StateFlow<List<SketchEntity>> = combine(
        allSketches,
        searchQuery,
        selectedFilter
    ) { sketches, query, filter ->
        var list = sketches
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                        it.meta.lowercase().contains(q) ||
                        it.medium.lowercase().contains(q)
            }
        }
        when (filter) {
            FilterType.ALL -> list
            FilterType.AVAILABLE -> list.filter { !it.isSold }
            FilterType.SOLD -> list.filter { it.isSold }
            FilterType.UNDER_350 -> list.filter { it.price <= 350.0 }
            FilterType.PEN_INK -> list.filter {
                it.medium.contains("ink", ignoreCase = true) ||
                        it.meta.contains("ink", ignoreCase = true) ||
                        it.meta.contains("pen", ignoreCase = true)
            }
            FilterType.GRAPHITE_CHARCOAL -> list.filter {
                it.medium.contains("graphite", ignoreCase = true) ||
                        it.medium.contains("charcoal", ignoreCase = true) ||
                        it.meta.contains("graphite", ignoreCase = true) ||
                        it.meta.contains("charcoal", ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart Sketches derived
    val cartSketches: StateFlow<List<SketchEntity>> = combine(
        allSketches,
        cartEntities
    ) { sketches, cartItems ->
        val cartIds = cartItems.map { it.sketchId }.toSet()
        sketches.filter { it.id in cartIds }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dialogs & Sheets states
    val selectedSketchForDetail = MutableStateFlow<SketchEntity?>(null)
    val isCartOpen = MutableStateFlow(false)
    val isUploadOpen = MutableStateFlow(false)
    val isMembersOpen = MutableStateFlow(false)
    val isChatOpen = MutableStateFlow(false)
    val isCheckoutFormOpen = MutableStateFlow(false)
    val isAuthOpen = MutableStateFlow(false)
    val isProfileOpen = MutableStateFlow(false)

    // Auth state
    val isAuthLoading = MutableStateFlow(false)
    val authErrorMessage = MutableStateFlow<String?>(null)

    // Toast notification
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Last completed order
    val lastCompletedOrder = MutableStateFlow<OrderEntity?>(null)

    // Chat
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = MessageSender.ASSISTANT,
                text = "Hi! Tell me a bit about what you're looking for — room, mood, size, budget — and I'll point you to a sketch from the shop that fits."
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()
    val isChatThinking = MutableStateFlow(false)

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun addToCart(sketch: SketchEntity) {
        if (sketch.isSold) {
            showToast("This piece has already sold.")
            return
        }
        val inCart = cartEntities.value.any { it.sketchId == sketch.id }
        if (inCart) {
            showToast("Already in cart — every sketch is one-of-one.")
            return
        }
        viewModelScope.launch {
            repository.addToCart(sketch.id)
            showToast("Added \"${sketch.title}\" to cart")
        }
    }

    fun removeFromCart(sketchId: String) {
        viewModelScope.launch {
            repository.removeFromCart(sketchId)
            showToast("Removed from cart")
        }
    }

    fun submitCheckout(customerName: String, address: String) {
        val currentCart = cartSketches.value
        if (currentCart.isEmpty()) {
            showToast("Your cart is empty")
            return
        }
        if (customerName.isBlank() || address.isBlank()) {
            showToast("Please provide your name and delivery address")
            return
        }

        viewModelScope.launch {
            val order = repository.checkout(customerName, address, currentCart)
            lastCompletedOrder.value = order
            isCheckoutFormOpen.value = false
            isCartOpen.value = false
            showToast("Order placed! Packed flat in a rigid mailer.")
        }
    }

    fun uploadSketch(
        title: String,
        meta: String,
        price: Double,
        imageUri: String?
    ) {
        val newSketch = SketchEntity(
            id = "p" + System.currentTimeMillis(),
            title = title.trim(),
            meta = if (meta.isBlank()) "Original sketch" else meta.trim(),
            price = price,
            originalPrice = null,
            isSold = false,
            medium = if (meta.isBlank()) "Mixed Media" else meta.trim(),
            dimensions = "Original sheet",
            description = "Hand-drawn original sheet uploaded by studio artist.",
            customImageUri = imageUri,
            isCustomUpload = true,
            drawingKey = "custom",
            dateAdded = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.addSketch(newSketch)
            isUploadOpen.value = false
            showToast("Sketch added to the shop!")
        }
    }

    fun sendChatMessage(userText: String) {
        if (userText.isBlank()) return
        val userMsg = ChatMessage(sender = MessageSender.USER, text = userText.trim())
        _chatMessages.value = _chatMessages.value + userMsg
        isChatThinking.value = true

        viewModelScope.launch {
            try {
                val reply = assistant.consultAssistant(
                    userPrompt = userText.trim(),
                    history = _chatMessages.value,
                    catalog = allSketches.value
                )
                _chatMessages.value = _chatMessages.value + reply
            } catch (e: Exception) {
                _chatMessages.value = _chatMessages.value + ChatMessage(
                    sender = MessageSender.ASSISTANT,
                    text = "I'm having a brief moment of quiet in the studio. Take a look at the wall above — Corner Café and Study of Hands are both available!"
                )
            } finally {
                isChatThinking.value = false
            }
        }
    }

    fun login(email: String, pass: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isAuthLoading.value = true
            authErrorMessage.value = null
            when (val res = authRepository.logIn(email, pass)) {
                is AuthResult.Success -> {
                    isAuthOpen.value = false
                    showToast("Welcome back, ${res.user.displayName}!")
                    onSuccess()
                }
                is AuthResult.Error -> {
                    authErrorMessage.value = res.message
                }
            }
            isAuthLoading.value = false
        }
    }

    fun signUp(email: String, pass: String, name: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isAuthLoading.value = true
            authErrorMessage.value = null
            when (val res = authRepository.signUp(email, pass, name)) {
                is AuthResult.Success -> {
                    isAuthOpen.value = false
                    showToast("Welcome to TYS, ${res.user.displayName}!")
                    onSuccess()
                }
                is AuthResult.Error -> {
                    authErrorMessage.value = res.message
                }
            }
            isAuthLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logOut()
            isProfileOpen.value = false
            showToast("Logged out successfully.")
        }
    }

    fun clearAuthError() {
        authErrorMessage.value = null
    }
}
