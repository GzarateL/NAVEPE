package com.weissoft.appnavepe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.weissoft.appnavepe.network.Article
import com.weissoft.appnavepe.network.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val articles = remember { mutableStateListOf<Article>() }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.apiService.getNews(apiKey = "sL0pVymZLD-DaX-CtA-u4wdEzlALN7MDNEVNXMD4wEU3gDqW")
                articles.addAll(response.news)
            } catch (e: Exception) {
                // Manejar errores si es necesario
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barra de título con flecha y texto
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cargar el ícono de la carpeta drawable
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back), // Cambia 'ic_arrow_back' al nombre de tu archivo de ícono
                            contentDescription = "Volver atrás"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "NOTICIAS", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            },
            actions = {},
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White)
        )

        // Contenido de las noticias
        if (isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                articles.forEach { article ->
                    NewsItem(article)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun NewsItem(article: Article) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (!article.image.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(article.image),
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = article.title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = article.description ?: "Sin descripción",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}
