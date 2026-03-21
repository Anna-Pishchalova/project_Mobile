package com.example.mobile.data.repository

import com.example.mobile.data.api.ScheduleApi
import com.example.mobile.data.dto.ScheduleDto

class ScheduleRepository(
    private val api: ScheduleApi
) {
    suspend fun getGroups(): List<String> {
        val response = api.getGroups()
        return response.map { it.groupName }.sorted()
    }

    suspend fun getSchedule(groupName: String): List<ScheduleDto> {
        return api.getSchedule(groupName)
    }
}