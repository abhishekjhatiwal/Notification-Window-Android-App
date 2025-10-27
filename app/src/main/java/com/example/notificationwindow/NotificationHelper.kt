package com.example.notificationwindow

import androidx.compose.runtime.*
import com.example.notificationwindow.data.NotificationData
import com.example.notificationwindow.data.NotificationType

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