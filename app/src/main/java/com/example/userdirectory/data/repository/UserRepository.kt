package com.example.userdirectory.data.repository

import com.example.userdirectory.data.User
import com.example.userdirectory.data.UserDao
import com.example.userdirectory.data.UserApiService
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao,
    private val apiService: UserApiService
) {
    // UI always observes this - single source of truth!
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()

    fun searchUsers(query: String): Flow<List<User>> = userDao.searchUsers(query)

    // Fetch from API and update database
    suspend fun refreshUsers(): Result<Unit> {
        return try {
            val users = apiService.getUsers()
            userDao.insertUsers(users)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}