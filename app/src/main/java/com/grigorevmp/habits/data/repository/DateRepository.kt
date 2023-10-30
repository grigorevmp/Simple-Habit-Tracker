package com.grigorevmp.habits.data.repository

import com.grigorevmp.habits.data.data.DateDao
import com.grigorevmp.habits.data.data.DateEntity
import java.time.LocalDate
import javax.inject.Inject

class DateRepository @Inject constructor(private val dateDao: DateDao) {

    fun insert(date: DateEntity) {
        dateDao.insert(date)
    }

    fun getDateId(date: LocalDate): DateEntity? = dateDao.getDateId(date)


    fun delete(date: DateEntity) {
        dateDao.delete(date)
    }
}