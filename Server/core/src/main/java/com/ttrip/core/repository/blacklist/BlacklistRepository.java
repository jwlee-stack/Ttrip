package com.ttrip.core.repository.blacklist;

import com.ttrip.core.entity.blacklist.Blacklist;
import com.ttrip.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist,Integer> {
    //List<Blacklist> findAllByBlacklist_Id(Integer memberId);
    List<Blacklist> findAllByMember(Member member);
    Boolean existsByMember(Member member);
}
