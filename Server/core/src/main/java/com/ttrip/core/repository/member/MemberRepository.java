package com.ttrip.core.repository.member;

import com.ttrip.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByMemberId(Integer memberId);
    Optional<Member> findByMemberUuid(UUID memberUuid);
    Optional<Member> findByNickname(String nickname);
    Boolean existsByNickname(String nickname);
    Optional<Member> findByPhoneNumber(String phoneNumber);
    List<Member> findMembersByMemberUuidInOrderByMemberUuid(List<UUID> memeberUuidList);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByMemberUuid(UUID memberUuid);
}
