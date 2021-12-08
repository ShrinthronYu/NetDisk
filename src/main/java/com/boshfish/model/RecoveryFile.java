package com.boshfish.model;

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
@Table(name = "recoveryfile")
@Entity
@TableName("recoveryfile")
public class RecoveryFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20)")
    private Long recoveryFileId;

    @Column(columnDefinition = "bigint(20)")
    private Long userFileId;

    @Column(columnDefinition = "varchar(25)")
    private String deleteTime;

    @Column(columnDefinition = "varchar(50)")
    private String deleteBatchNum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RecoveryFile that = (RecoveryFile) o;
        return recoveryFileId != null && Objects.equals(recoveryFileId, that.recoveryFileId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}