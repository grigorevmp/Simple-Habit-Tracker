package com.grigorevmp.habits.data.repository

import androidx.room.Query
import com.grigorevmp.habits.data.data.DateDao
import com.grigorevmp.habits.data.data.DateEntity
import java.time.LocalDate
import javax.inject.Inject

class DateRepository @Inject constructor(private val dateDao: DateDao) {

    suspend fun insert(date: DateEntity) {
        dateDao.insert(date)
    }

    suspend fun getDateId(date: LocalDate) = dateDao.getDateId(date)


    suspend fun delete(date: DateEntity) {
        dateDao.delete(date)
    }
}