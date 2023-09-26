package com.example.artbooknewtechs.repo

import androidx.lifecycle.LiveData
import com.example.artbooknewtechs.model.ImageResponse
import com.example.artbooknewtechs.roomdb.Art
import com.example.artbooknewtechs.util.Resource

interface ArtRepositoryInterface {

    suspend fun insertArt(art: Art)

    suspend fun deleteArt(art: Art)

    fun getArt(): LiveData<List<Art>>

    suspend fun searchImage(imageString: String): Resource<ImageResponse>
}