package com.example.petpal.api

import com.example.petpal.models.LoginUserModel
import com.example.petpal.models.PetModel
import com.example.petpal.models.RegisterUserModel
import com.example.petpal.models.PetUpdateModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("/api/pets/{id}")
    suspend fun getPetById(
        @Path("id") petId: Long
    ): Response<ApiResponse<PetModel>>


    @GET("/api/pets/currentuserid")
    suspend fun getCurrentUserId(): Response<ApiResponse<Long>>

    @GET("/api/pets/user/{userId}")
    suspend fun getPetsByUserId(
        @Path("userId") userId: Long
    ): Response<ApiResponse<List<PetModel>>>

    @POST("/api/auth/register")
    suspend fun registerUser(
        @Body registerUserModel: RegisterUserModel
    ): Response<ApiResponse<String>>

    @POST("/api/auth/login")
    suspend fun loginUser(
        @Body loginUserDto: LoginUserModel
    ): Response<ApiResponse<Map<String, Any>>>

    @DELETE("/api/pets/{id}")
    suspend fun deletePet(
        @Path("id") id: Long
    ): Response<ApiResponse<String>>

    @PUT("/api/pets/{id}")
    suspend fun updatePet(
        @Path("id") petId: Long,
        @Body petUpdateModel: PetUpdateModel
    ): Response<ApiResponse<PetModel>>
}
