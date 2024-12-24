package com.example.data.retrofilApi

import com.example.data.dto.EmailSharingCapsuleDto
import retrofit2.http.Body
import retrofit2.http.POST

interface EmailSharingCapsuleApi {
  @POST("/shareCapsuleWithEmails")
  suspend fun shareCapsuleWithEmails(@Body emailSharingCapsuleDto: EmailSharingCapsuleDto)
}