package com.grigorevmp.habits.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE habit_table ADD COLUMN habit_category TEXT NOT NULL DEFAULT 0")
    }
}