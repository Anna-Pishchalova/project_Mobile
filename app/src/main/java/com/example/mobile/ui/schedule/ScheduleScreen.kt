package com.example.mobile.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobile.data.dto.ScheduleDto
import com.example.mobile.data.repository.FavoritesRepository
import com.example.mobile.ui.components.ScheduleCard
import com.example.mobile.ui.components.SearchableDropdown
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    favoritesRepository: FavoritesRepository,
    modifier: Modifier = Modifier
) {
    val groupsState by viewModel.groupsState.collectAsState()
    val scheduleState by viewModel.scheduleState.collectAsState()
    val favorites by favoritesRepository.favoritesFlow.collectAsState(initial = emptySet())

    var selectedGroup by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Расписание занятий",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (groupsState) {
            is GroupsState.Loading -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            is GroupsState.Success -> {
                val groups = (groupsState as GroupsState.Success).groups

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SearchableDropdown(
                        items = groups,
                        selectedItem = selectedGroup,
                        onItemSelected = { group ->
                            selectedGroup = group
                            viewModel.loadSchedule(group)
                        },
                        placeholder = "Выберите группу",
                        modifier = Modifier.weight(1f)
                    )

                    if (selectedGroup != null) {
                        val isFavorite = favorites.contains(selectedGroup)
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (isFavorite) {
                                        favoritesRepository.removeFavorite(selectedGroup!!)
                                    } else {
                                        favoritesRepository.addFavorite(selectedGroup!!)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное",
                                tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            is GroupsState.Error -> {
                val errorMessage = (groupsState as GroupsState.Error).message
                Text(
                    text = "Ошибка загрузки групп: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (scheduleState) {
            is ScheduleState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ScheduleState.Success -> {
                val schedule = (scheduleState as ScheduleState.Success).schedule

                if (schedule.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Нет занятий на выбранную дату",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(schedule) { item ->
                            ScheduleCard(schedule = item)
                        }
                    }
                }
            }
            is ScheduleState.Error -> {
                val errorMessage = (scheduleState as ScheduleState.Error).message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ошибка загрузки расписания: $errorMessage",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}