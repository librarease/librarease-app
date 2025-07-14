package org.librarease.app.presentation.main.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.librarease.app.R

@Composable
fun LibraryCard(
    name: String,
    logo: String? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var elevated by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .width(160.dp)
            .height(200.dp)
            .padding(4.dp)
            .clickable { onClick() }
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (elevated) 12.dp else 4.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(id = R.color.primary).copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    colorResource(id = R.color.primary).copy(alpha = 0.2f),
                                    colorResource(id = R.color.primary).copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (logo != null && logo.isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(logo)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Library Logo",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.LocalLibrary,
                            contentDescription = "Library Icon",
                            modifier = Modifier
                                .size(40.dp)
                                .graphicsLayer {
                                    rotationZ = if (elevated) 5f else 0f
                                },
                            tint = colorResource(id = R.color.primary)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.primary).copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "View Details",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.primary)
                    )
                }
            }
        }
    }
}