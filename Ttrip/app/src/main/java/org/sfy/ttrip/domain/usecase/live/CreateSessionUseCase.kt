package org.sfy.ttrip.domain.usecase.live

import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.live.SessionItem
import org.sfy.ttrip.domain.repository.live.LiveRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateSessionUseCase @Inject constructor(
    private val liveRepository: LiveRepository
) {
    suspend operator fun invoke(): Resource<SessionItem> =
        liveRepository.createSession()
}