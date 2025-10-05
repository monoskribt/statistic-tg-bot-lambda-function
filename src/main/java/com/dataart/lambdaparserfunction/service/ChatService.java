package com.dataart.lambdaparserfunction.service;

import com.dataart.lambdaparserfunction.dto.UserTelegramMessageQueue;
import com.dataart.lambdaparserfunction.exception.ChatNotFoundException;
import com.dataart.lambdaparserfunction.model.Chat;
import com.dataart.lambdaparserfunction.repo.ChatRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.dataart.lambdaparserfunction.constant.ConstantUserMessage.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final SQSService sqsService;
    private final ChatRepo chatRepo;

    private ObjectMapper objectMapper;

    public Chat getChatObjectFromS3Document(String fileContent) throws JsonProcessingException {
        return objectMapper.readValue(fileContent, Chat.class);
    }

    public void saveChatFromFile(Chat chat, Long chatId)
            throws JsonProcessingException {
        String hashId = createHashIdForChat(chat, objectMapper);
        chat.setId(hashId);

        if(chatIsExist(hashId)) {
            sqsService.sendToQueue(UserTelegramMessageQueue
                    .builder()
                    .chatId(chatId)
                    .message(ALREADY_EXISTS)
                    .build());
            log.info("Chat history is already present");
            return;
        }
        compareChatsBySizeOfMessages(chat, chatId);
    }

    private void compareChatsBySizeOfMessages(Chat chat, Long chatId) throws JsonProcessingException {
        try {
            Chat existingChat = getChatByChatId(chat.getChatId());
            int existingSizeOfMessages = existingChat.getMessages().size();
            int newSizeOfMessages = chat.getMessages().size();

            log.info("Count of existing messages: {} from the chat: {}",
                    existingSizeOfMessages, existingChat.getChatName());
            log.info("Count of new messages: {} from the chat: {}",
                    newSizeOfMessages, chat.getChatName());

            if (existingSizeOfMessages < newSizeOfMessages) {
                deleteChat(existingChat);
                saveChat(chat);
                sqsService.sendToQueue(UserTelegramMessageQueue
                        .builder()
                        .chatId(chatId)
                        .message(SUCCESSFULLY_UPDATED)
                        .build());
                log.info("Chat was successfully updated with new messages");
            } else {
                sqsService.sendToQueue(UserTelegramMessageQueue
                        .builder()
                        .chatId(chatId)
                        .message(CHAT_WAS_NOT_SAVED)
                        .build());
            }
        } catch (ChatNotFoundException e) {
            saveChat(chat);
            sqsService.sendToQueue(UserTelegramMessageQueue
                    .builder()
                    .chatId(chatId)
                    .message(SUCCESSFULLY_SAVED)
                    .build());
            log.info("Chat history saved");
        }
    }

    public void saveChat(Chat chatToSave) {
        chatRepo.save(chatToSave);
    }

    public void deleteChat(Chat chatToDelete) {
        chatRepo.delete(chatToDelete);
    }

    public Chat getChatByChatId(String chatId) {
        return chatRepo.findChatByChatId(chatId)
                .orElseThrow(() -> new ChatNotFoundException("Chat with id " + chatId + "is not present"));
    }

    public boolean chatIsExist(String chatId) {
        return chatRepo.existsChatByChatId(chatId);
    }

    public String createHashIdForChat(Chat chat, ObjectMapper objectMapper) throws JsonProcessingException {
        return DigestUtils.md5DigestAsHex(objectMapper.writeValueAsBytes(chat));
    }
}
