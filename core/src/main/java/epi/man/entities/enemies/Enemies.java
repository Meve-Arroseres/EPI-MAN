package epi.man.entities.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import epi.man.Board;
import epi.man.entities.Entity;

import java.util.Random;

/**
 * Classe abstraite commune à tous les ennemis du jeu (Florian, Lyann, etc.).
 * Fournit :
 * - la gestion de direction
 * - la détection de collisions avec la map
 * - le blocage de la porte après sortie de l'enclos
 * - un système de direction aléatoire
 *
 * Chaque ennemi hérite d'Enemies et implémente :
 * - update() : logique et IA
 * - draw() : affichage
 */
public abstract class Enemies extends Entity {

    /** Générateur aléatoire pour les changements de direction. */
    protected Random random;

    /** Direction actuelle de l'ennemi. */
    protected Direction directionActually;

    /** Référence vers la map (Board). */
    protected Board board;

    /** Indique si l'ennemi a quitté son enclos une première fois. */
    protected boolean hasLeftPen = false;

    /**
     * Directions possibles pour les déplacements.
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * Constructeur commun à tous les ennemis.
     *
     * @param x position X de départ
     * @param y position Y de départ
     * @param speed vitesse de déplacement
     * @param board référence à la map pour gérer les collisions
     */
    public Enemies(float x, float y, float speed, Board board) {
        super();
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.board = board;

        this.random = new Random();
        this.directionActually = Direction.UP;
    }

    /**
     * Vérifie si l’ennemi peut se déplacer vers une zone donnée.
     * Gère :
     * - les collisions avec les murs
     * - l'interdiction d'utiliser la porte '-' après sortie de l'enclos
     *
     * @param nextX position X envisagée
     * @param nextY position Y envisagée
     * @param w largeur du sprite ennemi
     * @param h hauteur du sprite ennemi
     * @return true si la zone est libre et autorisée
     */
    protected boolean canMoveTo(float nextX, float nextY, float w, float h) {

        float cx = nextX + w / 2f;
        float cy = nextY + h / 2f;

        int tileX = (int)(cx / Board.TILE_SIZE);
        int tileY = board.getMap().length - 1 - (int)(cy / Board.TILE_SIZE);

        char tile = board.getMap()[tileY][tileX];

        // Blocage définitif de la porte '-' après sortie
        if (hasLeftPen && tile == '-') {
            return false;
        }

        float margin = 3f;

        // Vérification des 4 coins du sprite
        return board.isWalkableWorld(nextX + margin,        nextY + margin) &&
            board.isWalkableWorld(nextX + w - margin,    nextY + margin) &&
            board.isWalkableWorld(nextX + margin,        nextY + h - margin) &&
            board.isWalkableWorld(nextX + w - margin,    nextY + h - margin);
    }

    /**
     * Change aléatoirement la direction actuelle de l'ennemi.
     */
    protected void changeRandomDirection() {
        Direction[] dirs = Direction.values();
        directionActually = dirs[random.nextInt(dirs.length)];
    }

    /**
     * Met à jour l'ennemi :
     * déplacement, IA, orientation, gestion d’enclos...
     * Implémenté dans chaque ennemi concret.
     */
    public abstract void update(float delta);

    /**
     * Affiche l'ennemi.
     * Implémenté dans chaque ennemi concret.
     */
    public abstract void draw(SpriteBatch batch);
}





