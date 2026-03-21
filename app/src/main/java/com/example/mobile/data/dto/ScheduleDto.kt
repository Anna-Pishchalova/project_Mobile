package com.example.mobile.data.dto

data class ScheduleDto(
    val id: Int,
    val groupName: String,
    val subject: String,
    val lessonType: String,
    val teacher: String,
    val classroom: String,
    val building: String,
    val lessonDate: String,
    val lessonNumber: Int,
    val groupPart: String?
)

data class GroupDto(
    val id: Int,
    val groupName: String
)