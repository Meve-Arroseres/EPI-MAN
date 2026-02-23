package epi.man.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import epi.man.Board;
import epi.man.entities.Student;

import java.util.ArrayList;

/**
 * Ennemi Adrien.
 * Gère :
 * - l’ouverture de l’enclos (pénitentiaire Pac-Man style)
 * - le blocage de la porte après sortie
 * - un déplacement intelligent basé sur priorités
 * - des animations selon direction
 *
 * Fonctionnement :
 * 1) Tant qu’il est dans l’enclos ‘-’, il monte pour sortir.
 * 2) Dès qu’il quitte l’enclos → hasLeftPen = true → il ne peut plus jamais y re-rentrer.
 * 3) Ensuite il suit une IA de déplacement avec priorités (UP/RIGHT/etc.).
 */
public class Adrien extends Enemies {

    /** Animations par direction */
    private ArrayList<Texture> framesRight, framesLeft, framesUp, framesDown;

    /** Texture actuelle affichée */
    private Texture currentTexture;

    /** Indices et timer d’animations */
    private int currentFrameIndex = 0;
    private float animationTimer = 0f;

    /** Référence vers la map */
    private Board board;

    /** True dès qu'il quitte l’enclos une première fois */
    private boolean hasLeftPen = false;

    /**
     * Constructeur d’Adrien.
     *
     * @param x position X initiale
     * @param y position Y initiale
     * @param speed vitesse de déplacement
     * @param board référence Board pour la map
     * @param student référence au joueur (non utilisée mais possible extension IA)
     */
    public Adrien(float x, float y, float speed, Board board, Student student) {
        super(x, y, speed, board);
        this.board = board;

        // Chargement des sprites
        framesRight = new ArrayList<>();
        framesRight.add(new Texture("enemies/adridroite1.png"));
        framesRight.add(new Texture("enemies/adridroite2.png"));

        framesLeft = new ArrayList<>();
        framesLeft.add(new Texture("enemies/adrigauche1.png"));
        framesLeft.add(new Texture("enemies/adrigauche2.png"));

        framesUp = new ArrayList<>();
        framesUp.add(new Texture("enemies/adridos1.png"));
        framesUp.add(new Texture("enemies/adridos2.png"));

        framesDown = new ArrayList<>();
        framesDown.add(new Texture("enemies/adriface1.png"));
        framesDown.add(new Texture("enemies/adriface2.png"));

        currentTexture = framesRight.get(0);
    }

    /**
     * Vérifie si Adrien est dans l’enclos (‘-’).
     */
    private boolean isInPen(float x, float y) {
        int tileX = (int) (x / Board.TILE_SIZE);
        int tileY = board.getMap().length - 1 - (int) (y / Board.TILE_SIZE);
        return board.getMap()[tileY][tileX] == '-';
    }

    /**
     * Logique principale :
     * 1. S’il est encore dans l’enclos → il monte jusqu’à sortir.
     * 2. Une fois sorti → IA normale.
     */
    @Override
    public void update(float delta) {

        float w = currentTexture.getWidth();
        float h = currentTexture.getHeight();

        float cx = x + w / 2f;
        float cy = y + h / 2f;

        // ————— SORTIE DE L'ENCLOS —————
        if (!hasLeftPen) {

            // encore dedans → monter jusqu’à la sortie
            if (isInPen(cx, cy)) {
                float nextY = y + speed * delta;

                if (canMoveTo(x, nextY, w, h)) {
                    y = nextY;

                    // sorti ?
                    float newCY = y + h / 2f;
                    if (!isInPen(cx, newCY)) {
                        hasLeftPen = true;
                    }
                }
                return;
            }
            else {
                hasLeftPen = true;
            }
        }

        // ————— IA NORMALE —————
        Direction dir = chooseDirection();
        attemptMove(dir, delta);

        // ————— ANIMATION —————
        animation(delta);
    }

    /**
     * Déplace l'ennemi vers la direction donnée si possible.
     */
    private boolean attemptMove(Direction d, float delta) {
        float nextX = x;
        float nextY = y;
        float s = speed * delta;

        switch (d) {
            case RIGHT: nextX += s; break;
            case LEFT:  nextX -= s; break;
            case UP:    nextY += s; break;
            case DOWN:  nextY -= s; break;
        }

        float w = currentTexture.getWidth();
        float h = currentTexture.getHeight();

        if (canMoveTo(nextX, nextY, w, h)) {
            x = nextX;
            y = nextY;
            directionActually = d;
            return true;
        }
        return false;
    }

    /**
     * IA simple basée sur priorités :
     * continue d’abord dans sa direction,
     * sinon teste les directions proches,
     * sinon garde la même.
     */
    private Direction chooseDirection() {
        Direction[] prio;

        switch (directionActually) {
            case UP:    prio = new Direction[]{Direction.UP, Direction.RIGHT, Direction.LEFT, Direction.DOWN}; break;
            case DOWN:  prio = new Direction[]{Direction.DOWN, Direction.RIGHT, Direction.LEFT, Direction.UP}; break;
            case RIGHT: prio = new Direction[]{Direction.RIGHT, Direction.UP, Direction.DOWN, Direction.LEFT}; break;
            case LEFT:  prio = new Direction[]{Direction.LEFT, Direction.DOWN, Direction.UP, Direction.RIGHT}; break;
            default:    prio = new Direction[]{Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN};
        }

        for (Direction d : prio) {
            if (canMove(d)) return d;
        }

        return directionActually;
    }

    /**
     * Vérifie si Adrien peut aller dans une direction donnée.
     */
    private boolean canMove(Direction d) {
        float s = speed * Gdx.graphics.getDeltaTime();
        float nextX = x, nextY = y;

        switch (d) {
            case RIGHT: nextX += s; break;
            case LEFT:  nextX -= s; break;
            case UP:    nextY += s; break;
            case DOWN:  nextY -= s; break;
        }

        float w = currentTexture.getWidth();
        float h = currentTexture.getHeight();

        return canMoveTo(nextX, nextY, w, h);
    }

    /**
     * Met à jour l’animation selon la direction actuelle.
     */
    private void animation(float delta) {
        ArrayList<Texture> frames;

        switch (directionActually) {
            case RIGHT: frames = framesRight; break;
            case LEFT:  frames = framesLeft; break;
            case UP:    frames = framesUp; break;
            case DOWN:
            default:    frames = framesDown; break;
        }

        animationTimer += delta;
        if (animationTimer > 0.15f) {
            currentFrameIndex = (currentFrameIndex + 1) % frames.size();
            animationTimer = 0f;
        }

        currentTexture = frames.get(currentFrameIndex);
    }

    /**
     * Affiche l’ennemi.
     */
    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(currentTexture, x, y);
    }

    @Override public float getX() { return x; }
    @Override public float getY() { return y; }
    @Override public float getWidth() { return currentTexture.getWidth(); }
    @Override public float getHeight() { return currentTexture.getHeight(); }

    /**
     * Libère toutes les textures en mémoire.
     */
    public void dispose() {
        framesRight.forEach(Texture::dispose);
        framesLeft.forEach(Texture::dispose);
        framesUp.forEach(Texture::dispose);
        framesDown.forEach(Texture::dispose);
    }
}
