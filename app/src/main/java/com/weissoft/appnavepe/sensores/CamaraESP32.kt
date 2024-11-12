package com.weissoft.appnavepe.sensores

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ESP32CameraView(modifier: Modifier = Modifier) {
    // Usamos remember para que el `WebView` no se recree innecesariamente
    var webView: WebView? by remember { mutableStateOf(null) }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient() // Mantiene la navegación dentro de la aplicación
                webChromeClient = WebChromeClient() // Maneja el rendimiento
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE // Evita el almacenamiento en caché
                    setLayerType(WebView.LAYER_TYPE_HARDWARE, null) // Usa aceleración de hardware
                }
                loadUrl("http://192.168.0.13/stream") // URL de la cámara
                webView = this // Asigna el `WebView` a la variable
            }
        },
        modifier = modifier.fillMaxSize()
    )

    // Limpiar y liberar recursos del `WebView` al salir de la pantalla
    DisposableEffect(Unit) {
        onDispose {
            // Verifica si `webView` no es nulo antes de limpiar
            webView?.apply {
                stopLoading() // Detiene la carga actual
                clearCache(true) // Limpia la caché
                clearHistory() // Limpia el historial
                removeAllViews() // Remueve todas las vistas para liberar memoria
                destroy() // Destruye el `WebView`
            }
            webView = null // Limpia la referencia para asegurarse de que se libere
        }
    }
}
