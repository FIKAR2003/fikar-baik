package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KmsDark
import com.example.ui.theme.KmsPink
import com.example.ui.theme.KmsPinkContainer
import com.example.viewmodel.KmsViewModel

@Composable
fun RegisterScreen(
    viewModel: KmsViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val username by viewModel.regUsername.collectAsState()
    val name by viewModel.regName.collectAsState()
    val pin by viewModel.regPin.collectAsState()
    val error by viewModel.regError.collectAsState()
    val scrollState = rememberScrollState()
    var pinVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBFF))
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(KmsPinkContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ChildCare,
                contentDescription = "Cute logo",
                tint = KmsPink,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Daftar Akun Orang Tua",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = KmsPink,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Buat akun untuk memantau tumbuh kembang si kecil secara personal",
            fontSize = 13.sp,
            color = KmsDark.copy(alpha = 0.65f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Formulir Registrasi Ibu/Ayah",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = KmsDark,
                    modifier = Modifier.padding(bottom = 18.dp)
                )

                // Nickname or Parent's Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { viewModel.regName.value = it },
                    label = { Text("Nama Ibu / Ayah") },
                    placeholder = { Text("Contoh: Ibu Aisyah") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AssignmentInd,
                            contentDescription = "Nama Orang Tua",
                            tint = KmsPink
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = KmsPink,
                        focusedLabelColor = KmsPink,
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_name_input"),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Username field
                OutlinedTextField(
                    value = username,
                    onValueChange = { viewModel.regUsername.value = it },
                    label = { Text("Username") },
                    placeholder = { Text("Gunakan nama panggung (tanpa spasi)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Username",
                            tint = KmsPink
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = KmsPink,
                        focusedLabelColor = KmsPink,
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_username_input"),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // PIN
                OutlinedTextField(
                    value = pin,
                    onValueChange = { viewModel.regPin.value = it },
                    label = { Text("PIN Orang Tua") },
                    placeholder = { Text("Gunakan PIN 4 atau 6 digit") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "PIN",
                            tint = KmsPink
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { pinVisibility = !pinVisibility }) {
                            Icon(
                                imageVector = if (pinVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle PIN visibility",
                                tint = Color.Gray
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = if (pinVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = KmsPink,
                        focusedLabelColor = KmsPink,
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_pin_input"),
                    shape = RoundedCornerShape(16.dp)
                )

                AnimatedVisibility(
                    visible = error != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    error?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.registerParent(onSuccess = onRegisterSuccess)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KmsPink),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("register_submit_button")
                ) {
                    Text(
                        text = "Daftar & Masuk",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sudah memiliki akun?",
                fontSize = 14.sp,
                color = KmsDark.copy(alpha = 0.6f)
            )
            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.testTag("goto_login_button")
            ) {
                Text(
                    text = "Masuk Sekarang",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = KmsPink
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
