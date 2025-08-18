//package com.sexyslave.coursesapp
//
//
//import android.content.Intent
//import android.net.Uri
//import androidx.compose.foundation.background
//// import androidx.compose.foundation.clickable // Удалено, так как кнопки "Регистрация" и "Забыл пароль" неактивны
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Divider
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
// // Added import for FilterList
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.style.TextOverflow
//import java.util.regex.Pattern
//
//@Composable
//fun CoursesScreen() {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//    ) {
//        // 🔎 SearchBar + Sort
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            TextField(
//                value = "",
//                onValueChange = {},
//                placeholder = { Text("Search courses...", color = Color.Gray) },
//                leadingIcon = {
//                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
//                },
//                modifier = Modifier
//                    .weight(1f)
//                    .height(48.dp),
////                colors = TextFieldDefaults.textFieldColors(
////                    containerColor = Color.DarkGray,
////                    focusedIndicatorColor = Color.Transparent,
////                    unfocusedIndicatorColor = Color.Transparent,
////                    textColor = Color.White
////                )
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            IconButton(onClick = { /* TODO: сортировка */ }) {
//                Icon(Icons.Filled.Favorite, contentDescription = null, tint = Color.White)
//            }
//        }
//
//        Text(
//            text = "По дате добавления",
//            color = Color.Green,
//            fontSize = 14.sp,
//            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
//        )
//
//        // 📚 Список курсов
//        LazyColumn(
//            modifier = Modifier.weight(1f)
//        ) {
//            items(sampleCourses) { course ->
//                CourseCard(course)
//            }
//        }
//
//        // ⬇️ Навигация снизу
//        NavigationBar(
//            containerColor = Color.Black,
//            // contentColor = Color.White // contentColor is not a direct parameter, use NavigationBarItem colors
//        ) {
//            NavigationBarItem(
//                selected = true,
//                onClick = {},
//                icon = { Icon(Icons.Default.Home, contentDescription = null) },
//                label = { Text("Главная") }
//                // colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.White, unselectedIconColor = Color.Gray, selectedTextColor = Color.White, unselectedTextColor = Color.Gray)
//            )
//            NavigationBarItem(
//                selected = false,
//                onClick = {},
//                icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
//                label = { Text("Избранное") }
//                // colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.White, unselectedIconColor = Color.Gray, selectedTextColor = Color.White, unselectedTextColor = Color.Gray)
//            )
//            NavigationBarItem(
//                selected = false,
//                onClick = {},
//                icon = { Icon(Icons.Default.Person, contentDescription = null) },
//                label = { Text("Аккаунт") }
//                // colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.White, unselectedIconColor = Color.Gray, selectedTextColor = Color.White, unselectedTextColor = Color.Gray)
//            )
//        }
//    }
//}
//
//@Composable
//fun CourseCard(course: Course) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(12.dp),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
//    ) {
//        Column {
//            // 🖼️ Картинка
//            Image(
//                painter = painterResource(course.imageRes),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(160.dp)
//            )
//
//            // ⭐ Рейтинг и дата
//            Row(
//                modifier = Modifier.padding(8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow)
//                Text(
//                    text = "${course.rating}",
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(start = 4.dp)
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//                Text(course.date, color = Color.Gray, fontSize = 12.sp)
//            }
//
//            // 📖 Название
//            Text(
//                text = course.title,
//                color = Color.White,
//                fontWeight = FontWeight.Bold,
//                fontSize = 18.sp,
//                modifier = Modifier.padding(horizontal = 8.dp)
//            )
//
//            // 📄 Краткое описание
//            Text(
//                text = course.description,
//                color = Color.LightGray,
//                fontSize = 14.sp,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                modifier = Modifier.padding(8.dp)
//            )
//
//            // 💰 Цена + Подробнее
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text("${course.price} ₽", color = Color.White, fontWeight = FontWeight.Bold)
//                Text(
//                    "Подробнее ➜",
//                    color = Color.Green,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//        }
//    }
//}
//
//data class Course(
//    val title: String,
//    val description: String,
//    val price: String,
//    val rating: Double,
//    val date: String,
//    val imageRes: Int
//)
//
//val sampleCourses = listOf(
//    Course(
//        "Java-разработчик с нуля",
//        "Освойте backend-разработку и программирование на Java...",
//        "999",
//        4.9,
//        "22 Мая 2024",
//        android.R.drawable.ic_menu_gallery
//    ),
//    Course(
//        "3D-дженералист",
//        "Освой профессию 3D-дженералиста и стань универсальным специалистом...",
//        "12000",
//        3.9,
//        "10 Сентября 2024",
//        android.R.drawable.ic_menu_gallery
//    )
//)
