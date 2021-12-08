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
@Table(name = "userfile", uniqueConstraints = {
        @UniqueConstraint(name = "fileIndex", columnNames = {"fileName", "filePath", "extendName"})})
@TableName("userfile")
@Entity
public class UserFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20) comment 'UserFileId'")
    private Long userFileId;

    @Column(columnDefinition = "bigint(20) comment 'UserId'")
    private Long userId;

    @Column(columnDefinition = "bigint(20) comment 'FileId'")
    private Long fileId;

    @Column(columnDefinition = "varchar(100) comment 'FileName'")
    private String fileName;

    @Column(columnDefinition = "varchar(500) comment 'FilePath'")
    private String filePath;

    @Column(columnDefinition = "varchar(100) comment 'ExtendName'")
    private String extendName;

    @Column(columnDefinition = "int(1) comment 'is Dir 0-N, 1-Y'")
    private Integer isDir;

    @Column(columnDefinition = "varchar(25) comment 'UploadTime'")
    private String uploadTime;

    @Column(columnDefinition = "int(11) comment '删除标志 0-未删除 1-已删除'")
    private Integer deleteFlag;

    @Column(columnDefinition = "varchar(25) comment '删除时间'")
    private String deleteTime;

    @Column(columnDefinition = "varchar(50) comment '删除批次号'")
    private String deleteBatchNum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserFile userFile = (UserFile) o;
        return userFileId != null && Objects.equals(userFileId, userFile.userFileId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}