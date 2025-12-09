package com.rach.habitchange.presentations.uiComponents


import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rach.habitchange.R
import com.rach.habitchange.presentations.ui.AboutUsActivity
import kotlinx.coroutines.launch

@Composable
fun SideNavigationBar(onItemClick: () -> Unit) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf("home") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationItems = remember {
        listOf(
            NavigationItem("home", "Home", Icons.Default.Home),
            NavigationItem("share", "Share", Icons.Default.Share),
            NavigationItem("rate us", "Rate Us", Icons.Default.Star),
            NavigationItem("privacy policy", "privacy policy", Icons.Default.Lock),
            NavigationItem("about us", "About Us", Icons.Default.Info)
        )
    }
    DrawerContent(
        navigationItems = navigationItems,
        selectedItem = selectedItem,
        onItemClick = { itemId ->
            when (itemId) {
                "home" -> { onItemClick() }
                "share" -> {
                    val shareText =
                        "Check out this app:\nhttps://play.google.com/store/apps/details?id=com.rach.habitchange"

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share app"))
                }

                "rate us" -> {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.rach.habitchange")
                    )
                    context.startActivity(intent)
                }

                "privacy policy" -> {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://sites.google.com/view/habitscreentimetracker/home?authuser=1")
                    )
                    context.startActivity(intent)
                }

                "about us" -> {

                    val intent = Intent(context, AboutUsActivity::class.java)

                    context.startActivity(intent)

                }
            }

        }
    )

}


data class NavigationItem(
    val id: String,
    val title: String,
    val icon: ImageVector
)


@Composable
fun DrawerContent(
    navigationItems: List<NavigationItem>,
    selectedItem: String,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Drawer Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFE7E77F),
                            Color(0x99F3CD5B)
                        )
                    )
                )
                .padding(20.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                   Image(
                       painter = painterResource(com.rach.core.R.drawable.ic_launcher_round),"", contentScale = ContentScale.Crop
                   )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Habit Change",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Navigation Items
        navigationItems.forEach { item ->
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 16.sp
                    )
                },
                selected = selectedItem == item.id,
                onClick = { onItemClick(item.id) },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color(0xFF6366F1).copy(alpha = 0.1f),
                    selectedIconColor = Color(0xFF6366F1),
                    selectedTextColor = Color(0xFF6366F1)
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

       /* HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color(0xFFEF4444)
                )
            },
            label = {
                Text(
                    text = "Logout",
                    fontSize = 16.sp,
                    color = Color(0xFFEF4444)
                )
            },
            selected = false,
            onClick = { *//* Handle logout *//* },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )*/

        Spacer(modifier = Modifier.height(16.dp))
    }
}



