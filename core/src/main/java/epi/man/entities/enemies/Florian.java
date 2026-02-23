package epi.man.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import epi.man.Board;
import epi.man.entities.Student;

import java.util.ArrayList;

/**
 * Ennemi Florian.
 * Fonctionnement :
 * - Sort de l’enclos en montant tant qu’il marche sur des tiles '-'
 * - Ne peut plus revenir dans l’enclos une fois sorti
 * - Utilise une IA directionnelle basée sur des priorités
 * - Anime ses mouvements selon la direction
 *
 * Florian partage exactement la même architecture que Adrien,
 * mais peut avoir une IA légèrement différente (ordre des priorités).
 */
public class Florian extends Enemies {

    /** Sprites d’animation pour chaque direction. */
    private ArrayList<Texture> framesRight, framesLeft, framesUp, framesDown;

    /** Texture affichée actuellement. */
    private Texture currentTexture;

    /** Gestion de l’animation (frame + timer). */
    private int currentFrameIndex = 0;
    private float animationTimer = 0f;

    /** Référence vers la map. */
    private Board board;

    /** True dès qu’il sort de l’enclos : interdit d’y retourner. */
    private boolean hasLeftPen = false;

    /**
     * Constructeur de Florian.
     *
     * @param x position X initiale
     * @param y position Y initiale
     * @param speed vitesse de déplacement
     * @param board référence vers la map
     * @param student joueur (permet potentiellement IA future)
     */
    public Florian(float x, float y, float speed, Board board, Student student) {
        super(x, y, speed, board);
        this.board = board;

        // Chargement des sprites
        framesRight = new ArrayList<>();
        framesRight.add(new Texture("enemies/RIGHTFLO1.png"));
        framesRight.add(new Texture("enemies/RIGHTFLO2.png"));

        framesLeft = new ArrayList<>();
        framesLeft.add(new Texture("enemies/LEFTFLO1.png"));
        framesLeft.add(new Texture("enemies/LEFTFLO2.png"));

        framesUp = new ArrayList<>();
        framesUp.add(new Texture("enemies/UPFLO1.png"));
        framesUp.add(new Texture("enemies/UPFLO2.png"));

        framesDown = new ArrayList<>();
        framesDown.add(new Texture("enemies/DOWNFLO1.png"));
        framesDown.add(new Texture("enemies/DOWNFLO2.png"));

        currentTexture = framesRight.get(0);
    }

    /**
     * Vérifie si Florian se trouve dans l’enclos '-' (porte).
     */
    private boolean isInPen(float x, float y) {
        int tileX = (int) (x / Board.TILE_SIZE);
        int tileY = board.getMap().length - 1 - (int) (y / Board.TILE_SIZE);
        return board.getMap()[tileY][tileX] == '-';
    }

    /**
     * Logique complète de Florian :
     * 1. S’il n’a pas quitté l’enclos :
     *    - il monte tant qu’il voit la porte '-'
     * 2. Une fois sorti :
     *    - il ne peut plus jamais revenir
     *    - l’IA de déplacement prend le relais
     */
    @Override
    public void update(float delta) {

        float w = currentTexture.getWidth();
        float h = currentTexture.getHeight();

        float cx = x + w / 2f;
        float cy = y + h / 2f;

        // ————— SORTIE DE L'ENCLOS —————
        if (!hasLeftPen) {

            if (isInPen(cx, cy)) {
                float nextY = y + speed * delta;

                if (canMoveTo(x, nextY, w, h)) {
                    y = nextY;

                    float newCY = y + h / 2f;
                    if (!isInPen(cx, newCY)) hasLeftPen = true;
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
     * Déplace Florian vers une direction si possible.
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
     * Florian vérifie les directions dans un ordre défini
     * et choisit la première possible.
     */
    private Direction chooseDirection() {
        Direction[] prio;

        switch (directionActually) {
            case UP:    prio = new Direction[]{Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN}; break;
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
     * Vérifie si Florian peut se déplacer dans une direction donnée.
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
     * Met à jour l'animation en fonction de la direction actuelle.
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
     * Affiche l'ennemi Florian.
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










