package sbd.telegram.controllers;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private UserState state;
    private Long chatId;

    public User() {
        this.state = UserState.NONE;
    }
}
