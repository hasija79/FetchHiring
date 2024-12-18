package com.sample.fetch.hiring.model

import kotlinx.serialization.Serializable

@Serializable
data class Hiring(
    var id: Int,
    var listId: Int,
    var name: String? = null,
)
