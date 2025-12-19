package com.library.library.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private MessageType messageType;
    private String ofStatic;

    public String prepareErrorMessage() {
        StringBuilder builder = new StringBuilder();

        builder.append(messageType.getMessage());

        if (this.ofStatic != null) {
            builder.append(" : ").append(ofStatic);
        }
        return builder.toString();
    }

}