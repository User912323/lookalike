package com.dailycodebuffer.filemngt.service;

import com.dailycodebuffer.filemngt.entity.Attachment;
import com.dailycodebuffer.filemngt.repository.AttachmentRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private AttachmentRepository attachmentRepository;
    PasswordEncoder passwordEncoder;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public Attachment saveAttachment(MultipartFile file, String file_id, String path, String username, String password, String no_hp) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename().split("\\.")[0]);
        File tes = new File(String.valueOf(file));
        try {
            if (fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence "
                        + fileName);
            }
            Match match = new Match();
            Attachment attachment = new Attachment();
            String encodePassword = this.passwordEncoder.encode(password);
//            attachment.setPassword(encodePassword);
//            Attachment attachment
//                    = new Attachment(fileName,
//                    file.getContentType(),
//                    file.getBytes(), file.getBytes(), match.similar, file_id, path,username,attachment.setPassword(encodePassword),no_hp);
            attachment.setFileName(fileName);
            attachment.setFileType(file.getContentType());
            attachment.setData(file.getBytes());
            attachment.setSimilarity(match.similar);
            attachment.setFile_id(file_id);
            attachment.setPath(path);
            attachment.setUsername(username);
            attachment.setPassword(encodePassword);
            attachment.setNo_hp(no_hp);

            return attachmentRepository.save(attachment);

        } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
        }
    }


    // UPDATE
    @Override
    public void updateMatch(String file_id, float similarity, MultipartFile file, String username, String password, String no_hp) throws IOException {

        Attachment att = attachmentRepository.findById(file_id);
        att.setSimilarity(similarity);
        att.setData2(file.getBytes());
        att.setUsername(username);
//        att.setPassword(password);
        att.setNo_hp(no_hp);
        String encodePassword = this.passwordEncoder.encode(password);
        att.setPassword(encodePassword);
        attachmentRepository.save(att);

    }

//    @Override
//    public void updateSimilar(String file_id, float similarity) {
//        attachmentRepository.updateSimilar(file_id, similarity);
//    }

    @Override
    public void updateSimilar(String file_id, float similarity, MultipartFile file) throws IOException {
        attachmentRepository.updateSimilar(file_id, similarity);
        attachmentRepository.updatedata2(file_id,file.getBytes());
    }
//
//    @Override
//    public void updateSimilar(String file_id, float similarity, MultipartFile file, String username, String password, String no_hp) throws IOException {
//        attachmentRepository.updateSimilar(file_id, similarity);
//        attachmentRepository.updateuser(file_id,username);
//        attachmentRepository.updatepass(file_id,password);
//        attachmentRepository.updateno(file_id,no_hp);
//        attachmentRepository.updatedata2(file_id,file.getBytes());
//    }

    @Override
    public Attachment getAttachment(Long fileId) throws Exception {
        return attachmentRepository
                .findById(fileId)
                .orElseThrow(
                        () -> new Exception("File not found with Id: " + fileId));
    }
}
