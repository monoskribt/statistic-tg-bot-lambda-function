package com.dataart.lambdaparserfunction.model;

import com.dataart.lambdaparserfunction.model.submodel.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Chat {

    @JsonIgnore
    private String id;

    @JsonProperty(value = "name")
    private String chatName;

    @JsonProperty(value = "id")
    private String chatId;

    @JsonProperty(value = "messages")
    private List<Message> messages;
}
