package com.example.mobile.data.repository

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull

// Extension property для Context
val Context.dataStore by preferencesDataStore(name = "favorites")

class FavoritesRepository(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        private val FAVORITES_KEY = stringSetPreferencesKey("favorite_groups")
    }

    val favoritesFlow: Flow<Set<String>> = dataStore.data
        .map { preferences ->
            preferences[FAVORITES_KEY] ?: emptySet()
        }

    suspend fun addFavorite(groupName: String) {
        dataStore.edit { preferences ->
            val current = preferences[FAVORITES_KEY] ?: emptySet()
            preferences[FAVORITES_KEY] = current.toMutableSet().apply { add(groupName) }
        }
    }

    suspend fun removeFavorite(groupName: String) {
        dataStore.edit { preferences ->
            val current = preferences[FAVORITES_KEY] ?: emptySet()
            preferences[FAVORITES_KEY] = current.toMutableSet().apply { remove(groupName) }
        }
    }

    suspend fun isFavorite(groupName: String): Boolean {
        return dataStore.data.map { preferences ->
            preferences[FAVORITES_KEY]?.contains(groupName) ?: false
        }.firstOrNull() ?: false
    }
}