package com.buildertrend.core.services.dailylogs

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HiringRepository @Inject constructor(
    private val remoteDataSource: HiringRemoteDataSource,
) {
    suspend fun getHiringList() = withContext(Dispatchers.IO) {
        remoteDataSource.getHiringList().map {
            it.filterNot { it.name.isNullOrBlank() } // Filter out items where "name" is blank or null.
                // Sort the results first by "listId" then by "name" when displaying.
                .run { sortedBy { it.listId }.sortedBy { it.name?.split(" ")?.last()?.toInt() } }
                .sortedBy { it.listId } // Display all the items grouped by "listId"
                .also { if (DEBUG) Log.d(TAG, "Response:  ${it.joinToString("\n")}") }
        }
    }

    companion object {
        private const val DEBUG = false
        private const val TAG = "HiringRepository"
    }
}
