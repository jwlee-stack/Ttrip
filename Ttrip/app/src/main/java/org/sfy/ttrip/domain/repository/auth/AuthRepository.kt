package org.sfy.ttrip.domain.repository.auth


interface AuthRepository {

    suspend fun requestSignUp(phoneNumber: String, password: String)
}