package com.ttrip.core.repository.member;

import com.ttrip.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByMemberId(Integer memberId);
    Optional<Member> findByMemberUuid(UUID memberUuid);
    Optional<Member> findByNickname(String nickname);
    Boolean existsByNickname(String nickname);
    Optional<Member> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);
}
