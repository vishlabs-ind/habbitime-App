package com.rach.habitchange.presentations.uiComponents


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SideNavigationBar() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                OutlinedCard(modifier = Modifier.padding(10.dp)) {
                    Image(
                        imageVector = Icons.Default.Home,
                        contentDescription = "logo",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(10.dp),

                        )
                }

                Text(
                    "Home",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 20.dp)
                )

            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedCard(modifier = Modifier.padding(10.dp)) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = " Send Icon",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(60.dp)
                                .padding(10.dp)
                        )
                    }
                    Text(
                        "Shear",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }

            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedCard(modifier = Modifier.padding(10.dp)) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = " Rate Us Icon",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(60.dp)
                                .padding(10.dp)
                        )
                    }
                    Text(
                        "Rate Us",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedCard(modifier = Modifier.padding(10.dp)) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = " Privacy Policy Icon",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(60.dp)
                                .padding(10.dp)
                        )
                    }

                    Text(
                        "Privacy Policy",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 20.dp)


                    )
                }

            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedCard(modifier = Modifier.padding(10.dp)) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(60.dp)
                                .padding(10.dp)
                        )
                    }

                    Text(
                        "About App",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        }


    }


}
