package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mmff_movies")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val movie_id: Int = 0,

    val title: String,
    val description: String,

    // for your grid grouping + drawable lookup
    val year: Int,
    val imageName: String,

    // your genres
    val genre_one: String,
    val genre_two: String,
    val genre_three: String,

    // your awards
    val award_one: String,
    val award_two: String,
    val award_three: String,

    // watchlist flag
    val isWatchlisted: Boolean = false
)