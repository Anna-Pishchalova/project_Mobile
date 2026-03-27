package com.example.mobile.data.repository

import android.util.Log
import com.example.mobile.data.api.ScheduleApi
import com.example.mobile.data.dto.LessonDto
import com.example.mobile.data.dto.ScheduleByDateDto
import com.example.mobile.data.dto.ScheduleDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScheduleRepository(
    private val api: ScheduleApi
) {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    suspend fun getGroups(): List<String> {
        return try {
            val response = api.getAllGroups()
            Log.d("ScheduleRepository", "Получено групп: ${response.size}")
            response.map { it.groupName }.sorted()
        } catch (e: Exception) {
            Log.e("ScheduleRepository", "Ошибка загрузки групп", e)
            throw Exception("Не удалось загрузить группы: ${e.message}")
        }
    }

    suspend fun searchGroups(query: String): List<String> {
        return try {
            val response = api.searchGroups(query)
            Log.d("ScheduleRepository", "Поиск групп по '$query': найдено ${response.size}")
            response.map { it.groupName }.sorted()
        } catch (e: Exception) {
            Log.e("ScheduleRepository", "Ошибка поиска групп", e)
            throw Exception("Не удалось найти группы: ${e.message}")
        }
    }

    suspend fun getSchedule(
        groupName: String,
        startDate: LocalDate = LocalDate.now(),
        endDate: LocalDate = startDate
    ): List<ScheduleDto> {
        return try {
            val startString = startDate.format(dateFormatter)
            val endString = endDate.format(dateFormatter)

            Log.d("ScheduleRepository", "=== Запрос расписания ===")
            Log.d("ScheduleRepository", "Группа: $groupName")
            Log.d("ScheduleRepository", "Период: $startString - $endString")

            val days = api.getSchedule(groupName, startString, endString)
            Log.d("ScheduleRepository", "Получено дней от сервера: ${days.size}")

            val result = convertToScheduleDtoList(days, groupName)
            Log.d("ScheduleRepository", "Преобразовано записей: ${result.size}")
            result
        } catch (e: Exception) {
            Log.e("ScheduleRepository", "Ошибка загрузки расписания", e)
            throw Exception("Не удалось загрузить расписание: ${e.message}")
        }
    }

    private fun convertToScheduleDtoList(
        days: List<ScheduleByDateDto>,
        groupName: String
    ): List<ScheduleDto> {
        val result = mutableListOf<ScheduleDto>()
        var idCounter = 0

        for (day in days) {
            Log.d("ScheduleRepository", "Обработка дня: ${day.lessonDate}, уроков: ${day.lessons.size}")

            for (lesson in day.lessons) {
                val groupParts = lesson.groupParts

                if (groupParts.isNullOrEmpty()) {
                    result.add(
                        ScheduleDto(
                            id = idCounter++,
                            groupName = groupName,
                            subject = lesson.subject ?: "Без названия",
                            lessonType = getLessonType(lesson),
                            teacher = lesson.teacher ?: "Не указан",
                            classroom = lesson.classroom ?: "Не указана",
                            building = lesson.building ?: "Не указан",
                            lessonDate = day.lessonDate,
                            lessonNumber = lesson.lessonNumber,
                            groupPart = null
                        )
                    )
                    Log.d("ScheduleRepository", "  Добавлен урок ${lesson.lessonNumber}: ${lesson.subject}")
                } else {
                    for ((partKey, partData) in groupParts) {
                        if (partData != null) {
                            result.add(
                                ScheduleDto(
                                    id = idCounter++,
                                    groupName = groupName,
                                    subject = partData.subject ?: lesson.subject ?: "Без названия",
                                    lessonType = getLessonType(lesson),
                                    teacher = partData.teacher ?: lesson.teacher ?: "Не указан",
                                    classroom = partData.classroom ?: lesson.classroom ?: "Не указана",
                                    building = partData.building ?: lesson.building ?: "Не указан",
                                    lessonDate = day.lessonDate,
                                    lessonNumber = lesson.lessonNumber,
                                    groupPart = partKey.name
                                )
                            )
                            Log.d("ScheduleRepository", "  Добавлена подгруппа $partKey: ${partData.subject}")
                        } else {
                            result.add(
                                ScheduleDto(
                                    id = idCounter++,
                                    groupName = groupName,
                                    subject = lesson.subject ?: "Без названия",
                                    lessonType = getLessonType(lesson),
                                    teacher = lesson.teacher ?: "Не указан",
                                    classroom = lesson.classroom ?: "Не указана",
                                    building = lesson.building ?: "Не указан",
                                    lessonDate = day.lessonDate,
                                    lessonNumber = lesson.lessonNumber,
                                    groupPart = partKey.name
                                )
                            )
                        }
                    }
                }
            }
        }

        return result
    }

    private fun getLessonType(lesson: LessonDto): String {
        return when {
            lesson.time?.contains("лекц", ignoreCase = true) == true -> "Лекция"
            lesson.time?.contains("практ", ignoreCase = true) == true -> "Практика"
            lesson.time?.contains("лаб", ignoreCase = true) == true -> "Лабораторная"
            else -> "Занятие"
        }
    }
}