package com.example.mobile.ui.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobile.data.dto.ScheduleByDateDto

@Composable
fun ScheduleList(data: List<ScheduleByDateDto>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(data) { day ->
            Text(
                "${day.lessonDate} (${day.weekday})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )
            if (day.lessons.isEmpty()) {
                Text(
                    "Информация отсутствует",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            } else {
                day.lessons.forEach { lesson ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text("Пара ${lesson.lessonNumber} (${lesson.time ?: "время не указано"})")

                            // ✅ Исправлено: безопасная проверка на null
                            if (!lesson.groupParts.isNullOrEmpty()) {
                                lesson.groupParts.forEach { (part, info) ->
                                    if (info != null) {
                                        Text("$part: ${info.subject ?: "без названия"}")
                                        Text(info.teacher ?: "преподаватель не указан")
                                        Text("${info.building ?: "корпус не указан"}, ${info.classroom ?: "аудитория не указана"}")
                                    }
                                }
                            } else {
                                // Если нет подгрупп, показываем основные данные
                                Text(lesson.subject ?: "Без названия")
                                Text(lesson.teacher ?: "Преподаватель не указан")
                                Text("${lesson.building ?: "Корпус не указан"}, ${lesson.classroom ?: "Аудитория не указана"}")
                            }
                        }
                    }
                }
            }
        }
    }
}