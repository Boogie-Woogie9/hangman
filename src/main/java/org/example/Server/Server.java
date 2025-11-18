package org.example.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class Server extends UnicastRemoteObject implements IServer {
	private static final long serialVersionUID = 1L;
	private Data data;
	private StringBuilder sb;
	private String[] matrix;

	protected Server() throws RemoteException {
		super();
		this.data = new Data();
		this.sb = new StringBuilder();
		this.matrix = new String[]{
				".____",
				"|    ",
				"|    ",
				"|    ",
				"|    "
		};
		System.out.println("Сервер инициализирован. Доступно слов: " + Data.wordsPool.size());
	}

	@Override
	public String getMenu() throws RemoteException {
		StringBuilder sb = new StringBuilder();
		sb.append("Игра виселица!\n")
				.append("1. Новая игра\n")
				.append("2. Выйти\n")
				.append("Выберите вариант: ");
		return sb.toString();
	}

	@Override
	public GameState startGame() throws RemoteException {
		GameState state = new GameState();
		matrix = new String[]{
				".____",
				"|    ",
				"|    ",
				"|    ",
				"|    "
		};

		// Выбираем случайное слово
		String selectedWord = Data.wordsPool
				.get(new Random().nextInt(Data.wordsPool.size()));

		state.setSelectedWord(selectedWord);
		state.setCurrentWord(new char[selectedWord.length()]);

		// Заполняем текущее слово звездочками
		for (int i = 0; i < selectedWord.length(); i++) {
			state.getCurrentWord()[i] = '*';
		}

		state.setMessage("Слово загадано! Выбирайте букву!");
		updateMatrix(state);

		return state;
	}

	@Override
	public GameState makeGuess(char letter, GameState state) throws RemoteException {

		String selectedWord = state.getSelectedWord();
		char[] currentWord = state.getCurrentWord();
		boolean wrongChar = true;

		// Проверяем угаданную букву
		for (int i = 0; i < selectedWord.length(); i++) {
			if (selectedWord.charAt(i) == letter) {
				currentWord[i] = letter;
				wrongChar = false;
			}
		}

		if (wrongChar) {
			state.setErrorAmount(state.getErrorAmount() + 1);
		}

		// Обновляем матрицу виселицы
		updateMatrix(state);

		// Проверяем условия окончания игры
		String currentWordStr = new String(currentWord);
		if (state.getErrorAmount() >= 7) {
			state.setGameOver(true);
			state.setMessage("Поражение! Загаданное слово: " + selectedWord);
		} else if (!currentWordStr.contains("*")) {
			state.setGameWon(true);
			state.setMessage("Вы выиграли! Мои поздравления!");
		} else {
			state.setMessage("Текущее состояние: " + currentWordStr +
					" | Ошибок: " + state.getErrorAmount() + "/7");
		}

		return state;
	}

	@Override
	public void exitGame() throws RemoteException {
		System.out.println("Клиент отключился");
	}

	private void updateMatrix(GameState state) {

		switch(state.getErrorAmount()) {
			case 1: matrix[1] = "|  | "; break;
			case 2: matrix[2] = "|  O "; break;
			case 3: matrix[3] = "|  | "; break;
			case 4: matrix[3] = "| /| "; break;
			case 5: matrix[3] = "| /|\\"; break;
			case 6: matrix[4] = "| /  "; break;
			case 7: matrix[4] = "| / \\"; break;
		}

		state.setMatrix(matrix);
	}

	public static void main(String[] args) {
		try {
			Server server = new Server();
			Registry registry = LocateRegistry.createRegistry(9001);
			registry.rebind("hangman-server", server);
			System.out.println("Сервер запущен на порту 9001...");
		} catch (RemoteException e) {
			System.err.println("Ошибка запуска сервера: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
