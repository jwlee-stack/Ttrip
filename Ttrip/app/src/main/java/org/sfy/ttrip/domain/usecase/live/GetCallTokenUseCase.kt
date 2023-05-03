package org.sfy.ttrip.domain.usecase.live

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.live.CallToken
import org.sfy.ttrip.domain.repository.live.LiveRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCallTokenUseCase @Inject constructor(
    private val liveRepository: LiveRepository
) {
    suspend operator fun invoke(sessionId: String, memberUuid: String): Resource<CallToken> =
        withContext(Dispatchers.IO) {
            liveRepository.getCallToken(sessionId, memberUuid)
        }
}