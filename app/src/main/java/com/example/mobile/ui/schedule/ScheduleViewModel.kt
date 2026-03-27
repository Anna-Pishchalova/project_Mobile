package com.example.mobile.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.data.dto.ScheduleDto
import com.example.mobile.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import java.time.LocalDate

sealed class GroupsState {
    object Loading : GroupsState()
    data class Success(val groups: List<String>) : GroupsState()
    data class Error(val message: String) : GroupsState()
}

sealed class ScheduleState {
    object Loading : ScheduleState()
    data class Success(val schedule: List<ScheduleDto>) : ScheduleState()
    data class Error(val message: String) : ScheduleState()
}

class ScheduleViewModel(
    private val repository: ScheduleRepository
) : ViewModel() {

    private val _groupsState = MutableStateFlow<GroupsState>(GroupsState.Loading)
    val groupsState: StateFlow<GroupsState> = _groupsState.asStateFlow()

    private val _scheduleState = MutableStateFlow<ScheduleState>(ScheduleState.Loading)
    val scheduleState: StateFlow<ScheduleState> = _scheduleState.asStateFlow()

    init {
        loadGroups()
    }

    fun loadGroups() {
        viewModelScope.launch {
            _groupsState.value = GroupsState.Loading
            try {
                val groups = repository.getGroups()
                Log.d("ScheduleViewModel", "✅ Группы загружены: ${groups.size}")
                _groupsState.value = GroupsState.Success(groups)
            } catch (e: Exception) {
                Log.e("ScheduleViewModel", "❌ Ошибка загрузки групп", e)
                _groupsState.value = GroupsState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun loadSchedule(groupName: String, date: LocalDate = LocalDate.now()) {
        viewModelScope.launch {
            _scheduleState.value = ScheduleState.Loading
            try {
                Log.d("ScheduleViewModel", "📅 Загрузка расписания для $groupName на $date")
                val schedule = repository.getSchedule(groupName, date, date)
                Log.d("ScheduleViewModel", "✅ Расписание загружено: ${schedule.size} занятий")
                _scheduleState.value = ScheduleState.Success(schedule)
            } catch (e: Exception) {
                Log.e("ScheduleViewModel", "❌ Ошибка загрузки расписания", e)
                _scheduleState.value = ScheduleState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}