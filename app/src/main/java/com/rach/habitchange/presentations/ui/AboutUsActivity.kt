package com.rach.habitchange.presentations.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rach.habitchange.R
import com.rach.habitchange.presentations.uiComponents.CustomTopAppBar
import com.rach.habitchange.theme.HabitChangeTheme

class AboutUsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            HabitChangeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AboutUsScreen() {
                        (context as? Activity)?.finish()
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(onBackClick: () -> Unit = {}) {
    Scaffold(
        topBar = {

            CustomTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = "About Us",
                onNavigationIconClick = {
                    onBackClick()
                }
            )
        },

        ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo/Icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = "Habit Change",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Track Your Time • Build Better Habits",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // What We Do Section
            SectionCard(
                title = "What We Do",
                content = "HabitTracker helps you understand how you spend your time. Track the hours you dedicate to different activities, build positive habits, and gain insights into your daily routines. Whether you're learning a new skill, exercising, or managing work-life balance, we make time tracking simple and insightful."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mission Section
            SectionCard(
                title = "Our Mission",
                content = "To empower people to make conscious choices about how they spend their time. We believe that awareness is the first step to positive change, and by tracking your hours, you can identify patterns, celebrate progress, and achieve your personal goals."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Features Section
            Text(
                text = "Key Features",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            FeatureItem(
                icon = Icons.Default.DateRange,
                title = "Hour Tracking",
                description = "Log how many hours you spend on each activity"
            )

            Spacer(modifier = Modifier.height(12.dp))

            FeatureItem(
                icon = Icons.Default.MailOutline,
                title = "Progress Analytics",
                description = "View detailed statistics and trends over time"
            )

            Spacer(modifier = Modifier.height(12.dp))

            FeatureItem(
                icon = Icons.Default.Notifications,
                title = "Smart Reminders",
                description = "Get notified to log your activities and stay consistent"
            )

            Spacer(modifier = Modifier.height(12.dp))

            FeatureItem(
                icon = Icons.Default.Face,
                title = "Goals & Milestones",
                description = "Set targets and celebrate achievements"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Why Choose Us
            SectionCard(
                title = "Why Choose Habit Change?",
                content = "• Simple & intuitive interface\n" +
                        "• Detailed insights and reports\n" +
                        "• Privacy-focused - your data stays yours\n" +
                        "• Customizable categories and goals\n" +
                        "• Track unlimited habits and activities"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Contact Section
            Text(
                text = "Get in Touch",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactInfoItem(
                icon = Icons.Default.Email,
                text = "support@habitchange.com"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ContactInfoItem(
                icon = Icons.Default.AccountBox,
                text = "Share feedback and suggestions"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Version Info
            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SectionCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight.times(1.5f)
            )
        }
    }
}

@Composable
fun FeatureItem(icon: ImageVector, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ContactInfoItem(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}