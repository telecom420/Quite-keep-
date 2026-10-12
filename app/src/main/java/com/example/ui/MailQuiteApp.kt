package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AccountSwitcherDialog
import com.example.ui.components.CreateGoogleAccountDialog
import com.example.ui.components.GoogleSignInDialog
import com.example.ui.components.MailQuiteTopBar
import com.example.ui.components.NavigationDrawerContent
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.ComposeScreen
import com.example.ui.screens.DriveScreen
import com.example.ui.screens.EmailDetailScreen
import com.example.ui.screens.MailScreen
import com.example.ui.screens.MeetScreen
import com.example.ui.theme.MailBlue
import kotlinx.coroutines.launch

@Composable
fun MailQuiteApp(
    viewModel: MailQuiteViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val accounts by viewModel.accounts.collectAsState()
    val activeAccount by viewModel.activeAccount.collectAsState()
    val emails by viewModel.filteredEmails.collectAsState()
    val conversations by viewModel.conversations.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val meetings by viewModel.meetings.collectAsState()
    val calendarEvents by viewModel.calendarEvents.collectAsState()
    val driveFiles by viewModel.driveFiles.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Modal Drawer wrapper
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                currentFolder = uiState.activeFolder,
                activeAccount = activeAccount,
                onSelectFolder = { folder -> viewModel.selectFolder(folder) },
                onOpenCalendar = { viewModel.openCalendar() },
                onOpenDrive = { viewModel.openDrive() },
                onCreateAccountClick = { viewModel.openCreateAccountDialog() },
                onSignInClick = { viewModel.openSignInDialog() },
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                // Show top search bar only on main tabs when not in sub-screens
                if (uiState.selectedEmail == null &&
                    !uiState.isComposeOpen &&
                    !uiState.isCalendarOpen &&
                    !uiState.isDriveOpen &&
                    uiState.activeConversation == null &&
                    uiState.activeMeeting == null
                ) {
                    MailQuiteTopBar(
                        searchQuery = uiState.searchQuery,
                        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onAvatarClick = { viewModel.setAccountDialogOpen(true) },
                        avatarLetter = activeAccount.avatarLetter,
                        avatarColorHex = activeAccount.avatarColorHex
                    )
                }
            },
            bottomBar = {
                // Bottom navigation bar (Mail, Chat, Meet) shown when on top-level tabs
                if (uiState.selectedEmail == null &&
                    !uiState.isComposeOpen &&
                    !uiState.isCalendarOpen &&
                    !uiState.isDriveOpen &&
                    uiState.activeConversation == null &&
                    uiState.activeMeeting == null
                ) {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .testTag("main_bottom_nav_bar"),
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 3.dp
                    ) {
                        // Mail Tab
                        NavigationBarItem(
                            selected = uiState.currentTab == MainTab.MAIL,
                            onClick = { viewModel.selectTab(MainTab.MAIL) },
                            icon = {
                                Icon(
                                    imageVector = if (uiState.currentTab == MainTab.MAIL) Icons.Filled.Mail else Icons.Outlined.MailOutline,
                                    contentDescription = "Mail",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "Mail",
                                    fontWeight = if (uiState.currentTab == MainTab.MAIL) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MailBlue,
                                selectedTextColor = MailBlue,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("tab_mail")
                        )

                        // Chat Tab
                        NavigationBarItem(
                            selected = uiState.currentTab == MainTab.CHAT,
                            onClick = { viewModel.selectTab(MainTab.CHAT) },
                            icon = {
                                Icon(
                                    imageVector = if (uiState.currentTab == MainTab.CHAT) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                                    contentDescription = "Chat",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "Chat",
                                    fontWeight = if (uiState.currentTab == MainTab.CHAT) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MailBlue,
                                selectedTextColor = MailBlue,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("tab_chat")
                        )

                        // Meet Tab
                        NavigationBarItem(
                            selected = uiState.currentTab == MainTab.MEET,
                            onClick = { viewModel.selectTab(MainTab.MEET) },
                            icon = {
                                Icon(
                                    imageVector = if (uiState.currentTab == MainTab.MEET) Icons.Filled.Videocam else Icons.Outlined.Videocam,
                                    contentDescription = "Meet",
                                    modifier = Modifier.size(26.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "Meet",
                                    fontWeight = if (uiState.currentTab == MainTab.MEET) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MailBlue,
                                selectedTextColor = MailBlue,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("tab_meet")
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    uiState.isComposeOpen -> {
                        ComposeScreen(
                            userEmail = activeAccount.email,
                            onSend = { to, subj, body, att ->
                                viewModel.sendEmail(to, subj, body, att)
                            },
                            onSaveDraft = { to, subj, body ->
                                viewModel.saveDraft(to, subj, body)
                            },
                            onDiscard = { viewModel.closeCompose() }
                        )
                    }

                    uiState.isCalendarOpen -> {
                        CalendarScreen(
                            events = calendarEvents,
                            onAddEvent = { title, time, loc ->
                                viewModel.addCalendarEvent(title, time, loc)
                            },
                            onBack = { viewModel.closeWorkspaceView() }
                        )
                    }

                    uiState.isDriveOpen -> {
                        DriveScreen(
                            files = driveFiles,
                            onBack = { viewModel.closeWorkspaceView() }
                        )
                    }

                    uiState.selectedEmail != null -> {
                        val selectedEmail = uiState.selectedEmail!!
                        EmailDetailScreen(
                            email = selectedEmail,
                            onBack = { viewModel.closeEmail() },
                            onArchive = { viewModel.archiveEmail(selectedEmail.id) },
                            onDelete = { viewModel.deleteEmail(selectedEmail.id) },
                            onStarToggle = { viewModel.toggleStar(selectedEmail.id) },
                            onQuickReplySent = { replyText ->
                                viewModel.sendEmail(
                                    selectedEmail.senderEmail,
                                    "Re: " + selectedEmail.subject,
                                    replyText,
                                    null
                                )
                            }
                        )
                    }

                    else -> {
                        when (uiState.currentTab) {
                            MainTab.MAIL -> {
                                MailScreen(
                                    folder = uiState.activeFolder,
                                    emails = emails,
                                    activeFilterChip = uiState.activeFilterChip,
                                    onFilterChipSelected = { viewModel.setFilterChip(it) },
                                    onEmailClick = { viewModel.openEmail(it) },
                                    onStarToggle = { viewModel.toggleStar(it) },
                                    onComposeClick = { viewModel.openCompose() }
                                )
                            }

                            MainTab.CHAT -> {
                                ChatScreen(
                                    conversations = conversations,
                                    activeConversation = uiState.activeConversation,
                                    chatMessages = chatMessages,
                                    onSelectConversation = { viewModel.openConversation(it) },
                                    onCloseConversation = { viewModel.closeConversation() },
                                    onSendMessage = { convId, text ->
                                        viewModel.sendChatMessage(convId, text)
                                    }
                                )
                            }

                            MainTab.MEET -> {
                                MeetScreen(
                                    meetings = meetings,
                                    activeMeeting = uiState.activeMeeting,
                                    isMicMuted = uiState.isMicMuted,
                                    isCameraOn = uiState.isCameraOn,
                                    onStartInstantMeeting = { viewModel.startInstantMeeting() },
                                    onJoinMeeting = { viewModel.joinMeeting(it) },
                                    onLeaveMeeting = { viewModel.leaveMeeting() },
                                    onToggleMic = { viewModel.toggleMic() },
                                    onToggleCamera = { viewModel.toggleCamera() }
                                )
                            }
                        }
                    }
                }

                // Account Profile Switcher Dialog
                if (uiState.isAccountDialogOpen) {
                    AccountSwitcherDialog(
                        activeAccount = activeAccount,
                        accounts = accounts,
                        onSelectAccount = { viewModel.selectAccount(it) },
                        onCreateAccountClick = { viewModel.openCreateAccountDialog() },
                        onSignInClick = { viewModel.openSignInDialog() },
                        onDismiss = { viewModel.setAccountDialogOpen(false) }
                    )
                }

                // Google Sign In Dialog (GlifWebSignIn style)
                if (uiState.isSignInDialogOpen) {
                    GoogleSignInDialog(
                        onDismiss = { viewModel.closeSignInDialog() },
                        onSignInSuccess = { name, email ->
                            viewModel.signInGoogleAccount(name, email)
                        },
                        onCreateAccountClicked = {
                            viewModel.openCreateAccountDialog()
                        }
                    )
                }

                // Create Google Account Wizard Dialog
                if (uiState.isCreateAccountDialogOpen) {
                    CreateGoogleAccountDialog(
                        onDismiss = { viewModel.closeCreateAccountDialog() },
                        onAccountCreated = { name, email ->
                            viewModel.createGoogleAccount(name, email)
                        }
                    )
                }
            }
        }
    }
}
