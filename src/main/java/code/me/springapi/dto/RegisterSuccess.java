package code.me.springapi.dto;

import lombok.Getter;

@Getter
public class RegisterSuccess extends Success {

    public RegisterSuccess(String message) {
        super(message);
    }
}
