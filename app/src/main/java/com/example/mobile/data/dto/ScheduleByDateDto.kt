package com.example.mobile.data.dto

data class ScheduleByDateDto(
    val lessonDate: String,
    val weekday: String,
    val lessons: List<LessonDto>
)