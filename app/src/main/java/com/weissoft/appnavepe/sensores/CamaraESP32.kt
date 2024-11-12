package com.weissoft.appnavepe.sensores

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ESP32CameraView(modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient() // Mantener la navegación en la aplicación
                webChromeClient = WebChromeClient() // Para manejar mejor el rendimiento
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE // Evita almacenar en caché
                    setLayerType(WebView.LAYER_TYPE_HARDWARE, null) // Usa aceleración de hardware
                }
                loadUrl("http://192.168.0.13/stream") // URL de la cámara
            }
        },
        modifier = modifier.fillMaxSize()
    )
}


