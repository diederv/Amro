package com.amro.feature.movies.utils

private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p"

fun posterUrl(path: String?): String? = path?.let { "$BASE_IMAGE_URL/w500$it" }
fun backdropUrl(path: String?): String? = path?.let { "$BASE_IMAGE_URL/w780$it" }
