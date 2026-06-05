package com.example.project_enlishlearning.data.repository

import com.example.project_enlishlearning.data.local.dao.UserProfileDao
import com.example.project_enlishlearning.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

class UserProfileRepository(
    private val userProfileDao: UserProfileDao
) {

    fun getProfile(userId: String): Flow<UserProfileEntity?> {
        return userProfileDao.getProfileByUserId(userId)
    }

    suspend fun saveProfile(profile: UserProfileEntity) {
        userProfileDao.upsertProfile(profile)
    }

    suspend fun deleteProfile(userId: String) {
        userProfileDao.deleteProfile(userId)
    }
}