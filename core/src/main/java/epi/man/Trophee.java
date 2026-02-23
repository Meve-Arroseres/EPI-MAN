package epi.man;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Représente les trophées que l'on peut attraper sur la carte.
 * Quand il est ramassé, il s'efface et ajoute des points au score.
 * avec {@link #onCollected()}
 */

public class Trophee implements Bonus {

    /** Position x et y du trophée*/
    private int x, y;

    /** Savoir si le trophée est déjà ramassé. */
    private boolean collected = false;

    /** représente les trophées.*/
    private Texture texture;

    /**
     * Consctructeur du trophéé.
     * @param x position horizontale
     * @param y position verticale
     * @param texture texture du trophée à afficher
     */
    public Trophee(int x, int y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
    }

    /**
     * retourne à la position horizontale du trophée.
     * @return position X
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * retourne à la position verticale du trophée.
     * @return position Y
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * Largeur de l'objet.
     * @return la largeur du sprite.
     */
    public int getWidth() {
        return 50;
    } // sprite size

    /**
     * hauteur de l'objet
     * @return la hauteur du sprite.
     */
    public int getHeight() {
        return 50;
    }

    /**
     * Indique si le trophée a été attrapé.
     * @return true si déjà collecté sinon false.
     */
    @Override
    public boolean isCollected() {
        return collected;
    }

    /**
     * Déclanche l'action dès que le trophée est ramassé.
     * Il n'est donc plus affiché.
     */
    @Override
    public void onCollected() {
        collected = true;
        System.out.println("Trophée collecté !");
    }

    /**
     * Le trophée est affiché si il n'a pas encore été ramassé.
     * @param batch batch graphique est utilisé pour dessiner l'image.
     */
    @Override
    public void render(SpriteBatch batch) {
        if (!collected) {
            batch.draw(texture, x, y, getWidth(), getHeight());
        }
    }
}



