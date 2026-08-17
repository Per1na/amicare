package com.example.amicare.data.model

data class Complaint(
    val id: Int,
    val user_id: Int,
    val title: String,
    val category: String,
    val description: String,
    val status: String,
    val admin_response: String?,
    val created_at: String,
    val updated_at: String?
)

data class ComplaintResponse(
    val success: Boolean,
    val message: String,
    val data: List<Complaint>
)

