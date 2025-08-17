package com.sexyslave.coursesapp

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults // Добавлен новый импорт
import androidx.compose.material3.Text
//import androidx.compose.material3.TextFieldDefaults // Удален старый импорт
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log

@Composable
fun LoginFields(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Поле Email
        Text(
            text = "Email",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text("example@gmail.com") },
            singleLine = true,
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(vertical = 6.dp),
            colors = OutlinedTextFieldDefaults.colors( // Используем OutlinedTextFieldDefaults.colors
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF2C2C2C),
                unfocusedContainerColor = Color(0xFF2C2C2C),
                focusedBorderColor = Color.Green,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Green,
                focusedPlaceholderColor = Color(0xFFAAAAAA),
                unfocusedPlaceholderColor = Color(0xFFAAAAAA)
            )
        )

        // Поле Пароль
        Text(
            text = "Пароль",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = { Text("Введите пароль") },
            singleLine = true,
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(vertical = 6.dp),
            colors = OutlinedTextFieldDefaults.colors( // Используем OutlinedTextFieldDefaults.colors
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF2C2C2C),
                unfocusedContainerColor = Color(0xFF2C2C2C),
                focusedBorderColor = Color.Green,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Green,
                focusedPlaceholderColor = Color(0xFFAAAAAA),
                unfocusedPlaceholderColor = Color(0xFFAAAAAA)
            ),
            visualTransformation = PasswordVisualTransformation()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onVkClick: (url: String) -> Unit,
    onOkClick: (url: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val vkUrl = "https://vk.com"
    val okUrl = "https://ok.ru"

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Вход",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 24.dp)
        )

        LoginFields(
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it }
        )

        Button(
            onClick = { onLoginClick(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(vertical = 12.dp)
                .align(Alignment.Start),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
        ) {
            Text(
                "Вход",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Нет аккаунта?", color = Color.Gray)
            Spacer(Modifier.width(4.dp))
            Text(
                text = "Регистрация",
                color = Color(0xFF00C853),
                modifier = Modifier.clickable { onRegisterClick() }
            )
        }

        Text(
            text = "Забыл пароль",
            color = Color(0xFF00C853),
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { onForgotPasswordClick() }
        )

        Spacer(Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        ) {
            Divider(
                color = Color.DarkGray,
                thickness = 1.dp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "или",
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Divider(
                color = Color.DarkGray,
                thickness = 1.dp,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(vkUrl))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Не удалось открыть ВК: $vkUrl", e)
                    }
                    onVkClick(vkUrl)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2787F5)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp) // Добавлена высота
                    .padding(horizontal = 4.dp)
            ) {
                Text("ВК", color = Color.White)
            }
            Button(
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(okUrl))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Не удалось открыть ОК: $okUrl", e)
                    }
                    onOkClick(okUrl)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7700)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp) // Добавлена высота
                    .padding(horizontal = 4.dp)
            ) {
                Text("ОК", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
fun DefaultPreview() {
    LoginScreen(
        onLoginClick = { _, _ -> },
        onRegisterClick = {},
        onForgotPasswordClick = {},
        onVkClick = {},
        onOkClick = {}
    )
}
