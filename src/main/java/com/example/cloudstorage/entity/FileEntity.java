package com.example.cloudstorage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file")
public class FileEntity {
    public FileEntity(String name, String type, byte[] data, long size) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.size = size;
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JsonProperty("filename")
    @Column
    private String name;

    @JsonIgnore
    @Column
    private String type;

    @JsonIgnore
    @Lob
    private byte[] data;

    @Column
    private Long size;

    @JsonIgnore
    @Column
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FileEntity that = (FileEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
