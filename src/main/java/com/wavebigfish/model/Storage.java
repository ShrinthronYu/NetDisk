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
@Table(name = "storage")
@Entity
@TableName("storage")
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20)")
    private Long storageId;

    @Column(columnDefinition = "bigint(20)")
    private Long userId;

    @Column(columnDefinition = "bigint(20)")
    private Long storageSize;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Storage storage = (Storage) o;
        return storageId != null && Objects.equals(storageId, storage.storageId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}