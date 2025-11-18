package org.example.Game;

import java.util.*;

public class Game {
    private Data data; //данные из файла
    private String[] matrix; //отображение виселицы
    private byte errorAmount; //счетчик ошибок
    private String selectedWord; //загаданное слово

    public Game(){
        System.out.println("Игра виселица!");
    }
    public void start(){
        data = new Data();
        selectedWord = Data.wordsPool
                .get(new Random().nextInt(Data.wordsPool.size())); //случайно выбираем слово
        errorAmount = 0;
        matrix = new String[]{
                ".____",
                "|    ",
                "|    ",
                "|    ",
                "|    "
        };
        paint();
        System.out.println("Слово загадано! Выбирайте букву!");
        play();
    }

    private void paint(){
        switch(errorAmount){
            case 1:
                matrix[1] = "|  | ";
                break;
            case 2:
                matrix[2] = "|  O ";
                break;
            case 3:
                matrix[3] = "|  | ";
                break;
            case 4:
                matrix[3] = "| /| ";
                break;
            case 5:
                matrix[3] = "| /|\\";
                break;
            case 6:
                matrix[4] = "| /  ";
                break;
            case 7:
                matrix[4] = "| / \\  ";
                break;
        }
        for (String str : matrix) System.out.println(str);
    }
    private void play(){
        boolean game = true;
        boolean gameover = false;

        char[] strangeWord = selectedWord.toCharArray(); //загаданное слово в виде массива char
        char[] prevWord = new char[selectedWord.length()];
        Arrays.fill(prevWord, '*');
        System.out.println(new String(prevWord));

        Scanner input = new Scanner(System.in);
        while (game){

            char symbol = input.next().charAt(0);

            boolean wrongChar = true;
            StringBuilder buff = new StringBuilder();
            for (int i = 0; i < strangeWord.length; i++){
                if (strangeWord[i] == symbol){
                    buff.append(symbol);
                    wrongChar = false;
                } else {
                    buff.append(prevWord[i]);
                }
            }
            prevWord = buff.toString().toCharArray();

            if (wrongChar) errorAmount++;

            if (errorAmount == 7 || !(new String(prevWord).contains("*"))){
                if (errorAmount == 7) gameover = true;
                game = false;
            }
            paint();
            System.out.println(new String(prevWord));
        }
        if (!gameover){
            System.out.println("Вы выиграли! Мои поздравления!");
        } else {
            this.end();
        }
    }

    public void end(){
        System.out.println("Поражение!");
        System.out.println("Загаданное слово : " + selectedWord);
    }
}
