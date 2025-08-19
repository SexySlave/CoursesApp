package com.sexyslave.coursesapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

object FavoritesScreen : Screen {
    @Composable
    override fun Content() {
        FavoritesScreenContent()
    }
}

@Composable
fun FavoritesScreenContent(viewModel: CoursesViewModel = koinViewModel()) {
    var selectedItemIndex by remember { mutableStateOf(1) }
    val coursesState by viewModel.coursesState.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
    ) {

        Text(
            text = "Избранное",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 48.dp, bottom = 16.dp)
        )


        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (val state = coursesState) {
                is CoursesUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is CoursesUiState.Success -> {
                    val favoriteCourses = state.courses.filter { it.hasLike }
                    if (favoriteCourses.isEmpty()) {
                        Text(
                            text = "Нет избранных курсов",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(favoriteCourses, key = { course -> course.id }) { course ->
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
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
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
                onClick = {
                    selectedItemIndex = 0
                    // Check if HomeScreen is already in the stack and not the current screen
                    val homeScreenInStack =
                        navigator.items.any { it is HomeScreen && it != navigator.lastItem }
                    if (navigator.canPop && homeScreenInStack) {
                        navigator.popUntil { it is HomeScreen }
                    } else {
                        navigator.push(HomeScreen)
                    }
                },

//                onClick = {
//                    selectedItemIndex = 0
//                    navigator.push(HomeScreen)
//                },

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
                onClick = { selectedItemIndex = 1 /* No action needed, already here */ },
                icon = {
                    Icon(
                        painterResource(R.drawable.bookmark_24dp_ffffff),
                        contentDescription = "Избранное",
                        modifier = Modifier.size(24.dp)
                    )
                },
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
                onClick = { selectedItemIndex = 2 /* TODO: Navigate to AccountScreen */ },
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


