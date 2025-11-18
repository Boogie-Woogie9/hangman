package org.example.Server;

import org.example.Game.GameState;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {
	String getMenu() throws RemoteException;
	GameState startGame() throws RemoteException;
	GameState makeGuess(char letter, GameState state) throws RemoteException;
	void exitGame() throws RemoteException;

}
