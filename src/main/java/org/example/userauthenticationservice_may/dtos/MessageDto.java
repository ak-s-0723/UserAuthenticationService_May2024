package org.example.userauthenticationservice_may.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private String to;
    private String from;
    private String subject;
    private String body;
}
