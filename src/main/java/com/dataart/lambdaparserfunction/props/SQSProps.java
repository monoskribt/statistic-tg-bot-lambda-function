package com.dataart.lambdaparserfunction.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud.aws.sqs")
public record SQSProps(String queueName, String queueUrl) {
}
