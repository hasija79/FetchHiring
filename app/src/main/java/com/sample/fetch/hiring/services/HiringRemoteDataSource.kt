package com.buildertrend.core.services.dailylogs

import android.util.Log
import com.sample.fetch.hiring.model.Hiring
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.net.URL
import javax.inject.Inject

class HiringRemoteDataSource @Inject constructor() {

    @OptIn(ExperimentalSerializationApi::class)
    fun getHiringList(): Result<List<Hiring>> = runCatching {
        val stream = URL(HIRING_SERVICE_URL).openStream()
        Json.decodeFromStream<List<Hiring>>(stream = stream)
    }.onFailure {
        Log.e("Exception in getHiringList()", it.message, it)
    }

    companion object {
        private const val HIRING_SERVICE_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json"
    }
}


