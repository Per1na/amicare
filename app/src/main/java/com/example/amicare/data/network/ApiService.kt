package com.example.amicare.data.network

import com.example.amicare.data.model.ComplaintResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("complaints")
    suspend fun getComplaints(): ComplaintResponse

    // Pastikan fungsi POST ini ada untuk mengirim data pengaduan baru
    @POST("complaints")
    suspend fun storeComplaint(
        @Body complaintData: Map<String, String>
    ): Response<ResponseBody>
}