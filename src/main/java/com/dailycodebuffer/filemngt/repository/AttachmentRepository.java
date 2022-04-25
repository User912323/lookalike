package com.dailycodebuffer.filemngt.repository;

import com.dailycodebuffer.filemngt.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @Query("select u from Attachment u where u.id = :id")
     List<Attachment> findByEmailAddress(@Param("id") long id);

}
