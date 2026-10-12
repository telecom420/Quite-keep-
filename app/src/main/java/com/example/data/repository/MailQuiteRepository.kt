package com.example.data.repository

import com.example.data.model.CalendarEvent
import com.example.data.model.ChatConversation
import com.example.data.model.ChatMessage
import com.example.data.model.DriveFile
import com.example.data.model.EmailFolder
import com.example.data.model.EmailItem
import com.example.data.model.MeetingItem
import com.example.data.model.UserAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class MailQuiteRepository {

    private val defaultAccount = UserAccount(
        id = "acc_default",
        name = "Farhan Rahman",
        email = "oiki89663@gmail.com",
        avatarLetter = "F",
        avatarColorHex = 0xFF00796B,
        storageUsedGb = 1.2,
        totalStorageGb = 15.0
    )

    private val _accounts = MutableStateFlow<List<UserAccount>>(listOf(defaultAccount))
    val accounts: StateFlow<List<UserAccount>> = _accounts.asStateFlow()

    private val _activeAccount = MutableStateFlow<UserAccount>(defaultAccount)
    val activeAccount: StateFlow<UserAccount> = _activeAccount.asStateFlow()

    private val _emails = MutableStateFlow<List<EmailItem>>(emptyList())
    val emails: StateFlow<List<EmailItem>> = _emails.asStateFlow()

    private val _conversations = MutableStateFlow<List<ChatConversation>>(emptyList())
    val conversations: StateFlow<List<ChatConversation>> = _conversations.asStateFlow()

    private val _chatMessages = MutableStateFlow<Map<String, List<ChatMessage>>>(emptyMap())
    val chatMessages: StateFlow<Map<String, List<ChatMessage>>> = _chatMessages.asStateFlow()

    private val _meetings = MutableStateFlow<List<MeetingItem>>(emptyList())
    val meetings: StateFlow<List<MeetingItem>> = _meetings.asStateFlow()

    private val _calendarEvents = MutableStateFlow<List<CalendarEvent>>(emptyList())
    val calendarEvents: StateFlow<List<CalendarEvent>> = _calendarEvents.asStateFlow()

    private val _driveFiles = MutableStateFlow<List<DriveFile>>(emptyList())
    val driveFiles: StateFlow<List<DriveFile>> = _driveFiles.asStateFlow()

    init {
        seedInitialData()
    }

    private fun seedInitialData() {
        // Pre-seeded emails exactly matching the user's screenshot
        val initialEmails = listOf(
            EmailItem(
                id = "mail_1",
                sender = "Google",
                senderEmail = "no-reply@accounts.google.com",
                avatarLetter = "G",
                avatarColorHex = 0xFF4285F4,
                isGoogleLogo = true,
                subject = "Security alert",
                snippet = "New sign-in to your Mail Quite account...",
                body = "Your Google Account oiki89663@gmail.com was just used to sign in to Mail Quite on Android.\n\n" +
                        "• Device: Pixel 8 Pro (Android 15)\n" +
                        "• Time: 10:18 AM\n" +
                        "• Location: Mountain View, CA\n\n" +
                        "If this was you, you can safely ignore this alert. If you didn't initiate this action, please review your security settings immediately by tapping below.",
                timeFormatted = "10:18 AM",
                folder = EmailFolder.PRIMARY,
                isRead = false,
                isStarred = false
            ),
            EmailItem(
                id = "mail_2",
                sender = "Team Mail Quite",
                senderEmail = "team@mailquite.app",
                avatarLetter = "T",
                avatarColorHex = 0xFF7E57C2, // Purple circle as seen in screenshot
                isGoogleLogo = false,
                subject = "Tips for using Mail Quite",
                snippet = "Learn how to get the most out of your...",
                body = "Welcome to Mail Quite — Send • Receive • Stay Connected!\n\n" +
                        "Here are quick tips to supercharge your day:\n\n" +
                        "1. Unified Navigation: Jump between Mail, Chat, and Meet from the bottom bar at any time.\n" +
                        "2. Starred Essentials: Tap the star icon on any thread to keep important emails within easy reach.\n" +
                        "3. Smart Workspace: Open the drawer menu to access your integrated Calendar and Drive storage.\n" +
                        "4. Instant Compose: Tap the Compose button at any time to draft emails with smart templates.\n\n" +
                        "Enjoy a cleaner, faster email experience.\n" +
                        "— The Mail Quite Team",
                timeFormatted = "9:45 AM",
                folder = EmailFolder.PRIMARY,
                isRead = false,
                isStarred = false
            ),
            EmailItem(
                id = "mail_3",
                sender = "Project Updates",
                senderEmail = "notifications@projectwork.io",
                avatarLetter = "P",
                avatarColorHex = 0xFF2196F3, // Blue circle as seen in screenshot
                isGoogleLogo = false,
                subject = "Monthly report",
                snippet = "Hi, here's the latest update on the project...",
                body = "Hi Team,\n\n" +
                        "Here is the latest monthly progress update on our current milestone:\n\n" +
                        "• 94% of sprint deliverables finished ahead of schedule\n" +
                        "• User engagement metrics improved by 32% this month\n" +
                        "• Performance review highlights high satisfaction ratings\n\n" +
                        "The full deck with charts and key analytics is attached to this email. Please review before tomorrow's all-hands meeting.\n\n" +
                        "Best regards,\nProject Lead",
                timeFormatted = "Oct 9",
                folder = EmailFolder.PRIMARY,
                isRead = true,
                isStarred = false,
                hasAttachment = true,
                attachmentName = "Monthly_Status_Report.pdf",
                attachmentSize = "3.8 MB"
            ),
            EmailItem(
                id = "mail_4",
                sender = "Amazon",
                senderEmail = "shipment-tracking@amazon.com",
                avatarLetter = "A",
                avatarColorHex = 0xFF4CAF50, // Green circle as seen in screenshot
                isGoogleLogo = false,
                subject = "Your order has shipped",
                snippet = "Your order #123-456789-0123 is on its way.",
                body = "Hello,\n\n" +
                        "Great news! Your package is on its way and scheduled for delivery.\n\n" +
                        "Order Number: #123-456789-0123\n" +
                        "Item: Ergonomic Workspace Accessories & Noise-Cancelling Headphones\n" +
                        "Carrier: Amazon Logistics (Tracking: AMZ-98234-EXP)\n" +
                        "Estimated Delivery: Tomorrow by 7:00 PM\n\n" +
                        "You can track your package directly through your order history.",
                timeFormatted = "Oct 8",
                folder = EmailFolder.PRIMARY,
                isRead = true,
                isStarred = false
            ),
            EmailItem(
                id = "mail_5",
                sender = "Netflix",
                senderEmail = "info@mailer.netflix.com",
                avatarLetter = "N",
                avatarColorHex = 0xFFE53935, // Red circle as seen in screenshot
                isGoogleLogo = false,
                subject = "New on Netflix",
                snippet = "Check out what's new this week...",
                body = "Ready for the weekend?\n\n" +
                        "We've added new titles curated just for your watch list. Discover critically acclaimed releases, fresh sci-fi episodes, and new documentaries streaming right now in 4K HDR.\n\n" +
                        "Grab some popcorn and enjoy!",
                timeFormatted = "Oct 7",
                folder = EmailFolder.PRIMARY,
                isRead = true,
                isStarred = false
            ),
            EmailItem(
                id = "mail_6",
                sender = "GitHub",
                senderEmail = "notifications@github.com",
                avatarLetter = "G",
                avatarColorHex = 0xFF24292E,
                isGoogleLogo = false,
                subject = "[Merged] Feature: Compose & Chat UI Integration",
                snippet = "PR #108 was successfully merged into main...",
                body = "Pull request #108 'Compose & Chat UI Integration' has been approved and merged into branch 'main' by lead reviewer.\n\n" +
                        "All 42 automated tests passed with 0 warnings.\n" +
                        "View changes on GitHub.",
                timeFormatted = "Oct 5",
                folder = EmailFolder.UPDATES,
                isRead = true,
                isStarred = true
            ),
            EmailItem(
                id = "mail_7",
                sender = "Figma Team",
                senderEmail = "comments@figma.com",
                avatarLetter = "F",
                avatarColorHex = 0xFF9C27B0,
                isGoogleLogo = false,
                subject = "Sarah left a comment on 'Mail Quite Design System'",
                snippet = "Looks fantastic! Love the smooth floating Compose FAB...",
                body = "Sarah left a new comment on page 'M3 Adaptive Mobile':\n\n" +
                        "\"Looks fantastic! Love the smooth floating Compose FAB and clean email list layout. The bottom navigation bar spacing feels just right!\"\n\n" +
                        "Click here to reply in Figma.",
                timeFormatted = "Oct 3",
                folder = EmailFolder.SOCIAL,
                isRead = true,
                isStarred = false
            ),
            EmailItem(
                id = "mail_8",
                sender = "Spotify",
                senderEmail = "news@spotify.com",
                avatarLetter = "S",
                avatarColorHex = 0xFF1DB954,
                isGoogleLogo = false,
                subject = "Your Discover Weekly is ready",
                snippet = "30 new songs picked specially for your mood...",
                body = "Your weekly customized mix is freshly baked. Tune in to fresh indie, synthwave, and acoustic tracks matched to your listening vibe.",
                timeFormatted = "Oct 1",
                folder = EmailFolder.PROMOTIONS,
                isRead = true,
                isStarred = false
            )
        )
        _emails.value = initialEmails

        // Pre-seeded Chat Conversations
        val initialConversations = listOf(
            ChatConversation(
                id = "conv_1",
                name = "Team Mail Quite",
                lastMessage = "Let us know if you need any assistance getting started!",
                timestamp = "9:50 AM",
                unreadCount = 1,
                avatarLetter = "T",
                avatarColorHex = 0xFF7E57C2,
                isOnline = true
            ),
            ChatConversation(
                id = "conv_2",
                name = "Project Updates Channel",
                lastMessage = "Monthly analytics uploaded. Great numbers everyone!",
                timestamp = "Yesterday",
                unreadCount = 0,
                avatarLetter = "P",
                avatarColorHex = 0xFF2196F3,
                isOnline = true
            ),
            ChatConversation(
                id = "conv_3",
                name = "Sarah Jenkins",
                lastMessage = "Hey! Did you check out the new design mockup?",
                timestamp = "Yesterday",
                unreadCount = 2,
                avatarLetter = "S",
                avatarColorHex = 0xFF009688,
                isOnline = true
            ),
            ChatConversation(
                id = "conv_4",
                name = "Alex Rivera",
                lastMessage = "See you in the 2:00 PM video standup on Meet.",
                timestamp = "Oct 8",
                unreadCount = 0,
                avatarLetter = "A",
                avatarColorHex = 0xFFFF7043,
                isOnline = false
            )
        )
        _conversations.value = initialConversations

        // Initial chat messages
        _chatMessages.value = mapOf(
            "conv_1" to listOf(
                ChatMessage(
                    id = "msg_1",
                    conversationId = "conv_1",
                    senderName = "Team Mail Quite",
                    text = "Welcome to Mail Quite Chat! Here you can collaborate in real-time with teammates.",
                    timestamp = "9:46 AM",
                    isFromMe = false
                ),
                ChatMessage(
                    id = "msg_2",
                    conversationId = "conv_1",
                    senderName = "Team Mail Quite",
                    text = "Let us know if you need any assistance getting started!",
                    timestamp = "9:50 AM",
                    isFromMe = false
                )
            ),
            "conv_3" to listOf(
                ChatMessage(
                    id = "msg_3",
                    conversationId = "conv_3",
                    senderName = "Sarah Jenkins",
                    text = "Hi! I just finished polishing the Mail Quite components in Figma.",
                    timestamp = "Yesterday 4:15 PM",
                    isFromMe = false
                ),
                ChatMessage(
                    id = "msg_4",
                    conversationId = "conv_3",
                    senderName = "Me",
                    text = "Awesome Sarah, checking them now! The bottom bar and search looks super clean.",
                    timestamp = "Yesterday 4:20 PM",
                    isFromMe = true
                ),
                ChatMessage(
                    id = "msg_5",
                    conversationId = "conv_3",
                    senderName = "Sarah Jenkins",
                    text = "Hey! Did you check out the new design mockup?",
                    timestamp = "Yesterday 4:22 PM",
                    isFromMe = false
                )
            )
        )

        // Pre-seeded video meetings
        _meetings.value = listOf(
            MeetingItem(
                id = "meet_1",
                title = "Design & Product Sync",
                time = "Today, 11:30 AM",
                duration = "30 mins",
                code = "mqt-dync-pro",
                participants = listOf("Sarah", "Alex", "You"),
                isLiveNow = true
            ),
            MeetingItem(
                id = "meet_2",
                title = "Sprint Planning Review",
                time = "Today, 2:00 PM",
                duration = "45 mins",
                code = "mqt-plan-spr",
                participants = listOf("Alex", "David", "Emma", "You")
            ),
            MeetingItem(
                id = "meet_3",
                title = "Mail Quite All-Hands",
                time = "Tomorrow, 10:00 AM",
                duration = "60 mins",
                code = "mqt-all-hands",
                participants = listOf("Entire Workspace Team")
            )
        )

        // Pre-seeded calendar events
        _calendarEvents.value = listOf(
            CalendarEvent(
                id = "cal_1",
                title = "Design & Product Sync",
                timeRange = "11:30 AM - 12:00 PM",
                location = "Mail Quite Meet (mqt-dync-pro)",
                colorHex = 0xFF1A73E8,
                dayLabel = "Today"
            ),
            CalendarEvent(
                id = "cal_2",
                title = "Sprint Planning Review",
                timeRange = "2:00 PM - 2:45 PM",
                location = "Virtual Conference Room 2",
                colorHex = 0xFF34A853,
                dayLabel = "Today"
            ),
            CalendarEvent(
                id = "cal_3",
                title = "Mail Quite All-Hands Meeting",
                timeRange = "10:00 AM - 11:00 AM",
                location = "Main Auditorium & Meet",
                colorHex = 0xFFEA4335,
                dayLabel = "Tomorrow"
            )
        )

        // Pre-seeded drive files
        _driveFiles.value = listOf(
            DriveFile(
                id = "file_1",
                name = "Monthly_Status_Report.pdf",
                extension = "PDF",
                size = "3.8 MB",
                modifiedDate = "Oct 9, 2026",
                colorHex = 0xFFEA4335
            ),
            DriveFile(
                id = "file_2",
                name = "Workspace_Specs_v2.docx",
                extension = "DOCX",
                size = "1.5 MB",
                modifiedDate = "Oct 8, 2026",
                colorHex = 0xFF4285F4
            ),
            DriveFile(
                id = "file_3",
                name = "Q4_Revenue_Projections.xlsx",
                extension = "XLSX",
                size = "4.2 MB",
                modifiedDate = "Oct 6, 2026",
                colorHex = 0xFF34A853
            ),
            DriveFile(
                id = "file_4",
                name = "MailQuite_Brand_Guidelines.pdf",
                extension = "PDF",
                size = "8.1 MB",
                modifiedDate = "Oct 1, 2026",
                colorHex = 0xFFFBBC04
            )
        )
    }

    // Repository operations
    fun toggleStar(emailId: String) {
        _emails.value = _emails.value.map {
            if (it.id == emailId) it.copy(isStarred = !it.isStarred) else it
        }
    }

    fun markAsRead(emailId: String, read: Boolean = true) {
        _emails.value = _emails.value.map {
            if (it.id == emailId) it.copy(isRead = read) else it
        }
    }

    fun deleteEmail(emailId: String) {
        _emails.value = _emails.value.map {
            if (it.id == emailId) it.copy(folder = EmailFolder.TRASH) else it
        }
    }

    fun archiveEmail(emailId: String) {
        _emails.value = _emails.value.map {
            if (it.id == emailId) it.copy(folder = EmailFolder.UPDATES) else it
        }
    }

    fun restoreEmail(emailId: String) {
        _emails.value = _emails.value.map {
            if (it.id == emailId) it.copy(folder = EmailFolder.PRIMARY) else it
        }
    }

    fun sendEmail(
        to: String,
        subject: String,
        body: String,
        attachmentName: String? = null
    ) {
        val newEmail = EmailItem(
            id = "sent_" + UUID.randomUUID().toString().take(8),
            sender = "Me",
            senderEmail = "oiki89663@gmail.com",
            avatarLetter = "M",
            avatarColorHex = 0xFF00796B,
            subject = if (subject.isBlank()) "(no subject)" else subject,
            snippet = body.take(60),
            body = body,
            timeFormatted = "Just now",
            folder = EmailFolder.SENT,
            isRead = true,
            isStarred = false,
            hasAttachment = attachmentName != null,
            attachmentName = attachmentName,
            attachmentSize = if (attachmentName != null) "1.2 MB" else null
        )
        _emails.value = listOf(newEmail) + _emails.value
    }

    fun saveDraft(
        to: String,
        subject: String,
        body: String
    ) {
        val draft = EmailItem(
            id = "draft_" + UUID.randomUUID().toString().take(8),
            sender = "Draft",
            senderEmail = "oiki89663@gmail.com",
            avatarLetter = "D",
            avatarColorHex = 0xFF607D8B,
            subject = if (subject.isBlank()) "(Draft no subject)" else subject,
            snippet = body.take(60),
            body = body,
            timeFormatted = "Draft",
            folder = EmailFolder.DRAFTS,
            isRead = true,
            isStarred = false
        )
        _emails.value = listOf(draft) + _emails.value
    }

    fun sendChatMessage(conversationId: String, text: String) {
        val currentMsgs = _chatMessages.value[conversationId] ?: emptyList()
        val newMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            senderName = "Me",
            text = text,
            timestamp = "Just now",
            isFromMe = true
        )
        val updatedMap = _chatMessages.value.toMutableMap()
        updatedMap[conversationId] = currentMsgs + newMsg
        _chatMessages.value = updatedMap

        // Update conversation preview
        _conversations.value = _conversations.value.map {
            if (it.id == conversationId) it.copy(lastMessage = text, timestamp = "Just now") else it
        }
    }

    fun createMeeting(title: String, duration: String): MeetingItem {
        val newMeeting = MeetingItem(
            id = UUID.randomUUID().toString(),
            title = title,
            time = "Starting now",
            duration = duration,
            code = "mqt-" + UUID.randomUUID().toString().take(6),
            participants = listOf("You (Host)"),
            isLiveNow = true
        )
        _meetings.value = listOf(newMeeting) + _meetings.value
        return newMeeting
    }

    fun addCalendarEvent(title: String, timeRange: String, location: String) {
        val newEvent = CalendarEvent(
            id = UUID.randomUUID().toString(),
            title = title,
            timeRange = timeRange,
            location = location,
            colorHex = 0xFF1A73E8,
            dayLabel = "Today"
        )
        _calendarEvents.value = listOf(newEvent) + _calendarEvents.value
    }

    fun createGoogleAccount(name: String, email: String): UserAccount {
        val letter = name.take(1).uppercase().ifBlank { "G" }
        val colors = listOf(0xFF1A73E8, 0xFFEA4335, 0xFFFBBC04, 0xFF34A853, 0xFF7E57C2, 0xFFE91E63)
        val colorHex = colors[Math.abs(email.hashCode()) % colors.size]

        val newAccount = UserAccount(
            id = "acc_" + UUID.randomUUID().toString().take(8),
            name = name,
            email = email,
            avatarLetter = letter,
            avatarColorHex = colorHex,
            storageUsedGb = 0.1,
            totalStorageGb = 15.0
        )

        _accounts.value = _accounts.value + newAccount
        _activeAccount.value = newAccount

        // Seed welcome email for this new Google Account
        val welcomeEmail = EmailItem(
            id = "mail_welcome_" + UUID.randomUUID().toString().take(8),
            sender = "Google",
            senderEmail = "no-reply@accounts.google.com",
            avatarLetter = "G",
            avatarColorHex = 0xFF4285F4,
            isGoogleLogo = true,
            subject = "Welcome to your new Google Account, $name",
            snippet = "Confirm your settings and get started with Gmail...",
            body = "Hi $name,\n\nWelcome to your new Google Account ($email).\n\n" +
                    "Your new account gives you instant access to:\n" +
                    "• Mail Quite & Gmail: Fast, secure email with smart search\n" +
                    "• Google Meet: High-quality video calls and conferencing\n" +
                    "• Google Drive: 15 GB of secure cloud storage\n" +
                    "• Google Calendar: Smart schedule & appointments management\n\n" +
                    "You can manage your account settings, privacy, and security controls at any time.\n\n" +
                    "Thanks for signing up!\n— The Google Team",
            timeFormatted = "Just now",
            folder = EmailFolder.PRIMARY,
            isRead = false,
            isStarred = true
        )
        _emails.value = listOf(welcomeEmail) + _emails.value

        return newAccount
    }

    fun setActiveAccount(account: UserAccount) {
        _activeAccount.value = account
    }
}
