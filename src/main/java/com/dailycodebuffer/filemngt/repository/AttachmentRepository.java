package com.dailycodebuffer.filemngt.repository;

import com.dailycodebuffer.filemngt.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @Query("select u from Attachment u where u.file_id = :file_id")
     List<Attachment> findByEmailAddress(@Param("file_id") String file_id);

//    Attachment findFile_id(String file_id);
    Attachment findById(String file_id);

    @Modifying
    @Transactional
    @Query("update Attachment u set u.similarity = :similarity where u.file_id = :file_id")
    void updateSimilar(@Param("file_id") String file_id,@Param("similarity") float similarity);

    @Modifying
    @Transactional
    @Query("update Attachment a set a.username = :username where a.file_id = :file_id")
    void updateuser(@Param("file_id") String file_id,@Param("username") String username);

    @Modifying
    @Transactional
    @Query("update Attachment u set u.password = :password where u.file_id = :file_id")
    void updatepass(@Param("file_id") String file_id,@Param("password") String password);

    @Modifying
    @Transactional
    @Query("update Attachment u set u.no_hp = :no_hp where u.file_id = :file_id")
    void updateno(@Param("file_id") String file_id,@Param("no_hp") String no_hp);

    @Modifying
    @Transactional
    @Query("update Attachment u set u.data2 = :data2 where u.file_id = :file_id")
    void updatedata2(@Param("file_id") String file_id,@Param("data2") byte[] file);
}
