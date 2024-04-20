package com.example.videoopsservice.services;

import com.example.videoopsservice.entities.LoadFile;
import com.example.videoopsservice.entities.UserDetails;
import com.example.videoopsservice.messaging.FileMessage;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.io.IOUtils;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Service
public class VideoOpsService {

    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFsOperations operations;
    @Autowired
    private GraphQLClientService graphQLClientService;

    public FileMessage uploadFile(MultipartFile file, String jwt) throws Exception {

        String token = jwt.split(" ")[1];
        UserDetails userDetailsMono = graphQLClientService.getEmail(token);

        DBObject metadata = new BasicDBObject();
        metadata.put("fileType", file.getContentType());

        Object fileID = gridFsTemplate.store(file.getInputStream(),
                file.getOriginalFilename(), file.getContentType(), metadata);

        return new FileMessage(fileID.toString(), userDetailsMono.getEmail());

    }

    public LoadFile downloadVideo(String id) throws Exception {
//        System.out.println(id);
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));

//        System.out.println(gridFSFile.getFilename());
        LoadFile file = new LoadFile();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            file.setFileName(gridFSFile.getFilename());
            file.setFileSize(gridFSFile.getLength());
            file.setFileType(gridFSFile.getMetadata().get(("_contentType")).toString());

//            System.out.println(file.getFileName()+ file.getFileType() + file.getFileSize());
            file.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        }

        return file;
    }
}
