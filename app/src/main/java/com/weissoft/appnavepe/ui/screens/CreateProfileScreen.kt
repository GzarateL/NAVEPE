package com.weissoft.appnavepe.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavController
import com.weissoft.appnavepe.R
import com.weissoft.appnavepe.room.CarProfile
import com.weissoft.appnavepe.room.CarProfileRepository
import com.weissoft.appnavepe.utils.saveImageToInternalStorage
import kotlinx.coroutines.launch

@Composable
fun CreateProfileScreen(navController: NavController, repository: CarProfileRepository) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var driverName by remember { mutableStateOf("") }
    var carNickname by remember { mutableStateOf("") }
    var carModel by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para seleccionar una imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Guardar la imagen en almacenamiento interno y obtener la URI interna
        imageUri = uri?.let { saveImageToInternalStorage(context, it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Flecha de retroceso y título en la parte superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Volver",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PERFIL DEL AUTO",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fondo oscuro con bordes redondeados para los campos
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileTextField(value = driverName, onValueChange = { driverName = it }, label = "NOMBRE DEL CONDUCTOR")
                ProfileTextField(value = carNickname, onValueChange = { carNickname = it }, label = "APODO DEL CARRO")
                ProfileTextField(value = carModel, onValueChange = { carModel = it }, label = "MODELO DE CARRO")
                ProfileTextField(value = plate, onValueChange = { plate = it }, label = "PLACA (Ejemplo: ABC-1A2)")
                ProfileTextField(value = color, onValueChange = { color = it }, label = "COLOR")

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar la vista previa de la imagen seleccionada, si existe
                imageUri?.let {
                    val painter = rememberAsyncImagePainter(model = it)
                    Image(
                        painter = painter,
                        contentDescription = "Imagen Seleccionada",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para subir o cambiar imagen
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.White, RoundedCornerShape(16.dp))
                        .padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = if (imageUri == null) "SUBIR IMAGEN" else "CAMBIAR IMAGEN",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para crear el perfil
            Button(
                onClick = {
                    val carProfile = CarProfile(
                        driverName = driverName,
                        carNickname = carNickname,
                        carModel = carModel,
                        plate = plate,
                        color = color,
                        imageUri = imageUri.toString() // Guardar la URI interna en la base de datos
                    )
                    scope.launch {
                        repository.insertProfile(carProfile)
                        navController.popBackStack()
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "CREAR PERFIL",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            shape = RoundedCornerShape(16.dp), // Bordes redondeados para el campo de texto
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White, // Fondo blanco
                cursorColor = Color.Black, // Cursor en negro
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
    }
}
