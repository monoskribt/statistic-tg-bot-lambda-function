package com.dataart.lambdaparserfunction.service;

import com.dataart.lambdaparserfunction.dto.UserTelegramMessageQueue;
import com.dataart.lambdaparserfunction.model.Chat;
import com.dataart.lambdaparserfunction.props.SQSProps;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class SQSService {

    private final SQSProps sqsProps;
    private final SqsClient sqsClient;

    private ObjectMapper objectMapper;

    public void sendToQueue(UserTelegramMessageQueue messageQueue) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(messageQueue);

        try {
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(sqsProps.queueUrl())
                    .messageBody(jsonMessage)
                    .build();
            sqsClient.sendMessage(sendMsgRequest);
        } catch (Exception e) {
            log.error("Error during sending message to SQS queue: {}", e.getMessage());
        }
    }
}
