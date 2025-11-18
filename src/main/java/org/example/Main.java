package org.example;

import org.example.Game.Game;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean isActive = true;

        while (isActive){
            System.out.println("1 - Новая игра");
            System.out.println("2 - Выйти");

            Scanner scanner = new Scanner(System.in);
            char option = scanner.next().charAt(0);

            if (option == '1'){
                Game game = new Game();
                game.start();
            } else if (option == '2') {
                return;
            }
        }
    }
}