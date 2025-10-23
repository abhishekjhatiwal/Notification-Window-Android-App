package com.example.notificationwindow

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
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

// Notification Manager
class NotificationManager {
    private val _notifications = mutableStateListOf<Pair<String, NotificationData>>()
    val notifications: List<Pair<String, NotificationData>> = _notifications

    fun show(notification: NotificationData) {
        val id = System.currentTimeMillis().toString()
        _notifications.add(id to notification)
    }

    fun dismiss(id: String) {
        _notifications.removeIf { it.first == id }
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
        delay(300) // Wait for animation
        onDismiss()
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f)
        ) + fadeIn(),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(300)
        ) + fadeOut()
    ) {
        Card(
            modifier = Modifier
                .width(400.dp)
                .padding(8.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            backgroundColor = Color(0xFF1E1E1E),
            elevation = 8.dp
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
fun NotificationContainer(manager: NotificationManager) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .widthIn(max = 420.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            manager.notifications.forEach { (id, notification) ->
                key(id) {
                    NotificationWindow(
                        notification = notification,
                        onDismiss = { manager.dismiss(id) }
                    )
                }
            }
        }
    }
}

@Composable
fun DemoApp(manager: NotificationManager) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Window Notification System",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Click buttons to test different notification types",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Info Notification
        Button(
            onClick = {
                manager.show(
                    NotificationData(
                        title = "Information",
                        message = "This is an informational message with important details.",
                        type = NotificationType.INFO
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3)),
            modifier = Modifier.width(300.dp)
        ) {
            Icon(Icons.Default.Info, null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Show Info Notification", color = Color.White)
        }

        // Success Notification
        Button(
            onClick = {
                manager.show(
                    NotificationData(
                        title = "Success!",
                        message = "Your operation completed successfully.",
                        type = NotificationType.SUCCESS,
                        onAction = { println("Success action clicked!") },
                        actionText = "View Details"
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50)),
            modifier = Modifier.width(300.dp)
        ) {
            Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Show Success Notification", color = Color.White)
        }

        // Warning Notification
        Button(
            onClick = {
                manager.show(
                    NotificationData(
                        title = "Warning",
                        message = "Please review this warning message carefully.",
                        type = NotificationType.WARNING,
                        icon = Icons.Default.Warning
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFC107)),
            modifier = Modifier.width(300.dp)
        ) {
            Icon(Icons.Default.Warning, null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Show Warning Notification", color = Color.Black)
        }

        // Error Notification
        Button(
            onClick = {
                manager.show(
                    NotificationData(
                        title = "Error Occurred",
                        message = "An error has occurred. Please try again.",
                        type = NotificationType.ERROR,
                        onAction = { println("Retry clicked!") },
                        actionText = "Retry",
                        duration = 7000L
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF44336)),
            modifier = Modifier.width(300.dp)
        ) {
            Icon(Icons.Default.Error, null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Show Error Notification", color = Color.White)
        }

        // Custom Notification
        Button(
            onClick = {
                manager.show(
                    NotificationData(
                        title = "Custom Notification",
                        message = "This notification has a custom icon and longer duration.",
                        icon = Icons.Default.Star,
                        type = NotificationType.INFO,
                        duration = 8000L
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9C27B0)),
            modifier = Modifier.width(300.dp)
        ) {
            Icon(Icons.Default.Star, null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Show Custom Notification", color = Color.White)
        }
    }
}

fun main() = application {
    val manager = remember { NotificationManager() }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Notification System - Jetpack Compose",
        state = rememberWindowState(
            size = DpSize(800.dp, 600.dp),
            position = WindowPosition(Alignment.Center)
        )
    ) {
        MaterialTheme(
            colors = lightColors(
                primary = Color(0xFF2196F3),
                secondary = Color(0xFF4CAF50)
            )
        ) {
            Box {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F5F5)
                ) {
                    DemoApp(manager)
                }

                // Notification overlay
                NotificationContainer(manager)
            }
        }
    }
}