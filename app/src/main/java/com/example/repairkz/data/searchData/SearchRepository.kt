package com.example.repairkz.data.searchData

import com.example.repairkz.common.models.Master

interface SearchRepository {
    suspend fun getMasters() : List<Master>
}