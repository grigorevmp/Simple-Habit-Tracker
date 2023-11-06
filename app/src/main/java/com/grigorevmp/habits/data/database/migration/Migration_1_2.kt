package com.grigorevmp.habits.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE habit_ref ADD COLUMN value INTEGER DEFAULT NULL")
        db.execSQL("ALTER TABLE habit_table ADD COLUMN habit_countable INTEGER DEFAULT 0 NOT NULL")
        db.execSQL("ALTER TABLE habit_table ADD COLUMN habit_countable_entity TEXT")

        db.execSQL("UPDATE habit_table SET habit_countable = 0")
    }
}