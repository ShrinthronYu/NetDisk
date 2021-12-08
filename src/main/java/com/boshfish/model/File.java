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
@Table(name = "file")
@TableName("user")
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20) comment 'FileId'")
    private Long fileId;

    @Column(columnDefinition = "varchar(500) comment 'TimeStamp'")
    private String timeStamp;

    @Column(columnDefinition = "varchar(500) comment 'FileUrl'")
    private String fileUrl;

    @Column(columnDefinition = "bigint(10) comment 'FileSize'")
    private Long fileSize;

    @Column(columnDefinition = "int(1) comment 'StorageType 0-Local, 1-AliCloud, 2-FastDFS'")
    private Integer storageType;

    @Column(columnDefinition = "varchar(32) comment 'md5标识'")
    private String identifier;

    @Column(columnDefinition = "int(11) comment '文件引用数量'")
    private Integer pointCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        File file = (File) o;
        return fileId != null && Objects.equals(fileId, file.fileId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}