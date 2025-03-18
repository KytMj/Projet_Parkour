package com.example.projet_parkour.bdd

import android.content.Context
import androidx.room.*

fun provideDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "app_database"
    ).build()
}
