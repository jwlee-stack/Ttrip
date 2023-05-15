package com.ttrip.core.entity.member;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ttrip.core.customEnum.Authority;
import com.ttrip.core.customEnum.Gender;
import com.ttrip.core.entity.BaseEntity;
import com.ttrip.core.entity.survey.Survey;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int memberId;
    @Column(name = "uuid", nullable = false, unique = true, columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID memberUuid;
    private String phoneNumber;
    private String password;
    @Column(length = 6, unique = true)
    private String nickname;
    @Column(length = 20)
    private String intro;
    private String profileImgPath;
    private String markerImgPath;
    private String backgroundImgPath;
    private String fcmToken;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(columnDefinition = "TINYINT")
    private Integer age;
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean shareLocation;
    @Enumerated(EnumType.STRING) //문자열 자체 저장
    private Authority authority;
    private Boolean profileVerification;
    private Boolean isFreezed;
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    @JsonManagedReference
    private Survey survey;

    @Builder
    public Member(UUID uuid, String phoneNumber, String password, String nickname, String intro, String profileImgPath, String backgroundImgPath, String markerImgPath, String fcmToken, Gender gender, Integer age, Boolean shareLocation, Authority authority) {
        this.memberUuid = uuid;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.nickname = nickname;
        this.intro = intro;
        this.profileImgPath = profileImgPath;
        this.backgroundImgPath = backgroundImgPath;
        this.markerImgPath = markerImgPath;
        this.fcmToken = fcmToken;
        this.gender = gender;
        this.age = age;
        this.shareLocation = false;
        this.authority = authority;
        this.isFreezed = false;
    }
}
