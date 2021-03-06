package engine;

import java.awt.event.KeyListener;

/**
 * @author Horatiu Cirstea
 * 
 * controleur qui envoie des commandes au jeu 
 * 
 */
public interface GameController extends KeyListener {

	enum KeyboardMode{
		AZERTY, QWERTY;
	}

	/**
	 * quand on demande les commandes, le controleur retourne la commande en
	 * cours
	 * 
	 * @return commande faite par le joueur
	 */
	public Cmd getCommand();
	public void setCommand(Cmd cmd);

	/**
	 * change le mode de contrôles du héros
	 */
	public void setCurrentMode(KeyboardMode keyboardMode);

}
