package com.example.mobile.data.api

import com.example.mobile.data.dto.ScheduleDto
import com.example.mobile.data.dto.GroupDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApi {
    @GET("api/groups")
    suspend fun getGroups(): List<GroupDto>

    @GET("api/schedule")
    suspend fun getSchedule(
        @Query("groupName") groupName: String
    ): List<ScheduleDto>
}