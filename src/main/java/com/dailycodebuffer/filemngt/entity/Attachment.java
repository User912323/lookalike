package com.dailycodebuffer.filemngt.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(name = "attachmentv2", columnNames = { "username" }) })
public class Attachment {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    private String fileName;
    private String fileType;
    private String file_id;
    private float similarity;
    private String path;

    private String username;
    private String password;
    private String no_hp;
    private String status;

    @Lob
    private byte[] data2;

    @Lob
    private byte[] data;

    public Attachment(String fileName, String fileType, byte[] data,byte[] data2, float similarity, String file_id, String path, String username, String password, String no_hp, String status) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.similarity = similarity;
        this.file_id = file_id;
        this.path = path;
        this.data2 = data2;
        this.username = username;
        this.password = password;
        this.no_hp = no_hp;
        this.status = status;
    }
}
