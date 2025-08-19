package com.sexyslave.coursesapp

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
// import androidx.compose.foundation.clickable // Удалено, так как кнопки "Регистрация" и "Забыл пароль" неактивны
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import java.util.regex.Pattern


fun isEmailValid(email: String): Boolean {
    // Регулярное выражение для формата "текст@текст.текст" без кириллицы
    val emailRegex = Pattern.compile(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
    )
    return emailRegex.matcher(email).matches()
}

// Функция для фильтрации кириллицы
fun filterNonLatin(input: String): String {
    return input.filter { it.code < 128 } // Оставляем только символы ASCII
}

@Composable
fun LoginFields(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isEmailValid: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Поле Email
        Text(
            text = "Email",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { onEmailChange(filterNonLatin(it)) },
            placeholder = { Text("example@gmail.com") },
            singleLine = true,
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(vertical = 6.dp),
            textStyle = TextStyle(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF2C2C2C),
                unfocusedContainerColor = Color(0xFF2C2C2C),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF12B956),
                focusedPlaceholderColor = Color(0xFFAAAAAA),
                unfocusedPlaceholderColor = Color(0xFFAAAAAA),
                errorBorderColor = Color.Red
            ),
            isError = email.isNotEmpty() && !isEmailValid
        )
        if (email.isNotEmpty() && !isEmailValid) {
            Text(
                text = "Неверный формат email или содержит кириллицу",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }


        // Поле Пароль
        Text(
            text = "Пароль",
            color = Color.White,
            fontSize = 16.sp,
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
            textStyle = TextStyle(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF2C2C2C),
                unfocusedContainerColor = Color(0xFF2C2C2C),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF12B956),
                focusedPlaceholderColor = Color(0xFFAAAAAA),
                unfocusedPlaceholderColor = Color(0xFFAAAAAA)
            ),
            visualTransformation = PasswordVisualTransformation()
        )
    }
}

object LoginScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        val emailIsValid = remember(email) { isEmailValid(email) }
        val isLoginButtonEnabled = emailIsValid && password.isNotEmpty()

        val vkUrl = "https://vk.com"
        val okUrl = "https://ok.ru"

        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow

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
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 24.dp)
            )

            Spacer(Modifier.height(16.dp))

            LoginFields(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                isEmailValid = emailIsValid
            )

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = { if (isLoginButtonEnabled) navigator.push(HomeScreen) },
                enabled = isLoginButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .padding(top = 8.dp, bottom = 12.dp)
                    .align(Alignment.Start),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF12B956),
                    disabledContainerColor = Color.Gray
                )
            ) {
                Text(
                    "Вход",
                    color = Color.White,
                    fontSize = 18.sp,
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
                    color = Color(0xFF12B956),
                    // modifier = Modifier.clickable { onRegisterClick() }
                )
            }

            Text(
                text = "Забыл пароль",
                color = Color(0xFF12B956),
                modifier = Modifier
                    .padding(top = 8.dp)
                    // .clickable { onForgotPasswordClick() }
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
                        // onVkClick(vkUrl)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2787F5)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
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
                        // onOkClick(okUrl)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7700)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .padding(horizontal = 4.dp)
                ) {
                    Text("ОК", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
fun DefaultPreview() {
    LoginScreen.Content()
}
