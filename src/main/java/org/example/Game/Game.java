package org.example.Game;

import java.util.Random;

public class Game {
    private String selectedWord;
    private String currentWord;
    private int errorAmount;
    private boolean gameOver;
    private boolean gameWon;

    public Game() {}

    public void startNewGame() {
        // Берём слово, убираем лишние пробелы и приводим к верхнему регистру
        this.selectedWord = Data.wordsPool
                .get(new Random().nextInt(Data.wordsPool.size()))
                .trim()
                .toUpperCase();

        // Создаем строку со звездочками той же длины
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedWord.length(); i++) {
            sb.append('*');
        }
        this.currentWord = sb.toString();

        // Сбрасываем состояние
        this.errorAmount = 0;
        this.gameOver = false;
        this.gameWon = false;
    }

    public GameState makeGuess(char letter) {
        if (gameOver || gameWon) {
            return getGameState();
        }

        boolean wrongChar = true;
        char upperLetter = Character.toUpperCase(letter);

        // Проверяем угаданную букву
        StringBuilder newCurrentWord = new StringBuilder();
        for (int i = 0; i < selectedWord.length(); i++) {
            // Надёжное сравнение: оба символа в верхнем регистре
            if (Character.toUpperCase(selectedWord.charAt(i)) == upperLetter) {
                newCurrentWord.append(upperLetter);
                wrongChar = false;
            } else {
                newCurrentWord.append(currentWord.charAt(i));
            }
        }

        currentWord = newCurrentWord.toString();

        if (wrongChar) {
            errorAmount++;
        }

        // Проверяем условия окончания игры
        if (errorAmount >= 7) {
            gameOver = true;
        } else if (!currentWord.contains("*")) {
            gameWon = true;
        }

        return getGameState();
    }

    public GameState getGameState() {
        GameState state = new GameState();

        // Передаём выбранное слово клиенту только если игра закончена (чтобы не раскрывать ответ)
        if (gameOver || gameWon) {
            state.setSelectedWord(selectedWord);
        } else {
            state.setSelectedWord(null);
        }

        state.setCurrentWord(currentWord.toCharArray());
        state.setErrorAmount(errorAmount);
        state.setGameOver(gameOver);
        state.setGameWon(gameWon);

        // Обновляем матрицу виселицы
        updateMatrix(state);

        // Формируем сообщение (только одно!)
        String message;
        if (gameWon) {
            message = "🎉 Вы выиграли! Мои поздравления!\nЗагаданное слово: " + selectedWord;
        } else if (gameOver) {
            message = "💀 Поражение!\nЗагаданное слово: " + selectedWord;
        } else if (errorAmount == 0) {
            message = "Слово загадано! Выбирайте букву!";
        } else {
            message = "Текущее состояние: " + currentWord +
                    " | Ошибок: " + errorAmount + "/7";
        }
        state.setMessage(message);

        return state;
    }

    // updateMatrix и isGameActive без изменений
    private void updateMatrix(GameState state) {
        String[] matrix = new String[]{
                ".____",
                "|    ",
                "|    ",
                "|    ",
                "|    "
        };

        int errors = state.getErrorAmount();

        if (errors >= 1) matrix[1] = "|  | ";
        if (errors >= 2) matrix[2] = "|  O ";
        if (errors >= 3) matrix[3] = "|  | ";
        if (errors >= 4) matrix[3] = "| /| ";
        if (errors >= 5) matrix[3] = "| /|\\";
        if (errors >= 6) matrix[4] = "| /  ";
        if (errors >= 7) matrix[4] = "| / \\";

        state.setMatrix(matrix);
    }

    public boolean isGameActive() {
        return !gameOver && !gameWon;
    }
}
