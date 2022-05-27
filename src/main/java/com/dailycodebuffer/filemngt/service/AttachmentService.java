package com.dailycodebuffer.filemngt.service;

import com.dailycodebuffer.filemngt.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AttachmentService {
//    Attachment saveAttachment(MultipartFile file,String file_id, String path) throws Exception;

//    // UPDATE
//    Attachment updateMatch(Long Id, Attachment attachmentDetails);

    // UPDATE
//    void updateMatch(String file_id, float similarity);

    // UPDATE
//    void updateMatch(String file_id, float similarity, MultipartFile file, String username, String password, String no_hp) throws IOException;

//    void updateSimilar(String file_id, float similarity);

    Attachment saveAttachment(MultipartFile file, String file_id, String path, String username, String password, String no_hp) throws Exception;

    // UPDATE
    void updateMatch(String file_id, float similarity, MultipartFile file, String username, String password, String no_hp) throws IOException;

//    void updateSimilar(String file_id, float similarity, MultipartFile file, String username, String password, String no_hp) throws IOException;

    void updateSimilar(String file_id, float similarity, MultipartFile file) throws IOException;

    Attachment getAttachment(Long fileId) throws Exception;
}
