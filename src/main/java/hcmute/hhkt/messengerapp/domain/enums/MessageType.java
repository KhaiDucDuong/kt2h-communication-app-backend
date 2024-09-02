package hcmute.hhkt.messengerapp.domain.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum MessageType {
    TEXT(200),
    RECORDING(201);

    private int value;

    MessageType(int value){
        this.value = value;
    }

}
