package com.example.data.model

enum class EmailFolder(val displayName: String) {
    PRIMARY("Primary"),
    SOCIAL("Social"),
    PROMOTIONS("Promotions"),
    UPDATES("Updates"),
    STARRED("Starred"),
    SNOOZED("Snoozed"),
    SENT("Sent"),
    DRAFTS("Drafts"),
    TRASH("Trash"),
    SPAM("Spam")
}

data class UserAccount(
    val id: String,
    val name: String,
    val email: String,
    val avatarLetter: String,
    val avatarColorHex: Long = 0xFF00796B,
    val storageUsedGb: Double = 1.2,
    val totalStorageGb: Double = 15.0
)

data class EmailItem(
    val id: String,
    val sender: String,
    val senderEmail: String,
    val avatarLetter: String,
    val avatarColorHex: Long,
    val isGoogleLogo: Boolean = false,
    val subject: String,
    val snippet: String,
    val body: String,
    val timeFormatted: String,
    val dateEpoch: Long = System.currentTimeMillis(),
    val folder: EmailFolder = EmailFolder.PRIMARY,
    val isRead: Boolean = true,
    val isStarred: Boolean = false,
    val hasAttachment: Boolean = false,
    val attachmentName: String? = null,
    val attachmentSize: String? = null
)
