package com.ttrip.core.repository.member;

import com.ttrip.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findById(Integer id);
    Optional<Member> findByUuid(UUID uuid);
    Optional<Member> findByNickname(String nickname);
}
