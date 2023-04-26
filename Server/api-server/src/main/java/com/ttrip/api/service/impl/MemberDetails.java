package com.ttrip.api.service.impl;

import com.ttrip.core.entity.member.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class MemberDetails extends User {

    private final Member member;

    public MemberDetails(Member member, GrantedAuthority grantedAuthority) {
        super(String.valueOf(member.getPhoneNumber()), member.getPassword(), Collections.singleton(grantedAuthority));
        this.member = member;
    }
}
