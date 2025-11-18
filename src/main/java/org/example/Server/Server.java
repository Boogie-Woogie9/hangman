package org.example.Server;

import org.example.Game.Data;
import org.example.Game.Game;
import org.example.Game.GameState;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Server extends UnicastRemoteObject implements IServer {
	private static final long serialVersionUID = 1L;
	private Data data;
	private Map<String, Game> clientGames;

	protected Server() throws RemoteException {
		super();
		this.data = new Data();
		this.clientGames = new HashMap<>();
		System.out.println("Сервер инициализирован. Доступно слов: " + Data.wordsPool.size());
	}

	@Override
	public String getMenu() throws RemoteException {
		return "Игра виселица!\n" +
				"1. Новая игра\n" +
				"2. Выйти\n" +
				"Выберите вариант: ";
	}

	@Override
	public GameState startGame() throws RemoteException {
		String clientId = getClientId();
		Game game = new Game();
		game.startNewGame();
		clientGames.put(clientId, game);

		System.out.println("Новая игра начата для клиента: " + clientId);
		return game.getGameState();
	}

	@Override
	public GameState makeGuess(char letter, GameState state) throws RemoteException {
		String clientId = getClientId();
		Game game = clientGames.get(clientId);

		if (game == null) {
			// Если игры нет, создаем новую
			state.setMessage("Ошибка: игра не начата. Выберите '1. Новая игра'");
			return state;
		}

		// Делегируем логику игры классу Game
		GameState newState = game.makeGuess(letter);

		// Если игра завершена, удаляем ее из активных
		if (!game.isGameActive()) {
			clientGames.remove(clientId);
			System.out.println("Игра завершена для клиента: " + clientId);
		}

		return newState;
	}

	@Override
	public void exitGame() throws RemoteException {
		String clientId = getClientId();
		clientGames.remove(clientId);
		System.out.println("Клиент отключился: " + clientId);
	}

	private String getClientId() {
		try {
			return java.rmi.server.RemoteServer.getClientHost();
		} catch (Exception e) {
			return "unknown";
		}
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