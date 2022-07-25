package com.example.cloudstorage.repository;

import com.example.cloudstorage.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {

    @Query(value = "select * from file s where s.user_id = ?1 order by s.id desc limit ?2", nativeQuery = true)
    List<FileEntity> findAllByUserIdWithLimit(long userId, int limit);

    Optional<FileEntity> findByUserIdAndName(long userId, String name);

    void removeByUserIdAndName(long userId, String name);
}
