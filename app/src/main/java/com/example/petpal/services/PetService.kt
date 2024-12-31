package com.example.petpal.services

import com.example.petpal.api.PetApi
import com.example.petpal.api.RetrofitClient
import com.example.petpal.dtos.PetCreateDto
import com.example.petpal.entities.PetEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetService {
    private val petApi = RetrofitClient.instance.create(PetApi::class.java)

    fun getPetsByUserId(userId: Long, callback: (List<PetEntity>?, String?) -> Unit) {
        petApi.getPetsByUserId(userId).enqueue(object : Callback<List<PetEntity>> {
            override fun onResponse(call: Call<List<PetEntity>>, response: Response<List<PetEntity>>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Failed to fetch pets")
                }
            }

            override fun onFailure(call: Call<List<PetEntity>>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }

    fun createPet(userId: Long, petCreateDto: PetCreateDto, callback: (PetEntity?, String?) -> Unit) {
        petApi.createPet(userId, petCreateDto).enqueue(object : Callback<PetEntity> {
            override fun onResponse(call: Call<PetEntity>, response: Response<PetEntity>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Failed to create pet")
                }
            }

            override fun onFailure(call: Call<PetEntity>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }

    fun updatePet(petId: Long, petCreateDto: PetCreateDto, callback: (PetEntity?, String?) -> Unit) {
        petApi.updatePet(petId, petCreateDto).enqueue(object : Callback<PetEntity> {
            override fun onResponse(call: Call<PetEntity>, response: Response<PetEntity>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Failed to update pet")
                }
            }

            override fun onFailure(call: Call<PetEntity>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }

    fun deletePet(petId: Long, callback: (String?) -> Unit) {
        petApi.deletePet(petId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(null)
                } else {
                    callback("Failed to delete pet")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(t.message)
            }
        })
    }
}
