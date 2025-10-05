package com.dataart.lambdaparserfunction.service;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Client s3Client;

    private ObjectMapper objectMapper;

    public String downloadFileFromS3Bucket(S3Event s3Event) throws IOException {
        var record = s3Event.getRecords().get(0);
        String bucket = record.getS3().getBucket().getName();
        String key = record.getS3().getObject().getKey();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build()
        );

        String fileContent;

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s3Object))) {
            fileContent = bufferedReader.lines().collect(Collectors.joining("\n"));
            log.info("Content after getting file from S3 bucket: {}" , fileContent);
        }

        return fileContent;
    }
}
