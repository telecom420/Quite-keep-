package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import com.example.ui.theme.MailBlue

@Composable
fun ComposeScreen(
    userEmail: String = "oiki89663@gmail.com",
    onSend: (to: String, subject: String, body: String, attachment: String?) -> Unit,
    onSaveDraft: (to: String, subject: String, body: String) -> Unit,
    onDiscard: () -> Unit,
    modifier: Modifier = Modifier
) {
    var toText by remember { mutableStateOf("") }
    var subjectText by remember { mutableStateOf("") }
    var bodyText by remember { mutableStateOf("") }
    var attachmentName by remember { mutableStateOf<String?>(null) }
    var showDiscardDialog by remember { mutableStateOf(false) }

    fun handleBack() {
        if (toText.isNotBlank() || subjectText.isNotBlank() || bodyText.isNotBlank()) {
            showDiscardDialog = true
        } else {
            onDiscard()
        }
    }

    BackHandler { handleBack() }

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Save draft?") },
            text = { Text("Do you want to save this email as a draft or discard it?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDiscardDialog = false
                        onSaveDraft(toText, subjectText, bodyText)
                    }
                ) {
                    Text("Save Draft")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDiscardDialog = false
                        onDiscard()
                    }
                ) {
                    Text("Discard", color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { handleBack() },
                modifier = Modifier.testTag("compose_close_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "Compose",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            // Attachment button
            IconButton(
                onClick = {
                    attachmentName = if (attachmentName == null) "Project_Document.pdf" else null
                },
                modifier = Modifier.testTag("compose_attach_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = "Attach file",
                    tint = if (attachmentName != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Send button
            IconButton(
                onClick = {
                    if (toText.isNotBlank() || subjectText.isNotBlank() || bodyText.isNotBlank()) {
                        onSend(
                            toText.ifBlank { "team@mailquite.app" },
                            subjectText.ifBlank { "(no subject)" },
                            bodyText.ifBlank { "Sent from Mail Quite on Android" },
                            attachmentName
                        )
                    }
                },
                modifier = Modifier.testTag("compose_send_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = MailBlue
                )
            }
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        // From row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "From",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(60.dp)
            )
            Text(
                text = userEmail,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

        // To row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "To",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(60.dp)
            )
            OutlinedTextField(
                value = toText,
                onValueChange = { toText = it },
                placeholder = { Text("Add recipient email") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("compose_to_input"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

        // Subject row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Subject",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(60.dp)
            )
            OutlinedTextField(
                value = subjectText,
                onValueChange = { subjectText = it },
                placeholder = { Text("Subject") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("compose_subject_input"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

        // Smart templates row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MailBlue,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Smart suggestions:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))

            SuggestionChip(
                onClick = {
                    subjectText = "Status Update & Sprint Summary"
                    bodyText = "Hi Team,\n\nHere is a quick overview of our recent progress. Deliverables are on track and testing completed.\n\nBest,\nFarhan"
                },
                label = { Text("Status Update") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            SuggestionChip(
                onClick = {
                    subjectText = "Meeting Follow-up & Next Steps"
                    bodyText = "Hi everyone,\n\nThank you for today's productive discussion. Attached are the summarized action items and schedule.\n\nRegards,\nFarhan"
                },
                label = { Text("Follow-up") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            SuggestionChip(
                onClick = {
                    subjectText = "Thank You!"
                    bodyText = "Hi,\n\nJust wanted to thank you for your help with the recent milestone. Really appreciate your support!\n\nBest,\nFarhan"
                },
                label = { Text("Say Thank You") }
            )
        }

        // Attached file badge (if attached)
        if (attachmentName != null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = attachmentName ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { attachmentName = null },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove attachment",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Email Body Input
        OutlinedTextField(
            value = bodyText,
            onValueChange = { bodyText = it },
            placeholder = { Text("Compose email...") },
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("compose_body_input"),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
