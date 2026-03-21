package com.example.mobile.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableDropdown(
    items: List<String>,
    selectedItem: String?,
    onItemSelected: (String) -> Unit,
    placeholder: String = "Выберите группу",
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val filteredItems = remember(searchText, items) {
        if (searchText.isEmpty()) items
        else items.filter { it.contains(searchText, ignoreCase = true) }
    }

    Column(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedItem ?: searchText,
                onValueChange = {
                    searchText = it
                    expanded = true
                },
                readOnly = false,
                placeholder = { Text(placeholder) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (filteredItems.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Ничего не найдено") },
                        onClick = { expanded = false }
                    )
                } else {
                    filteredItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                onItemSelected(item)
                                searchText = item
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}