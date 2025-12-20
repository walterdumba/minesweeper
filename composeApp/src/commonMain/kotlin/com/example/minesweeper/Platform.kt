package com.example.minesweeper

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform