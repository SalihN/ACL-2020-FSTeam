package engine;

import java.io.IOException;

/**
 * @author Horatiu Cirstea, Vincent Thomas
 *
 *         un jeu qui peut evoluer (avant de se terminer) sur un plateau width x
 *         height
 */
public interface Game {

	/**
	 * methode qui contient l'evolution du jeu en fonction de la commande
	 * 
	 * @param userCmd
	 *            commande utilisateur
	 */
	public void evolve(Cmd userCmd) throws IOException;

	/**
	 * @return true si et seulement si le jeu est fini
	 */
	public boolean isFinished();

	/**
	 *
	 * @return mode actuel des contrôles
	 */
	public GameController.KeyboardMode getChosenMode();

}
