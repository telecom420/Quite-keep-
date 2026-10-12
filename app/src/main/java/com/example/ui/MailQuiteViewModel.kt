package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CalendarEvent
import com.example.data.model.ChatConversation
import com.example.data.model.ChatMessage
import com.example.data.model.DriveFile
import com.example.data.model.EmailFolder
import com.example.data.model.EmailItem
import com.example.data.model.MeetingItem
import com.example.data.model.UserAccount
import com.example.data.repository.MailQuiteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
    MAIL,
    CHAT,
    MEET
}

enum class FilterChipType(val label: String) {
    ALL("All"),
    UNREAD("Unread"),
    STARRED("Starred"),
    ATTACHMENTS("Attachments")
}

data class MailQuiteUiState(
    val currentTab: MainTab = MainTab.MAIL,
    val activeFolder: EmailFolder = EmailFolder.PRIMARY,
    val searchQuery: String = "",
    val activeFilterChip: FilterChipType = FilterChipType.ALL,
    val selectedEmail: EmailItem? = null,
    val isComposeOpen: Boolean = false,
    val isAccountDialogOpen: Boolean = false,
    val isCreateAccountDialogOpen: Boolean = false,
    val isSignInDialogOpen: Boolean = false,
    val isNewMeetingDialogOpen: Boolean = false,
    val activeMeeting: MeetingItem? = null,
    val activeConversation: ChatConversation? = null,
    val isCalendarOpen: Boolean = false,
    val isDriveOpen: Boolean = false,
    val isMicMuted: Boolean = false,
    val isCameraOn: Boolean = true,
    val snackbarMessage: String? = null
)

class MailQuiteViewModel(
    private val repository: MailQuiteRepository = MailQuiteRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MailQuiteUiState())
    val uiState: StateFlow<MailQuiteUiState> = _uiState.asStateFlow()

    val rawEmails: StateFlow<List<EmailItem>> = repository.emails
    val accounts: StateFlow<List<UserAccount>> = repository.accounts
    val activeAccount: StateFlow<UserAccount> = repository.activeAccount
    val conversations: StateFlow<List<ChatConversation>> = repository.conversations
    val chatMessages: StateFlow<Map<String, List<ChatMessage>>> = repository.chatMessages
    val meetings: StateFlow<List<MeetingItem>> = repository.meetings
    val calendarEvents: StateFlow<List<CalendarEvent>> = repository.calendarEvents
    val driveFiles: StateFlow<List<DriveFile>> = repository.driveFiles

    // Filtered emails based on search query, active folder, and filter chip
    val filteredEmails: StateFlow<List<EmailItem>> = combine(
        repository.emails,
        _uiState
    ) { emails, state ->
        var list = emails

        // Filter by folder (unless searching broadly)
        if (state.searchQuery.isBlank()) {
            list = when (state.activeFolder) {
                EmailFolder.STARRED -> list.filter { it.isStarred }
                EmailFolder.TRASH -> list.filter { it.folder == EmailFolder.TRASH }
                EmailFolder.DRAFTS -> list.filter { it.folder == EmailFolder.DRAFTS }
                EmailFolder.SENT -> list.filter { it.folder == EmailFolder.SENT }
                EmailFolder.SPAM -> list.filter { it.folder == EmailFolder.SPAM }
                else -> list.filter { it.folder == state.activeFolder && it.folder != EmailFolder.TRASH }
            }
        }

        // Filter by search query
        if (state.searchQuery.isNotBlank()) {
            val q = state.searchQuery.trim().lowercase()
            list = list.filter {
                it.sender.lowercase().contains(q) ||
                it.subject.lowercase().contains(q) ||
                it.snippet.lowercase().contains(q) ||
                it.body.lowercase().contains(q)
            }
        }

        // Filter by active chip
        list = when (state.activeFilterChip) {
            FilterChipType.ALL -> list
            FilterChipType.UNREAD -> list.filter { !it.isRead }
            FilterChipType.STARRED -> list.filter { it.isStarred }
            FilterChipType.ATTACHMENTS -> list.filter { it.hasAttachment }
        }

        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: MainTab) {
        _uiState.value = _uiState.value.copy(
            currentTab = tab,
            selectedEmail = null,
            activeConversation = null,
            isCalendarOpen = false,
            isDriveOpen = false
        )
    }

    fun selectFolder(folder: EmailFolder) {
        _uiState.value = _uiState.value.copy(
            activeFolder = folder,
            selectedEmail = null,
            searchQuery = "",
            isCalendarOpen = false,
            isDriveOpen = false,
            currentTab = MainTab.MAIL
        )
    }

    fun setFilterChip(chip: FilterChipType) {
        _uiState.value = _uiState.value.copy(activeFilterChip = chip)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun openEmail(email: EmailItem) {
        repository.markAsRead(email.id, true)
        _uiState.value = _uiState.value.copy(selectedEmail = email.copy(isRead = true))
    }

    fun closeEmail() {
        _uiState.value = _uiState.value.copy(selectedEmail = null)
    }

    fun toggleStar(emailId: String) {
        repository.toggleStar(emailId)
        val currentSelected = _uiState.value.selectedEmail
        if (currentSelected?.id == emailId) {
            _uiState.value = _uiState.value.copy(
                selectedEmail = currentSelected.copy(isStarred = !currentSelected.isStarred)
            )
        }
    }

    fun deleteEmail(emailId: String) {
        repository.deleteEmail(emailId)
        _uiState.value = _uiState.value.copy(
            selectedEmail = null,
            snackbarMessage = "Email moved to Trash"
        )
    }

    fun archiveEmail(emailId: String) {
        repository.archiveEmail(emailId)
        _uiState.value = _uiState.value.copy(
            selectedEmail = null,
            snackbarMessage = "Email archived"
        )
    }

    fun openCompose() {
        _uiState.value = _uiState.value.copy(isComposeOpen = true)
    }

    fun closeCompose() {
        _uiState.value = _uiState.value.copy(isComposeOpen = false)
    }

    fun sendEmail(to: String, subject: String, body: String, attachment: String?) {
        repository.sendEmail(to, subject, body, attachment)
        _uiState.value = _uiState.value.copy(
            isComposeOpen = false,
            snackbarMessage = "Message sent"
        )
    }

    fun saveDraft(to: String, subject: String, body: String) {
        repository.saveDraft(to, subject, body)
        _uiState.value = _uiState.value.copy(
            isComposeOpen = false,
            snackbarMessage = "Draft saved"
        )
    }

    fun setAccountDialogOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isAccountDialogOpen = open)
    }

    fun openCreateAccountDialog() {
        _uiState.value = _uiState.value.copy(
            isAccountDialogOpen = false,
            isCreateAccountDialogOpen = true
        )
    }

    fun closeCreateAccountDialog() {
        _uiState.value = _uiState.value.copy(isCreateAccountDialogOpen = false)
    }

    fun openSignInDialog() {
        _uiState.value = _uiState.value.copy(
            isAccountDialogOpen = false,
            isCreateAccountDialogOpen = false,
            isSignInDialogOpen = true
        )
    }

    fun closeSignInDialog() {
        _uiState.value = _uiState.value.copy(isSignInDialogOpen = false)
    }

    fun signInGoogleAccount(name: String, email: String) {
        val existing = repository.accounts.value.find { it.email.equals(email, ignoreCase = true) }
        if (existing != null) {
            repository.setActiveAccount(existing)
        } else {
            repository.createGoogleAccount(name, email)
        }
        _uiState.value = _uiState.value.copy(
            isSignInDialogOpen = false,
            snackbarMessage = "Signed in as $email"
        )
    }

    fun createGoogleAccount(name: String, email: String) {
        val newAcc = repository.createGoogleAccount(name, email)
        _uiState.value = _uiState.value.copy(
            isCreateAccountDialogOpen = false,
            snackbarMessage = "Google Account created: ${newAcc.email}"
        )
    }

    fun selectAccount(account: UserAccount) {
        repository.setActiveAccount(account)
        _uiState.value = _uiState.value.copy(
            isAccountDialogOpen = false,
            snackbarMessage = "Switched to ${account.email}"
        )
    }

    fun openConversation(conversation: ChatConversation) {
        _uiState.value = _uiState.value.copy(activeConversation = conversation)
    }

    fun closeConversation() {
        _uiState.value = _uiState.value.copy(activeConversation = null)
    }

    fun sendChatMessage(conversationId: String, text: String) {
        if (text.isBlank()) return
        repository.sendChatMessage(conversationId, text)
    }

    fun openNewMeetingDialog(open: Boolean) {
        _uiState.value = _uiState.value.copy(isNewMeetingDialogOpen = open)
    }

    fun startInstantMeeting(title: String = "Instant Meeting") {
        val meeting = repository.createMeeting(title, "Ongoing")
        _uiState.value = _uiState.value.copy(
            isNewMeetingDialogOpen = false,
            activeMeeting = meeting,
            isMicMuted = false,
            isCameraOn = true
        )
    }

    fun joinMeeting(meeting: MeetingItem) {
        _uiState.value = _uiState.value.copy(
            activeMeeting = meeting,
            isMicMuted = false,
            isCameraOn = true
        )
    }

    fun leaveMeeting() {
        _uiState.value = _uiState.value.copy(
            activeMeeting = null,
            snackbarMessage = "Left meeting"
        )
    }

    fun toggleMic() {
        _uiState.value = _uiState.value.copy(isMicMuted = !_uiState.value.isMicMuted)
    }

    fun toggleCamera() {
        _uiState.value = _uiState.value.copy(isCameraOn = !_uiState.value.isCameraOn)
    }

    fun openCalendar() {
        _uiState.value = _uiState.value.copy(
            isCalendarOpen = true,
            isDriveOpen = false,
            selectedEmail = null
        )
    }

    fun openDrive() {
        _uiState.value = _uiState.value.copy(
            isDriveOpen = true,
            isCalendarOpen = false,
            selectedEmail = null
        )
    }

    fun closeWorkspaceView() {
        _uiState.value = _uiState.value.copy(
            isCalendarOpen = false,
            isDriveOpen = false
        )
    }

    fun addCalendarEvent(title: String, time: String, location: String) {
        repository.addCalendarEvent(title, time, location)
        _uiState.value = _uiState.value.copy(snackbarMessage = "Event added to Calendar")
    }

    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }
}
