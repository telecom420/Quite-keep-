package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmailItem
import com.example.ui.theme.StarGold

@Composable
fun EmailListItem(
    email: EmailItem,
    onClick: () -> Unit,
    onStarToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("email_item_${email.id}"),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar circle
        if (email.isGoogleLogo) {
            GoogleAvatar(modifier = Modifier.size(40.dp))
        } else {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = Color(email.avatarColorHex)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = email.avatarLetter,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Content Column
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            // Sender & Time Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = email.sender,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = if (!email.isRead) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (!email.isRead) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = email.timeFormatted,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (!email.isRead) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        fontWeight = if (!email.isRead) FontWeight.Bold else FontWeight.Normal
                    )
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Subject Line
            Text(
                text = email.subject,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (!email.isRead) FontWeight.Bold else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Snippet Preview
            Text(
                text = email.snippet,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Attachment chip (if present)
            if (email.hasAttachment && email.attachmentName != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = "Attachment",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = email.attachmentName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // Star Icon Button
        IconButton(
            onClick = onStarToggle,
            modifier = Modifier
                .size(36.dp)
                .testTag("star_btn_${email.id}")
        ) {
            Icon(
                imageVector = if (email.isStarred) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = if (email.isStarred) "Starred" else "Not starred",
                tint = if (email.isStarred) StarGold else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/**
 * Signature multi-colored Google 'G' circle logo as displayed in the screenshot for Google emails.
 */
@Composable
fun GoogleAvatar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Canvas(modifier = Modifier.padding(9.dp)) {
            val strokeWidth = size.width * 0.22f
            val radius = (size.width - strokeWidth) / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            val rect = androidx.compose.ui.geometry.Rect(
                center.x - radius,
                center.y - radius,
                center.x + radius,
                center.y + radius
            )

            // Red arc (top)
            drawArc(
                color = Color(0xFFEA4335),
                startAngle = 210f,
                sweepAngle = 105f,
                useCenter = false,
                topLeft = rect.topLeft,
                size = rect.size,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Yellow arc (left-bottom)
            drawArc(
                color = Color(0xFFFBBC05),
                startAngle = 135f,
                sweepAngle = 80f,
                useCenter = false,
                topLeft = rect.topLeft,
                size = rect.size,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Green arc (bottom)
            drawArc(
                color = Color(0xFF34A853),
                startAngle = 45f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = rect.topLeft,
                size = rect.size,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Blue horizontal bar & right arc
            drawArc(
                color = Color(0xFF4285F4),
                startAngle = 315f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = rect.topLeft,
                size = rect.size,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            // Center blue crossbar
            drawLine(
                color = Color(0xFF4285F4),
                start = Offset(center.x, center.y),
                end = Offset(center.x + radius, center.y),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}
