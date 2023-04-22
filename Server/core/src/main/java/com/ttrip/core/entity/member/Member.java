package com.ttrip.core.entity.member;

import com.ttrip.core.entity.BaseEntity;
import com.ttrip.core.enum2.Gender;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column(nullable = false, unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID uuid;
    @Column
    private String phoneNumber;
    @Column
    private String password;
    @Column(length = 6, unique = true)
    private String nickname;
    @Column(length = 20)
    private String intro;
    @Column
    private String imagePath;
    @Column
    private String fcmToken;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column
    private LocalDate birthday;
    @Column
    private Boolean shareLocation;

    @Builder
    public Member(UUID uuid, String phoneNumber, String password, String nickname, String intro, String imagePath, String fcmToken, Gender gender, LocalDate birthday, Boolean shareLocation) {
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
    }
}
