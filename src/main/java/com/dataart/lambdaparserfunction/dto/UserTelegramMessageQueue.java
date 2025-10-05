package com.dataart.lambdaparserfunction.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserTelegramMessageQueue {
    private Long chatId;
    private String message;
}
