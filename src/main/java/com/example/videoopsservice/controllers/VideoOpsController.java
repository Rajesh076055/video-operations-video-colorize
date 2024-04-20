package com.example.videoopsservice.controllers;

import com.example.videoopsservice.entities.LoadFile;
import com.example.videoopsservice.messaging.FileMessage;
import com.example.videoopsservice.messaging.FileMessageProducer;
import com.example.videoopsservice.services.VideoOpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/video-ops")
public class VideoOpsController {

    @Autowired
    private VideoOpsService videoOpsService;

    @Autowired
    private FileMessageProducer fileMessageProducer;

    @PostMapping("/upload")
    public ResponseEntity<FileMessage> uploadVideo(@RequestBody MultipartFile file,
                                            @RequestHeader("Authorization") String jwt) throws Exception {
        try {
            FileMessage fileMessage = videoOpsService.uploadFile(file, jwt);
            //Send the message into the queue
            fileMessageProducer.sendMessage(fileMessage);
            return new ResponseEntity<>(fileMessage, HttpStatus.OK);
        } catch (Exception e) {
            throw  new Exception(e);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadVideo(@PathVariable String id) throws Exception {
        LoadFile file = videoOpsService.downloadVideo(id);

        System.out.println(file.getFileName());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFile()));
    }
}
