package com.example.petpal.api

import com.example.petpal.dtos.PetCreateDto
import com.example.petpal.entities.PetEntity
import retrofit2.Call
import retrofit2.http.*

interface PetApi {
    @GET("/api/pets/user/{userId}")
    fun getPetsByUserId(@Path("userId") userId: Long): Call<List<PetEntity>>

    @POST("/api/pets/user/{userId}")
    fun createPet(@Path("userId") userId: Long, @Body petCreateDto: PetCreateDto): Call<PetEntity>

    @PUT("/api/pets/{petId}")
    fun updatePet(@Path("petId") petId: Long, @Body petCreateDto: PetCreateDto): Call<PetEntity>

    @DELETE("/api/pets/{petId}")
    fun deletePet(@Path("petId") petId: Long): Call<Void>
}
