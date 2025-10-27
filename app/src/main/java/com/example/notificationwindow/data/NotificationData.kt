package com.example.notificationwindow.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

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
    WARNING(Color(0xFFEEC344), Icons.Default.Warning),
    ERROR(Color(0xFFF44336), Icons.Default.Error)
}
