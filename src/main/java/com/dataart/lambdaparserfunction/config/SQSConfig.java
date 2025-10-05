package com.dataart.lambdaparserfunction.config;

import com.dataart.lambdaparserfunction.props.AWSProps;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@RequiredArgsConstructor
public class SQSConfig {

    private final AWSProps awsProps;

    @Bean
    public SqsClient sqsClient() {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(awsProps.accessKey(), awsProps.secretKey());
        return SqsClient.builder()
                .region(Region.of(awsProps.region()))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }
}
