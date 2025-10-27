package com.example.notificationwindow.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notificationwindow.NotificationManager
import com.example.notificationwindow.data.NotificationData
import com.example.notificationwindow.data.NotificationType

@Composable
fun MainContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Window Notification System",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Test different notification types below",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Info Notification Button
        Button(
            onClick = {
                NotificationManager.showInfo(
                    "Information",
                    "This is an informational message with important details."
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Show Info Notification")
        }

        // Success Notification with Action Button
        Button(
            onClick = {
                NotificationManager.show(
                    NotificationData(
                        title = "Success!",
                        message = "Your operation completed successfully.",
                        type = NotificationType.SUCCESS,
                        onAction = {
                            NotificationManager.showInfo(
                                "Action Clicked",
                                "You clicked the View Details button!"
                            )
                        },
                        actionText = "View Details"
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Show Success with Action")
        }

        // Warning Notification Button
        Button(
            onClick = {
                NotificationManager.showWarning(
                    "Warning",
                    "Please review this warning message carefully before proceeding."
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFC107),
                contentColor = Color.Black
            )
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Show Warning")
        }

        // Error Notification with Retry Button
        Button(
            onClick = {
                NotificationManager.show(
                    NotificationData(
                        title = "Error Occurred",
                        message = "An error has occurred. Please try again later.",
                        type = NotificationType.ERROR,
                        duration = 7000L,
                        onAction = {
                            NotificationManager.showInfo(
                                "Retrying",
                                "Attempting to retry operation..."
                            )
                        },
                        actionText = "Retry"
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF44336)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Show Error with Retry")
        }

        // Custom Notification with Custom Icon
        Button(
            onClick = {
                NotificationManager.show(
                    NotificationData(
                        title = "Custom Notification",
                        message = "This notification has a custom star icon and longer duration.",
                        icon = Icons.Default.Star,
                        type = NotificationType.INFO,
                        duration = 8000L
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9C27B0)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Show Custom Notification")
        }

        // Multiple Notifications
        Button(
            onClick = {
                NotificationManager.showInfo("First", "First notification")
                NotificationManager.showSuccess("Second", "Second notification")
                NotificationManager.showWarning("Third", "Third notification")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00BCD4)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Show Multiple Notifications")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dismiss All Button
        OutlinedButton(
            onClick = { NotificationManager.dismissAll() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Dismiss All Notifications")
        }

        Spacer(modifier = Modifier.weight(1f))

        // Usage example card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💡 Usage Example:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "NotificationManager.showSuccess(\n  \"Saved\",\n  \"Data saved successfully\"\n)",
                    fontSize = 12.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}



/*

// Helper methods to show notifications
fun showInfoNotification(title: String, message: String) {
    NotificationManager.showInfo(title, message)
}

fun showSuccessNotification(title: String, message: String) {
    NotificationManager.showSuccess(title, message)
}

fun showWarningNotification(title: String, message: String) {
    NotificationManager.showWarning(title, message)
}

fun showErrorNotification(title: String, message: String) {
    NotificationManager.showError(title, message)
}

fun showCustomNotification(
    title: String,
    message: String,
    type: NotificationType = NotificationType.INFO,
    duration: Long = 5000L,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onAction: (() -> Unit)? = null,
    actionText: String = "Action"
) {
    NotificationManager.show(
        NotificationData(
            title = title,
            message = message,
            type = type,
            duration = duration,
            icon = icon,
            onAction = onAction,
            actionText = actionText
        )
    )
}

 */
