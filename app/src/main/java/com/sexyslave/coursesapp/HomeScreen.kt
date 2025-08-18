package com.sexyslave.coursesapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // Для Modifier.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List

import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star

import androidx.compose.material.icons.outlined.FavoriteBorder // Для "Избранное" в карточке
import androidx.compose.material.icons.outlined.Home // Для "Главная"
import androidx.compose.material.icons.outlined.Person // Для "Аккаунт"
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
// import androidx.compose.ui.graphics.vector.ImageVector // Не используется напрямую ImageVector для painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle // Добавлен импорт
import androidx.compose.ui.text.TextStyle // Добавлен импорт
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        CoursesScreen()
    }
}

@Composable
fun CoursesScreen() {
    var selectedItemIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // 🔎 SearchBar + Filter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 36.dp, end = 12.dp, bottom = 12.dp), // Увеличен верхний отступ
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = "", // Состояние для текста поиска нужно будет добавить
                onValueChange = { /* TODO: обновить состояние текста поиска */ },
                placeholder = { Text("Search courses...", style = TextStyle(fontSize = 16.sp, platformStyle = PlatformTextStyle(includeFontPadding = false))) }, // Размер шрифта увеличен и убраны отступы
                leadingIcon = {
                    Icon(painter =  painterResource(R.drawable.search_24dp_ffffff), contentDescription = "Search Icon", modifier = Modifier.size(24.dp),  tint = Color.White)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp), // Округлая форма
                textStyle = TextStyle(fontSize = 16.sp, platformStyle = PlatformTextStyle(includeFontPadding = false)), // Добавлено для основного текста и плейсхолдера, убраны отступы
                colors = TextFieldDefaults.colors( // Обновленные цвета
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedContainerColor = Color(0xFF2C2C2C), // Цвет фона как у навигации
                    unfocusedContainerColor = Color(0xFF2C2C2C),
                    disabledContainerColor = Color(0xFF2C2C2C),
                    focusedIndicatorColor = Color.Transparent, // Без подчеркивания
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedLeadingIconColor = Color(0xFF9E9E9E),
                    unfocusedLeadingIconColor = Color(0xFF9E9E9E),
                    focusedPlaceholderColor = Color(0xFF9E9E9E),
                    unfocusedPlaceholderColor = Color(0xFF9E9E9E)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box( // Обертка для круглой кнопки фильтра
                modifier = Modifier
                    .size(48.dp) // Размер соответствует высоте TextField
                    .background(color = Color(0xFF2C2C2C), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { /* TODO: логика фильтрации */ }) {
                    Icon(
                        painter =  painterResource(R.drawable.filter_alt_24dp_ffffff), // Иконка фильтра обновлена
                        contentDescription = "Filter courses",
                        tint = Color.White // Белая иконка,
                        , modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Row( // Родительская Row для выравнивания
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp), // Отступы от краев экрана и снизу
            horizontalArrangement = Arrangement.End // Выравниваем кнопку по правому краю
        ) {
            Row( // Сама кнопка сортировки
                modifier = Modifier
                    .background(color = Color(0x002C2C2C), shape = RoundedCornerShape(8.dp))
                    .clickable { /* TODO: Добавить логику смены сортировки или открытия меню сортировки */ }
                    .padding(horizontal = 12.dp, vertical = 6.dp), // Внутренние отступы для текста и иконки
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "По дате добавления",
                    color = Color.Green, // Можно будет поменять на Color.White для лучшего контраста, если нужно
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(R.drawable.swap_vert_24dp_ffffff),
                    contentDescription = "Изменить сортировку",
                    tint = Color.Green, // Аналогично тексту
                    modifier = Modifier.size(18.dp)

                )
            }
        }

        // 📚 Список курсов
        LazyColumn(
            modifier = Modifier.weight(1f) // Даем LazyColumn весь доступный вес
        ) {
            items(sampleCourses) { course ->
                CourseCard(course)
            }
        }

        // ⬇️ Навигация снизу
        Divider(color = Color.DarkGray, thickness = 1.dp) // Верхняя серая полоска
        NavigationBar(
            containerColor = Color(0xFF2C2C2C), // Темно-серый фон бара
        ) {
            NavigationBarItem(
                selected = selectedItemIndex == 0,
                onClick = { selectedItemIndex = 0 },
                icon = { Icon(Icons.Outlined.Home, contentDescription = "Главная") },
                label = { Text("Главная") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF388E3C),
                    unselectedIconColor = Color.LightGray,
                    selectedTextColor = Color(0xFF388E3C),
                    unselectedTextColor = Color.LightGray,
                    indicatorColor = Color.Transparent // Убираем фон индикатора
                )
            )
            NavigationBarItem(
                selected = selectedItemIndex == 1,
                onClick = { selectedItemIndex = 1 },
                icon = { Icon(painterResource( R.drawable.bookmark_24dp_ffffff), contentDescription = "Избранное", modifier = Modifier.size(24.dp))  },
                label = { Text("Избранное") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF388E3C),
                    unselectedIconColor = Color.LightGray,
                    selectedTextColor = Color(0xFF388E3C),
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
                    selectedIconColor = Color(0xFF388E3C),
                    unselectedIconColor = Color.LightGray,
                    selectedTextColor = Color(0xFF388E3C),
                    unselectedTextColor = Color.LightGray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun CourseCard(course: Course) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(12.dp), // Общее закругление карточки
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
    ) {
        Column {
            Box( // Контейнер для изображения и оверлеев
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp)) // Закругление всех углов контейнера картинки
            ) {
                Image(
                    painter = painterResource(R.drawable.card1), // Используем course.imageRes
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Кнопка "Избранное" в правом верхнем углу
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp) // Уменьшенный отступ от краев картинки
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp) // Немного увеличим закругление фона кнопки
                        )
                        .padding(2.dp) // Уменьшенный внутренний отступ для иконки
                ) {
                    IconButton(
                        onClick = { /* TODO: Handle favorite click */ },
                        modifier = Modifier.size(20.dp) // Явно уменьшим размер IconButton
                    ) {
                        Icon(
                            painter = painterResource( R.drawable.bookmark_24dp_ffffff), // Используем drawable для карточки
                            contentDescription = "Add to favorites",
                            tint = Color.White,
                            modifier = Modifier.fillMaxSize() // Иконка заполняет IconButton
                        )
                    }
                }

                // Оверлей с рейтингом и датой в левом нижнем углу
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Фон для звезд и рейтинга
                    Row(
                        modifier = Modifier
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp) // Увеличенное закругление
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp), // Немного адаптируем паддинг
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Green,
                            modifier = Modifier.size(14.dp) // Немного уменьшим звезду
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

                    // Фон для даты
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp) // Увеличенное закругление
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp) // Немного адаптируем паддинг
                    ) {
                        Text(
                            text = course.date,
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
                    color = Color(0xFF388E3C),
                    fontWeight = FontWeight.Medium,
                    modifier = androidx . compose . ui . Modifier.clickable { /* TODO: Handle details click */ }
                )
            }
        }
    }
}


data class Course(
    val title: String,
    val description: String,
    val price: String,
    val rating: Double,
    val date: String,
    val imageRes: Int
)

val sampleCourses = listOf(
    Course(
        "Java-разработчик с нуля",
        "Освойте backend-разработку и программирование на Java...",
        "999",
        4.9,
        "22 Мая 2024",
        android.R.drawable.ic_menu_gallery // Placeholder, замените на ваши ресурсы
    ),
    Course(
        "3D-дженералист",
        "Освой профессию 3D-дженералиста и стань универсальным специалистом...",
        "12000",
        3.9,
        "10 Сентября 2024",
        android.R.drawable.ic_menu_gallery // Placeholder, замените на ваши ресурсы
    )
)
