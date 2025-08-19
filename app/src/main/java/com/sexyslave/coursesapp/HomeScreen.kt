package com.sexyslave.coursesapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sexyslave.coursesapp.features.courses.CoursesUiState
import com.sexyslave.coursesapp.features.courses.CoursesViewModel
import com.sexyslave.domain.model.Course
import org.koin.androidx.compose.koinViewModel

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        CoursesScreen()
    }
}

@Composable
fun CoursesScreen(viewModel: CoursesViewModel = koinViewModel()) {
    var selectedItemIndex by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    val coursesState by viewModel.coursesState.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 36.dp, end = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                placeholder = { Text("Search courses...", style = TextStyle(fontSize = 16.sp, platformStyle = PlatformTextStyle(includeFontPadding = false))) },
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.search_24dp_ffffff), contentDescription = "Search Icon", modifier = Modifier.size(24.dp), tint = Color.White)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                textStyle = TextStyle(fontSize = 16.sp, platformStyle = PlatformTextStyle(includeFontPadding = false)),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedContainerColor = Color(0xFF2C2C2C),
                    unfocusedContainerColor = Color(0xFF2C2C2C),
                    disabledContainerColor = Color(0xFF2C2C2C),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedLeadingIconColor = Color(0xFF9E9E9E),
                    unfocusedLeadingIconColor = Color(0xFF9E9E9E),
                    focusedPlaceholderColor = Color(0xFF9E9E9E),
                    unfocusedPlaceholderColor = Color(0xFF9E9E9E)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color = Color(0xFF2C2C2C), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {  }) {
                    Icon(
                        painter = painterResource(R.drawable.filter_alt_24dp_ffffff),
                        contentDescription = "Filter courses",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                modifier = Modifier
                    .background(color = Color(0x002C2C2C), shape = RoundedCornerShape(8.dp))
                    .clickable { viewModel.sortCoursesByPublishDate() }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "По дате добавления",
                    color = Color(0xFF12B956),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(R.drawable.swap_vert_24dp_ffffff),
                    contentDescription = "Изменить сортировку",
                    tint = Color(0xFF12B956),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (val state = coursesState) {
                is CoursesUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is CoursesUiState.Success -> {
                    if (state.courses.isEmpty()) {
                        Text(
                            text = "Нет доступных курсов",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.courses, key = { course -> course.id }) { course ->
                                CourseCard(course = course, viewModel = viewModel)
                            }
                        }
                    }
                }
                is CoursesUiState.Error -> {
                    Text(
                        text = "Ошибка: ${state.message}",
                        color = Color.Red,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
            }
        }

        Divider(color = Color.DarkGray, thickness = 1.dp)
        NavigationBar(
            containerColor = Color(0xFF2C2C2C),
        ) {
            NavigationBarItem(
                selected = selectedItemIndex == 0,
                onClick = { selectedItemIndex = 0 },
                icon = { Icon(Icons.Outlined.Home, contentDescription = "Главная") },
                label = { Text("Главная") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF12B956),
                    unselectedIconColor = Color.LightGray,
                    selectedTextColor = Color(0xFF12B956),
                    unselectedTextColor = Color.LightGray,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedItemIndex == 1,
                onClick =
                    { selectedItemIndex = 1
                        navigator.push(FavoritesScreen)
                          },
                icon = { Icon(painterResource(R.drawable.bookmark_24dp_ffffff), contentDescription = "Избранное", modifier = Modifier.size(24.dp)) },
                label = { Text("Избранное") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF12B956),
                    unselectedIconColor = Color.LightGray,
                    selectedTextColor = Color(0xFF12B956),
                    unselectedTextColor = Color.LightGray,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedItemIndex == 2,
                onClick = { selectedItemIndex = 2 },
                icon = { Icon(Icons.Outlined.Person, contentDescription = "Аккаунт") },
                label = { Text("Аккаунт") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF12B956),
                    unselectedIconColor = Color.LightGray,
                    selectedTextColor = Color(0xFF12B956),
                    unselectedTextColor = Color.LightGray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun CourseCard(course: Course, viewModel: CoursesViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {  },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter =  painterResource(if (course.id.toInt() % 3 == 0) R.drawable.card1  else if (course.id.toInt() % 3 == 1) R.drawable.card2 else R.drawable.card3),
                    contentDescription = course.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(2.dp)
                ) {
                    IconButton(
                        onClick = { viewModel.toggleFavorite(course.id) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(if (course.hasLike) R.drawable.bookmark_24dp_1f1f1f else R.drawable.bookmark_24dp_ffffff),
                            contentDescription = "Add to favorites",
                            tint = if (course.hasLike) Color(0xFF12B956) else Color.White,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.star_on),
                            contentDescription = "Rating",
                            tint = Color(0xFF12B956),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${course.rating}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = run {
                                val parts = course.startDate.split("-")
                                val year = parts.getOrNull(0) ?: ""
                                val monthNumber = parts.getOrNull(1)?.toIntOrNull()
                                val day = parts.getOrNull(2)?.let { if (it.length == 2 && it.startsWith("0")) it.substring(1) else it } ?: ""

                                val monthName = when (monthNumber) {
                                    1 -> "января"
                                    2 -> "февраля"
                                    3 -> "марта"
                                    4 -> "апреля"
                                    5 -> "мая"
                                    6 -> "июня"
                                    7 -> "июля"
                                    8 -> "августа"
                                    9 -> "сентября"
                                    10 -> "октября"
                                    11 -> "ноября"
                                    12 -> "декабря"
                                    else -> ""
                                }
                                if (day.isNotEmpty() && monthName.isNotEmpty() && year.isNotEmpty()) {
                                    "$day $monthName $year"
                                } else {
                                    course.publishDate 
                                }
                            },
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Text(
                text = course.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
            )
            Text(
                text = course.description,
                color = Color.LightGray,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${course.price} ₽", color = Color.White, fontWeight = FontWeight.Bold)
                Text(
                    "Подробнее ➜",
                    color = Color(0xFF12B956),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {  }
                )
            }
        }
    }
}
