package epi.man;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Cette interface représente un objet qui peut être collecté.
 *
 */
public interface Bonus {
    /**
     * position horizontale du bonus.
     * @return position X.
     */
    int getX();

    /**
     * Position verticale du bonus
     * @return position Y
     */
    int getY();

    /**
     * Indique si le bonus a été collecté.
     * @return true si le bonus est collecté sinon false.
     */
    boolean isCollected();

    /**
     * Affiche le bonus
     * @param batch {@link SpriteBatch}  utilisé pour le dessin.
     */
    void render(SpriteBatch batch);

    /**
     * Déclenche les actions à réaliser lorsque le bonus est collecté.
     * Lorsqu'il est collecté des scores sont affichés.
     */
    void onCollected();

    /**
     * Largeur du bonus
     * @return largeur du bonus
     */

    int getWidth();

    /**
     * Hauteur du bonus
     * @return hauteur du bonus
     */

    int getHeight();
}
