package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MeetingItem
import com.example.ui.theme.MailBlue
import com.example.ui.theme.MailRed

@Composable
fun MeetScreen(
    meetings: List<MeetingItem>,
    activeMeeting: MeetingItem?,
    isMicMuted: Boolean,
    isCameraOn: Boolean,
    onStartInstantMeeting: () -> Unit,
    onJoinMeeting: (MeetingItem) -> Unit,
    onLeaveMeeting: () -> Unit,
    onToggleMic: () -> Unit,
    onToggleCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showJoinCodeDialog by remember { mutableStateOf(false) }
    var joinCodeInput by remember { mutableStateOf("") }

    if (activeMeeting != null) {
        ActiveVideoMeetingView(
            meeting = activeMeeting,
            isMicMuted = isMicMuted,
            isCameraOn = isCameraOn,
            onLeaveMeeting = onLeaveMeeting,
            onToggleMic = onToggleMic,
            onToggleCamera = onToggleCamera,
            modifier = modifier
        )
    } else {
        MeetHomeView(
            meetings = meetings,
            onStartInstantMeeting = onStartInstantMeeting,
            onOpenJoinWithCode = { showJoinCodeDialog = true },
            onJoinMeeting = onJoinMeeting,
            modifier = modifier
        )
    }

    if (showJoinCodeDialog) {
        AlertDialog(
            onDismissRequest = { showJoinCodeDialog = false },
            title = { Text("Join with a code") },
            text = {
                Column {
                    Text(
                        text = "Enter the meeting code provided by the organizer (e.g. mqt-dync-pro):",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = joinCodeInput,
                        onValueChange = { joinCodeInput = it },
                        placeholder = { Text("mqt-abc-xyz") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showJoinCodeDialog = false
                        val code = joinCodeInput.ifBlank { "mqt-custom-call" }
                        onJoinMeeting(
                            MeetingItem(
                                id = "custom",
                                title = "Meeting ($code)",
                                time = "Now",
                                duration = "Live",
                                code = code,
                                participants = listOf("Organizer", "You"),
                                isLiveNow = true
                            )
                        )
                    }
                ) {
                    Text("Join")
                }
            },
            dismissButton = {
                TextButton(onClick = { showJoinCodeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun MeetHomeView(
    meetings: List<MeetingItem>,
    onStartInstantMeeting: () -> Unit,
    onOpenJoinWithCode: () -> Unit,
    onJoinMeeting: (MeetingItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Video Meetings",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Connect, collaborate, and celebrate from anywhere with Mail Quite Meet.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons Row: [New Meeting] [Join with a code]
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onStartInstantMeeting,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("start_instant_meeting_btn"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MailBlue)
            ) {
                Icon(imageVector = Icons.Default.VideoCall, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("New meeting", fontWeight = FontWeight.SemiBold)
            }

            OutlinedButton(
                onClick = onOpenJoinWithCode,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("join_with_code_btn"),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Join with a code", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Scheduled for today",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(meetings, key = { it.id }) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${item.time} • ${item.duration}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Button(
                                onClick = { onJoinMeeting(item) },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (item.isLiveNow) Color(0xFF34A853) else MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(if (item.isLiveNow) "Join Now" else "Join")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.participants.joinToString(", "),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveVideoMeetingView(
    meeting: MeetingItem,
    isMicMuted: Boolean,
    isCameraOn: Boolean,
    onLeaveMeeting: () -> Unit,
    onToggleMic: () -> Unit,
    onToggleCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onLeaveMeeting() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1E1F22))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Meeting top details
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = meeting.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Meeting Code: ${meeting.code}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.LightGray
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF34A853).copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "● LIVE",
                        color = Color(0xFF81C784),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Main Video Stage Tile
            Surface(
                modifier = Modifier
                    .size(200.dp),
                shape = CircleShape,
                color = if (isCameraOn) MailBlue.copy(alpha = 0.3f) else Color(0xFF2B2D31)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (isCameraOn) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Camera Active",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        Text(
                            text = "F",
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Farhan Rahman (You)",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            if (isMicMuted) {
                Text(
                    text = "Microphone is muted",
                    color = MailRed,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // In-Call Action Control Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mic toggle
                IconButton(
                    onClick = onToggleMic,
                    modifier = Modifier.size(56.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (isMicMuted) MailRed else Color(0xFF3C4043)
                    )
                ) {
                    Icon(
                        imageVector = if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Toggle Mic",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Camera toggle
                IconButton(
                    onClick = onToggleCamera,
                    modifier = Modifier.size(56.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (!isCameraOn) MailRed else Color(0xFF3C4043)
                    )
                ) {
                    Icon(
                        imageVector = if (isCameraOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                        contentDescription = "Toggle Video",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // End call button
                IconButton(
                    onClick = onLeaveMeeting,
                    modifier = Modifier.size(56.dp),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MailRed)
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "Leave Call",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
