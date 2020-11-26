package model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import engine.Cmd;
import engine.Game;
import views.EndScreen;
import views.GameScreen;
import views.MazeScreen;
import views.MenuScreen;

/**
 * @author Horatiu Cirstea, Vincent Thomas
 *
 *         Version avec personnage qui peut se deplacer. A completer dans les
 *         versions suivantes.
 * 
 */
public class PacmanGame implements Game {
	private GameScreen currentScreen;
	public enum GameState{
		Maze,MainMenu,Pause,Victory,Lost,Quit
	}
	private GameState currentState;


	/**
	 * constructeur avec fichier source pour le help
	 * 
	 */
	public PacmanGame() throws IOException {
		currentScreen = new MenuScreen(this);
		currentState = GameState.MainMenu;
	}

	/**
	 * faire evoluer le jeu suite a une commande
	 * 
	 * @param commande commande reçu par le clavier
	 */
	@Override
	public void evolve(Cmd commande) throws IOException {
		currentScreen.update(commande);
	}

	/**
	 * Affichage de l'écran en cours
	 * @param im frame buffer
	 */
	public void draw(BufferedImage im) {
		currentScreen.display(im);
	}

	/**
	 * verifie si le jeu est fini
	 */
	@Override
	public boolean isFinished() {
		return currentState == GameState.Quit;
	}

	/**
	 *
	 * @return état courant du jeu
	 */
	public GameState getCurrentState() {
		return currentState;
	}

	/**
	 *
	 * @param currentState permet de changer l'état courrant du jeu
	 */
	public void setCurrentState(GameState currentState) throws IOException {

		swapScreen(currentState);
		this.currentState = currentState;
	}

	/**
	 * Change l'écran à afficher
	 * @param currentState nouvel état du jeu
	 * @throws IOException Chargement d'images non réussit
	 */
	private void swapScreen(GameState currentState) throws IOException {
		if(currentState == GameState.Maze)
			currentScreen = new MazeScreen(this);
		if(currentState == GameState.Lost)
			currentScreen = new EndScreen(this,"Lost");
		if(currentState == GameState.Victory)
			currentScreen = new EndScreen(this,"Victory");
		if(currentState == GameState.MainMenu)
			currentScreen = new MenuScreen(this);
	}
}
