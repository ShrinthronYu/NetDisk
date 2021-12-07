package com.wavebigfish.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.*;

import org.hibernate.Hibernate;

import javax.persistence.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "user")
@TableName("user")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20) comment 'UserId'")
    private Long userId;

    @Column(columnDefinition = "varchar(30) comment 'UserName'")
    private String userName;

    @Column(columnDefinition = "varchar(35) comment 'Password'")
    private String password;

    @Column(columnDefinition = "varchar(15) comment 'Telephone'")
    private String telephone;

    @Column(columnDefinition = "varchar(20) comment 'Salt'")
    private String salt;

    @Column(columnDefinition = "varchar(30) comment 'RegisterTime'")
    private String registerTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return userId != null && Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}