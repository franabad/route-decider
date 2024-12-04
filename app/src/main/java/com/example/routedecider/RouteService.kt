package com.example.routedecider

import retrofit2.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.JsonParseException
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import timber.log.Timber


interface ApiService {
    @POST()
    suspend fun getDirections(
        @Url url: String,
        @Body routeRequest: RouteRequest,
        @Header("X-Goog-FieldMask") fieldMask: String,
        @Header("X-Goog-Api-Key") apiKey: String,
    ): Response<DirectionsResponse>
}

data class Location(
    val latLng: LatLng
)

data class Waypoint(
    val location: Location
)

data class RouteRequest (
    val origin: Waypoint,
    val destination: Waypoint,
    val travelMode: String,
    val routingPreference: String,
)

data class DirectionsResponse(
    val routes: List<Any>? = null,
    val error: ErrorResponse? = null,
)

data class ErrorResponse(
    val code: Int,
    val message: String,
    val status: String,
)

class RouteService {

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://routes.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    suspend fun getRoute(origin: Waypoint, destination: Waypoint, travelMode: String, routingPreference: String, apiKey: String, fieldMask: String): DirectionsResponse? {
        val routeRequest = RouteRequest(origin, destination, travelMode, routingPreference)
        val response = apiService.getDirections(
            "directions/v2:computeRoutes",
            routeRequest,
            fieldMask,
            apiKey
        )

        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorBody = response.errorBody()?.string()
            Timber.tag("Error").e("Error body: $errorBody")
            Timber.tag("Error").e("Response code: ${response.code()}")
            Timber.tag("Error").e("Response headers: ${response.headers()}")

            if (errorBody != null) {
                try {
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    Timber.tag("Error").e("Error al obtener las rutas: ${errorResponse.message}")
                } catch (e: JsonParseException) {
                    Timber.e("Error al parsear la respuesta de error: ${e.message}")
                }
            } else {
                Timber.e("Error al obtener las rutas: Respuesta de error inesperada")
            }
            return null
        }
    }
}
