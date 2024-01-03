package com.grigorevmp.habits.data.repository

import com.grigorevmp.habits.data.date.DateDao
import com.grigorevmp.habits.data.date.DateEntity
import java.time.LocalDate
import javax.inject.Inject

class DateRepository @Inject constructor(private val dateDao: DateDao) {
    fun getAllDates() = dateDao.getAllDates()

    fun insert(date: DateEntity) {
        dateDao.insert(date)
    }

    fun getDateId(date: LocalDate): DateEntity? = dateDao.getDateId(date)

    fun delete(date: DateEntity) {
        dateDao.delete(date)
    }

    fun getDate(dateId: Long) = dateDao.getDate(dateId)
}