package com.example.data.model

data class ChatMessage(
    val id: String,
    val conversationId: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean
)

data class ChatConversation(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val avatarLetter: String,
    val avatarColorHex: Long,
    val isOnline: Boolean = true
)

data class MeetingItem(
    val id: String,
    val title: String,
    val time: String,
    val duration: String,
    val code: String,
    val participants: List<String>,
    val isLiveNow: Boolean = false
)

data class CalendarEvent(
    val id: String,
    val title: String,
    val timeRange: String,
    val location: String,
    val colorHex: Long,
    val dayLabel: String
)

data class DriveFile(
    val id: String,
    val name: String,
    val extension: String,
    val size: String,
    val modifiedDate: String,
    val colorHex: Long
)
