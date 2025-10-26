// NotificationSystem.kt
package com.example.notificationwindow

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Notification data class
data class NotificationData(
    val title: String,
    val message: String,
    val icon: ImageVector? = null,
    val type: NotificationType = NotificationType.INFO,
    val duration: Long = 5000L,
    val onAction: (() -> Unit)? = null,
    val actionText: String = "Action"
)

// Notification types with colors
enum class NotificationType(val color: Color, val icon: ImageVector) {
    INFO(Color(0xFF2196F3), Icons.Default.Info),
    SUCCESS(Color(0xFF4CAF50), Icons.Default.CheckCircle),
    WARNING(Color(0xFFFFC107), Icons.Default.Warning),
    ERROR(Color(0xFFF44336), Icons.Default.Error)
}

// Notification Manager - Singleton
object NotificationManager {
    private val _notifications = mutableStateListOf<Pair<String, NotificationData>>()
    val notifications: List<Pair<String, NotificationData>> = _notifications

    // Main method to show notifications
    fun show(notification: NotificationData) {
        val id = System.currentTimeMillis().toString()
        _notifications.add(id to notification)
    }

    // Convenience methods
    fun showInfo(title: String, message: String, onAction: (() -> Unit)? = null) {
        show(NotificationData(title, message, type = NotificationType.INFO, onAction = onAction))
    }

    fun showSuccess(title: String, message: String, onAction: (() -> Unit)? = null) {
        show(NotificationData(title, message, type = NotificationType.SUCCESS, onAction = onAction))
    }

    fun showWarning(title: String, message: String, onAction: (() -> Unit)? = null) {
        show(NotificationData(title, message, type = NotificationType.WARNING, onAction = onAction))
    }

    fun showError(title: String, message: String, onAction: (() -> Unit)? = null) {
        show(NotificationData(title, message, type = NotificationType.ERROR, onAction = onAction))
    }

    fun dismiss(id: String) {
        _notifications.removeIf { it.first == id }
    }

    fun dismissAll() {
        _notifications.clear()
    }
}

@Composable
fun NotificationWindow(
    notification: NotificationData,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Auto-dismiss after duration
    LaunchedEffect(Unit) {
        delay(notification.duration)
        visible = false
        delay(300)
        onDismiss()
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f)
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(300)
        ) + fadeOut()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                val displayIcon = notification.icon ?: notification.type.icon
                Icon(
                    imageVector = displayIcon,
                    contentDescription = null,
                    tint = notification.type.color,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = notification.message,
                        fontSize = 14.sp,
                        color = Color(0xFFB0B0B0),
                        lineHeight = 20.sp
                    )

                    // Action button
                    if (notification.onAction != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(
                            onClick = {
                                try {
                                    notification.onAction.invoke()
                                    visible = false
                                    scope.launch {
                                        delay(300)
                                        onDismiss()
                                    }
                                } catch (e: Exception) {
                                    println("Error executing action: ${e.message}")
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = notification.type.color
                            )
                        ) {
                            Text(notification.actionText)
                        }
                    }
                }

                // Close button
                IconButton(
                    onClick = {
                        visible = false
                        scope.launch {
                            delay(300)
                            onDismiss()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF808080),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationContainer() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NotificationManager.notifications.forEach { (id, notification) ->
                key(id) {
                    NotificationWindow(
                        notification = notification,
                        onDismiss = { NotificationManager.dismiss(id) }
                    )
                }
            }
        }
    }
}