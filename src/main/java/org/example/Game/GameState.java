package org.example.Game;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private String[] matrix;
    private int errorAmount;
    private String selectedWord;
    private char[] currentWord;
    private boolean gameOver;
    private boolean gameWon;
    private String message;

    // Конструкторы, геттеры и сеттеры
    public GameState() {
        this.matrix = new String[]{
                ".____",
                "|    ",
                "|    ",
                "|    ",
                "|    "
        };
        this.errorAmount = 0;
        this.gameOver = false;
        this.gameWon = false;
    }
}
