package com.dailycodebuffer.filemngt.controller;

import com.dailycodebuffer.filemngt.ResponseData;
import com.dailycodebuffer.filemngt.ResponseResult;
import com.dailycodebuffer.filemngt.entity.Attachment;
import com.dailycodebuffer.filemngt.repository.AttachmentRepository;
import com.dailycodebuffer.filemngt.service.AttachmentService;
import com.dailycodebuffer.filemngt.service.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
public class AttachmentController {

    private AttachmentService attachmentService;
    @Autowired
    AttachmentRepository attachmentRepository;


    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

//    Path root = Paths.get("uploads");
    Path root = Paths.get("C:\\Me'\\Intellij\\tes\\coba\\coba\\image");

    public String save(MultipartFile file, String add) {
        try {
            Path filePath = this.root.resolve(add + ".jpg");
            Files.copy(file.getInputStream(), filePath);
            return filePath.toString();
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String file_id,@RequestParam String username, @RequestParam String password, @RequestParam String no_hp) throws Exception {
        System.out.println("fileid >> " + file_id);
        Attachment attachment ;
        String downloadURl = "";
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss" + file_id).format(new Date());

        attachment = attachmentService.saveAttachment(file, file_id, save(file, timeStamp),username,password,no_hp);


        return new ResponseData(attachment.getFileName(),
                downloadURl,
                file.getContentType(),
                file.getSize());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment attachment = null;
        attachment = attachmentService.getAttachment(Long.valueOf(fileId));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName()
                                + "\"")
                .body(new ByteArrayResource(attachment.getData()));

    }

    @PostMapping("/match")
    public ResponseResult matching(@RequestParam("file") MultipartFile file, @RequestParam String file_id ) throws Exception {
        Match match = new Match();

        com.dailycodebuffer.filemngt.wrapper.ContextWrapper.getContext().getAutowireCapableBeanFactory().autowireBean(match);
        match.init();
        List<Attachment> imageList = attachmentRepository.findByEmailAddress(file_id);
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        match.menuEnrollFace(imageList, save(file, timeStamp + file_id + "New"));

        for (int i = 0; i < match.sim_ind.size(); ++i) {
            attachmentService.updateSimilar(file_id, match.similar,file);
        }
//        if (match.result_similar == null)
//        {
//         float gagal = 0.1f;
//         match.result_similar = Collections.singletonList(gagal);
//        }
        return new ResponseResult(match.result_similar
        );
    }
}
