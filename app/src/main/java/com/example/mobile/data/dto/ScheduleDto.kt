package com.example.mobile.data.dto

data class ScheduleResponse(
    val lessonDate: String,
    val weekday: String,
    val lessons: List<LessonDto>
)
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