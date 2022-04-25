package com.dailycodebuffer.filemngt.service;

import com.dailycodebuffer.filemngt.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file) throws Exception;

    Attachment getAttachment(Long fileId) throws Exception;
}
