package com.dailycodebuffer.filemngt.controller;

import com.dailycodebuffer.filemngt.Download;
import com.dailycodebuffer.filemngt.ResponseData;
import com.dailycodebuffer.filemngt.entity.Attachment;
import com.dailycodebuffer.filemngt.repository.AttachmentRepository;
import com.dailycodebuffer.filemngt.service.AttachmentService;
import com.dailycodebuffer.filemngt.service.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class AttachmentController {

    private AttachmentService attachmentService;
    @Autowired
    AttachmentRepository attachmentRepository;


    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }


    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file")MultipartFile file) throws Exception {
        Attachment attachment = null;
        String downloadURl = "";
        attachment = attachmentService.saveAttachment(file);
        downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(String.valueOf(attachment.getId()))
                .toUriString();

        return new ResponseData(attachment.getFileName(),
                downloadURl,
                file.getContentType(),
                file.getSize());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment attachment = null;
        attachment = attachmentService.getAttachment(Long.valueOf(fileId));

        String link = "http://localhost:8080/download/"+fileId;
        File out = new File("C:\\Me'\\Intellij\\tes\\spring-boot-file-upload-main\\spring-boot-file-upload-main\\tes.jpg");

//        Download download = new Download(link,out);
//        Thread thread = new Thread(download, "T1");
//        thread.start();
//        Thread.sleep(2000);
//        download.setExit(true);

        // creating two objects t1 & t2 of MyThread
        int i = 0;
        do {
            Download t1 = new Download(link,out);
            try {
                Thread.sleep(1);
                // t1 is an object of MyThread
                // which has an object t
                // which is of type Thread
                t1.t.interrupt();

                Thread.sleep(5);
            }
            catch (InterruptedException e) {
                System.out.println("Caught:" + e);
            }
            System.out.println("Exiting the main Thread");
            t1.t.interrupt();

            System.out.println(i);
            i++;
        }
        while (i < 1);

        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName()
                + "\"")
                .body(new ByteArrayResource(attachment.getData()));

    }
//
    //get all
    @GetMapping(path = "/{fileId}", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Attachment> getMatch(@PathVariable String fileId) throws Exception {
        Attachment attachment = null;
        attachment = attachmentService.getAttachment(Long.valueOf(fileId));
        return attachmentRepository.findByEmailAddress(Long.parseLong(fileId));
    }

    //match
    @PostMapping(path = "/match", consumes = "application/json", produces = "application/json")
    public String yukMatch(@RequestBody Attachment attachment)    throws IOException {
        System.out.println("testMatch");
     /*

        test.menuEnrollFace(imageList);*/
        List<Attachment> imageList = attachmentRepository.findByEmailAddress((int) attachment.getId());

        Match match = new Match();
        com.dailycodebuffer.filemngt.wrapper.ContextWrapper.getContext().getAutowireCapableBeanFactory().autowireBean(match);

        match.init();
        match.menuEnrollFace(imageList);
//        matchRepository.save(lookalike);
        return "lookalikeArrayList.toString()";

    }


}
