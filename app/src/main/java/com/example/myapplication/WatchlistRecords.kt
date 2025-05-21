package com.example.myapplication

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

object WatchlistStore {
    private val KEY_WATCHLIST = stringSetPreferencesKey("watchlist_ids")

    val watchlistIdsFlow: Flow<Set<String>> =
        MyApp.instance.dataStore.data
            .map { prefs -> prefs[KEY_WATCHLIST] ?: emptySet() }

    /**
     * Add one ID to the watchlist
     */
    suspend fun add(id: String) {
        MyApp.instance.dataStore.edit { prefs ->
            val current = prefs[KEY_WATCHLIST]?.toMutableSet() ?: mutableSetOf()
            current.add(id)
            prefs[KEY_WATCHLIST] = current
        }
    }

    /**
     * Remove one ID from the watchlist
     */
    suspend fun remove(id: String) {
        MyApp.instance.dataStore.edit { prefs ->
            val current = prefs[KEY_WATCHLIST]?.toMutableSet() ?: mutableSetOf()
            current.remove(id)
            prefs[KEY_WATCHLIST] = current
        }
    }
}
