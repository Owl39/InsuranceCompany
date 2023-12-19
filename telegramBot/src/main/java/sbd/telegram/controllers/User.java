package sbd.telegram.controllers;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    // Методы для работы с состоянием State
    private State state;
    // Методы для работы с chatId
    private Long chatId;

    public User() {
        this.state = State.NONE;
    }

}
