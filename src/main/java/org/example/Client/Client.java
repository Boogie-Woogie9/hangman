package org.example.Client;

import org.example.Server.IServer;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

	private IServer server;
	private Scanner scanner;
	private GameState currentState;

	public Client() {
		this.scanner = new Scanner(System.in);
		this.currentState = null;
	}

	// Подключаемся к серверу
	public void connect() {
		try {
			Registry registry = LocateRegistry.getRegistry("127.0.0.1", 9001);
			server = (IServer) registry.lookup("hangman-server");
			System.out.println("Успешно подключено к серверу!");
			showMenu();
		} catch (RemoteException | NotBoundException e) {
			System.err.println("Ошибка подключения: " + e.getMessage());
		}
	}

	// Запрашиваем меню у сервера
	private void showMenu() {
		try {
			while (true) {
				String menu = server.getMenu();
				System.out.print(menu);

				String choice = scanner.nextLine().trim();

				// Выбираем опции в меню
				switch (choice) {
					case "1" -> startGame();
					case "2" -> {
						server.exitGame();
						System.out.println("Выход из игры...");
						return;
					}
					default -> System.out.println("Неверный выбор. Попробуйте снова.");
				}
			}
		} catch (RemoteException e) {
			System.err.println("Ошибка связи с сервером: " + e.getMessage());
		}
	}

	// Начинаем игру
	private void startGame() {
		try {
			currentState = server.startGame();
			playGame();
		} catch (RemoteException e) {
			System.err.println("Ошибка начала игры: " + e.getMessage());
		}
	}

	private void playGame() {
		try {
			while (currentState != null && !currentState.isGameOver() && !currentState.isGameWon()) {
				// Отображаем виселицу
				displayGameState();

				// Запрашиваем букву
				System.out.print("Введите букву: ");
				String input = scanner.nextLine().trim();

				if (input.length() != 1) {
					System.out.println("Пожалуйста, введите одну букву!");
					continue;
				}

				char letter = input.charAt(0);
				currentState = server.makeGuess(letter, currentState);
			}

			// Отображаем финальное состояние
			displayGameState();
			System.out.println(currentState.getMessage());
			System.out.println();

		} catch (RemoteException e) {
			System.err.println("Ошибка во время игры: " + e.getMessage());
		}
	}

	private void displayGameState() {
		if (currentState == null) return;

		// Отображаем виселицу
		String[] matrix = currentState.getMatrix();
		for (String line : matrix) {
			System.out.println(line);
		}

		// Отображаем текущее слово
		if (currentState.getCurrentWord() != null) {
			System.out.println("Слово: " + new String(currentState.getCurrentWord()));
		}

		// Отображаем сообщение
		if (currentState.getMessage() != null) {
			System.out.println(currentState.getMessage());
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.connect();
	}
}