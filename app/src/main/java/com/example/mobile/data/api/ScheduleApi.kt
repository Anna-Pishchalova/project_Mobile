package com.example.mobile.data.api

import com.example.mobile.data.dto.GroupDto
import com.example.mobile.data.dto.ScheduleByDateDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleApi {
    @GET("api/schedule/groups")
    suspend fun getAllGroups(): List<GroupDto>

    @GET("api/schedule/groups/search")
    suspend fun searchGroups(@Query("query") query: String): List<GroupDto>

    @GET("api/schedule/group/{groupName}")
    suspend fun getSchedule(
        @Path("groupName") groupName: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): List<ScheduleByDateDto>
}