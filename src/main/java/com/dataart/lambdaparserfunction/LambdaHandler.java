package com.dataart.lambdaparserfunction;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.dataart.lambdaparserfunction.exception.DocumentParseException;
import com.dataart.lambdaparserfunction.model.Chat;
import com.dataart.lambdaparserfunction.service.ChatService;
import com.dataart.lambdaparserfunction.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LambdaHandler implements RequestHandler<S3Event, String> {

    private final S3Service s3Service;
    private final ChatService chatService;

    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        try {
            String fileContent = s3Service.downloadFileFromS3Bucket(s3Event);
            Chat chat = chatService.getChatObjectFromS3Document(fileContent);
            chatService.saveChatFromFile(chat, Long.valueOf(chat.getChatId()));
            return "Successfully SAVED and PARSED";
        } catch (IOException e) {
            throw new DocumentParseException("Failed during parsing message from S3: " + e.getMessage());
        }
    }
}
