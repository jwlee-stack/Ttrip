package com.ttrip.core.entity.member;

import com.ttrip.core.entity.BaseEntity;
import com.ttrip.core.customEnum.Authority;
import com.ttrip.core.customEnum.Gender;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
//@ToString(callSuper = true)
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true,columnDefinition = "char(36)")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID uuid;
    private String phoneNumber;
    private String password;
    @Column(length = 6, unique = true)
    private String nickname;
    @Column(length = 20)
    private String intro;
    private String imagePath;
    private String fcmToken;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birthday;
    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private Boolean shareLocation;
    @Enumerated(EnumType.STRING) //문자열 자체 저장
    private Authority authority;

    @Builder
    public Member(UUID uuid,String phoneNumber, String password, String nickname, String intro, String imagePath, String fcmToken, Gender gender, LocalDate birthday, Boolean shareLocation,Authority authority) {
        this.uuid = uuid;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.nickname = nickname;
        this.intro = intro;
        this.imagePath = imagePath;
        this.fcmToken = fcmToken;
        this.gender = gender;
        this.birthday = birthday;
        this.shareLocation = shareLocation;
        this.authority=authority;
    }


}
