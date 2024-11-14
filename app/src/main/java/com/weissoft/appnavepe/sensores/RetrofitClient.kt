import com.weissoft.appnavepe.sensores.ApiResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.12:8000/") // Cambia esta URL según tu red e ip
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

interface ApiService {
    @GET("api/sensor_data/") // Ruta específica del endpoint
    suspend fun getAllSensorData(): List<ApiResponse>
}
