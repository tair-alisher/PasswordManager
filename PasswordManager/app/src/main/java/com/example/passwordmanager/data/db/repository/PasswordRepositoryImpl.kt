package com.example.passwordmanager.data.db.repository

import com.example.passwordmanager.data.db.dao.PasswordDao
import com.example.passwordmanager.data.db.entity.PasswordEntity
import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.repository.PasswordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PasswordRepositoryImpl @Inject constructor(
    private val passwordDao: PasswordDao
) : PasswordRepository {

    override suspend fun getById(id: Int): Password {
        return passwordDao.getById(id).toModel()
    }

    override fun getAll(): Flow<List<Password>> {
        return passwordDao.getAll().map { items -> items.map { item -> item.toModel() }}
    }

    override suspend fun update(password: Password) {
        return passwordDao.update(PasswordEntity(password.id, password.title,
            password.link, password.login, password.password))
    }

    override suspend fun addPassword(password: Password) {
        return passwordDao.addPassword(PasswordEntity(password.id, password.title,
            password.link, password.login, password.password))
    }

    override suspend fun deletePassword(id: Int) {
        return passwordDao.deletePassword(id)
    }

    override fun searchPassword(searchVal: String): Flow<List<Password>> {
        return passwordDao.searchPassword(searchVal).map { items -> items.map { item -> item.toModel() }}
    }
}