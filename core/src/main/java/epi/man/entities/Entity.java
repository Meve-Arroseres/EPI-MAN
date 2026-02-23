package epi.man.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Classe abstraite représentant toute entité du jeu (joueur ou ennemi).
 * Fournit :
 * - une position (x, y)
 * - une vitesse
 * - une texture optionnelle
 *
 * Chaque sous-classe doit implémenter :
 * - update() : comportement et logique par frame
 * - draw() : affichage graphique
 * - getX(), getY() : position actuelle
 * - getWidth(), getHeight() : dimensions de collision
 */
public abstract class Entity {

    /** Position X de l'entité dans le monde. */
    protected float x;

    /** Position Y de l'entité dans le monde. */
    protected float y;

    /** Vitesse de déplacement de l'entité. */
    protected float speed;

    /** Direction de déplacement (optionnel, non utilisé ici directement). */
    protected float dx, dy;

    /** Texture affichée (certaines entités n'en utilisent pas ou l'écrasent). */
    protected Texture texture;

    /** Constructeur vide, utilisé par les sous-classes. */
    public Entity() {}

    /**
     * Logique interne mise à jour à chaque frame.
     * @param delta temps écoulé depuis la dernière frame
     */
    public abstract void update(float delta);

    /**
     * Affiche l'entité à l'écran avec son SpriteBatch.
     */
    public abstract void draw(SpriteBatch batch);

    /**
     * Déplacement basique (non utilisé ici).
     * Laisse la liberté aux sous-classes de l'implémenter ou non.
     */
    public void move() {}

    /** @return position X actuelle */
    public abstract float getX();

    /** @return position Y actuelle */
    public abstract float getY();

    /** @return largeur réelle utilisée pour la collision */
    public abstract float getWidth();

    /** @return hauteur réelle utilisée pour la collision */
    public abstract float getHeight();
}
