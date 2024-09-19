package com.moa.moa.global.aws.s3.bucket.application;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.moa.moa.global.aws.s3.bucket.config.BucketConfig;
import com.moa.moa.global.aws.s3.bucket.domain.dto.AddBucketDto;
import com.moa.moa.global.common.message.FailHttpMessage;
import com.moa.moa.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BucketService {
    private final AmazonS3 amazonS3Client;
    private final BucketConfig bucketConfig;

    public AddBucketDto.Response upload(MultipartFile file) {
        return AddBucketDto.of(uploadS3(file));
    }

    public void read(String keyName) {
        boolean check = amazonS3Client.doesObjectExist(bucketConfig.getBucket(), keyName);

        if (!check) {
            throw new BusinessException(FailHttpMessage.Image.NOT_FOUND);
        }
    }

    private String uploadS3(MultipartFile file) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd_HH:mm:ss"));

        log.info("Bucket Service init time :: " + time);

        String keyName = time + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketConfig.getBucket(), keyName, inputStream, objectMetadata)
            );

        } catch (SdkClientException e) {
            log.info("BucketService uploadS3 SdkClientException");
            throw new SdkClientException(e);
        } catch (IOException e) {
            log.info("BucketService uploadS3 IOException");
            throw new RuntimeException(e);
        }
        log.info("Bucket Service uploadFileName : " + keyName);

        return keyName;
    }
}