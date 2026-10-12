package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.EmailFolder
import com.example.data.model.UserAccount
import com.example.ui.theme.MailBlue
import com.example.ui.theme.MailBlueDark

@Composable
fun NavigationDrawerContent(
    currentFolder: EmailFolder,
    activeAccount: UserAccount? = null,
    onSelectFolder: (EmailFolder) -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenDrive: () -> Unit,
    onCreateAccountClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(320.dp)
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_mail_quite_logo),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Mail Quite",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MailBlueDark
                    )
                    Text(
                        text = "Your Mail, Your Way",
                        style = MaterialTheme.typography.labelSmall,
                        color = MailBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(modifier = Modifier.padding(horizontal = 12.dp))
            Spacer(modifier = Modifier.height(8.dp))

            if (activeAccount != null) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = Color(activeAccount.avatarColorHex)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = activeAccount.avatarLetter,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = activeAccount.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = activeAccount.email,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clickable {
                            onCloseDrawer()
                            onSignInClick()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inbox,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sign in to another account",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clickable {
                            onCloseDrawer()
                            onCreateAccountClick()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null,
                            tint = MailBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Create Google Account",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MailBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Divider(modifier = Modifier.padding(horizontal = 12.dp))
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = "INBOXES",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
            )

            DrawerFolderItem(
                label = "Primary",
                icon = Icons.Default.Inbox,
                isSelected = currentFolder == EmailFolder.PRIMARY,
                onClick = {
                    onSelectFolder(EmailFolder.PRIMARY)
                    onCloseDrawer()
                }
            )

            DrawerFolderItem(
                label = "Promotions",
                icon = Icons.Default.LocalOffer,
                isSelected = currentFolder == EmailFolder.PROMOTIONS,
                onClick = {
                    onSelectFolder(EmailFolder.PROMOTIONS)
                    onCloseDrawer()
                }
            )

            DrawerFolderItem(
                label = "Social",
                icon = Icons.Default.People,
                isSelected = currentFolder == EmailFolder.SOCIAL,
                onClick = {
                    onSelectFolder(EmailFolder.SOCIAL)
                    onCloseDrawer()
                }
            )

            DrawerFolderItem(
                label = "Updates",
                icon = Icons.Default.Update,
                isSelected = currentFolder == EmailFolder.UPDATES,
                onClick = {
                    onSelectFolder(EmailFolder.UPDATES)
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider(modifier = Modifier.padding(horizontal = 12.dp))
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ALL LABELS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
            )

            DrawerFolderItem(
                label = "Starred",
                icon = Icons.Default.Star,
                isSelected = currentFolder == EmailFolder.STARRED,
                onClick = {
                    onSelectFolder(EmailFolder.STARRED)
                    onCloseDrawer()
                }
            )

            DrawerFolderItem(
                label = "Snoozed",
                icon = Icons.Default.Schedule,
                isSelected = currentFolder == EmailFolder.SNOOZED,
                onClick = {
                    onSelectFolder(EmailFolder.SNOOZED)
                    onCloseDrawer()
                }
            )

            DrawerFolderItem(
                label = "Sent",
                icon = Icons.Default.Send,
                isSelected = currentFolder == EmailFolder.SENT,
                onClick = {
                    onSelectFolder(EmailFolder.SENT)
                    onCloseDrawer()
                }
            )

            DrawerFolderItem(
                label = "Drafts",
                icon = Icons.Default.Drafts,
                isSelected = currentFolder == EmailFolder.DRAFTS,
                onClick = {
                    onSelectFolder(EmailFolder.DRAFTS)
                    onCloseDrawer()
                }
            )

            DrawerFolderItem(
                label = "Spam",
                icon = Icons.Default.Report,
                isSelected = currentFolder == EmailFolder.SPAM,
                onClick = {
                    onSelectFolder(EmailFolder.SPAM)
                    onCloseDrawer()
                }
            )

            DrawerFolderItem(
                label = "Trash",
                icon = Icons.Default.Delete,
                isSelected = currentFolder == EmailFolder.TRASH,
                onClick = {
                    onSelectFolder(EmailFolder.TRASH)
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider(modifier = Modifier.padding(horizontal = 12.dp))
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "WORKSPACE APPS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
            )

            DrawerFolderItem(
                label = "Calendar",
                icon = Icons.Default.CalendarMonth,
                isSelected = false,
                badge = "Schedule",
                onClick = {
                    onOpenCalendar()
                    onCloseDrawer()
                }
            )

            DrawerFolderItem(
                label = "Drive",
                icon = Icons.Default.Cloud,
                isSelected = false,
                badge = "Files",
                onClick = {
                    onOpenDrive()
                    onCloseDrawer()
                }
            )
        }
    }
}

@Composable
private fun DrawerFolderItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    badge: String? = null,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )
                if (badge != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        selected = isSelected,
        onClick = onClick,
        modifier = Modifier
            .padding(NavigationDrawerItemDefaults.ItemPadding)
            .testTag("drawer_item_$label"),
        shape = RoundedCornerShape(24.dp)
    )
}
