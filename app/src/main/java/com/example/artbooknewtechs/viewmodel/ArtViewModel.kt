package com.example.artbooknewtechs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artbooknewtechs.model.ImageResponse
import com.example.artbooknewtechs.repo.ArtRepository
import com.example.artbooknewtechs.roomdb.Art
import com.example.artbooknewtechs.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArtViewModel @Inject constructor(
    private val repository: ArtRepository
) : ViewModel() {

    val artList = repository.getArt()

    private val imaeges = MutableLiveData<Resource<ImageResponse>>()
    val images: LiveData<Resource<ImageResponse>>
        get() = imaeges

    private val selectedImage = MutableLiveData<String>()
    val selectedImageUrl: LiveData<String>
        get() = selectedImage

    private var insertArtMsg = MutableLiveData<Resource<Art>>()
    val insertArtMessage: LiveData<Resource<Art>>
        get() = insertArtMsg

    fun resetInsertArtMsg(){
        insertArtMsg = MutableLiveData<Resource<Art>>()
    }

    fun setSelectedImage(url: String) {
        selectedImage.postValue(url)
    }

    fun deleteArt(art: Art) = viewModelScope.launch {
        repository.deleteArt(art)
    }

    fun insertArt(art: Art) = viewModelScope.launch {
        repository.insertArt(art)
    }

    fun makeArt(name: String, artistName: String, year: String) {
        if (name.isEmpty() || artistName.isEmpty() || year.isEmpty()) {
            insertArtMsg.postValue(Resource.error("Enter name, artist, year", null))
            return
        }
        val yearInt = try {
            year.toInt()
        } catch (e: Exception) {
            insertArtMsg.postValue(Resource.error("Year should be number", null))
            return
        }
        val art = Art(name, artistName, yearInt, selectedImage.value ?: "")
        insertArt(art)
        setSelectedImage("")
        insertArtMsg.postValue(Resource.success(art))
    }

    fun searchForImage(imageString: String) {
        if(imageString.isEmpty()){
            return
        }
        imaeges.value = Resource.loading(null)
        viewModelScope.launch {
            val response = repository.searchImage(imageString)
            imaeges.value = response
        }
    }
}