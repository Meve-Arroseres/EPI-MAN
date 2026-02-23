package epi.man.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import epi.man.Board;
import epi.man.entities.Student;

import java.util.ArrayList;

/**
 * Ennemi Lyann.
 * Particularités :
 * - Sort de l’enclos en montant tant qu’il marche sur '-'
 * - Une fois sorti, ne peut plus jamais entrer dans l’enclos
 * - Utilise une IA directionnelle avec priorités différentes des autres ennemis
 * - Anime son sprite selon la direction actuelle
 *
 * Lyann suit la même architecture que Florian et Guillaume,
 * mais possède un ordre de priorité personnalisé dans chooseDirection().
 */
public class Lyann extends Enemies {

    /** Sprites d'animation par direction. */
    private ArrayList<Texture> framesRight, framesLeft, framesUp, framesDown;

    /** Texture affichée actuellement. */
    private Texture currentTexture;

    /** Index de frame + timer d’animation. */
    private int currentFrameIndex = 0;
    private float animationTimer = 0f;

    /** Référence vers la map. */
    private Board board;

    /** True dès que Lyann sort de l’enclos. */
    private boolean hasLeftPen = false;

    /**
     * Constructeur de Lyann.
     *
     * @param x position X de départ
     * @param y position Y de départ
     * @param speed vitesse de déplacement
     * @param board référence à la map
     * @param student joueur (actuellement inutilisé)
     */
    public Lyann(float x, float y, float speed, Board board, Student student) {
        super(x, y, speed, board);
        this.board = board;

        framesRight = new ArrayList<>();
        framesRight.add(new Texture("enemies/lyanndroite1.png"));
        framesRight.add(new Texture("enemies/lyanndroite2.png"));

        framesLeft = new ArrayList<>();
        framesLeft.add(new Texture("enemies/lyanngauche1.png"));
        framesLeft.add(new Texture("enemies/lyanngauche2.png"));

        framesUp = new ArrayList<>();
        framesUp.add(new Texture("enemies/lyanndos1.png"));
        framesUp.add(new Texture("enemies/lyanndos2.png"));

        framesDown = new ArrayList<>();
        framesDown.add(new Texture("enemies/lyannface1.png"));
        framesDown.add(new Texture("enemies/lyannface2.png"));

        currentTexture = framesRight.get(0);
    }

    /**
     * Vérifie si Lyann se trouve encore dans la zone '-'.
     */
    private boolean isInPen(float x, float y) {
        int tileX = (int) (x / Board.TILE_SIZE);
        int tileY = board.getMap().length - 1 - (int) (y / Board.TILE_SIZE);
        return board.getMap()[tileY][tileX] == '-';

    }

    /**
     * Logique complète :
     * 1. Tant qu’il marche sur '-' → il monte pour sortir de l’enclos
     * 2. Une fois sorti → IA directionnelle normale
     * 3. Animation associée à la direction
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
                    if (!isInPen(cx, newCY)) {
                        hasLeftPen = true;
                    }
                }
                return;
            } else {
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
     * Déplace Lyann s'il peut aller dans la direction donnée.
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
     * IA de Lyann : ordre de priorité personnalisé.
     */
    private Direction chooseDirection() {
        Direction[] prio;

        switch (directionActually) {
            case UP:
                prio = new Direction[]{Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN};
                break;
            case RIGHT:
                prio = new Direction[]{Direction.RIGHT, Direction.UP, Direction.DOWN, Direction.LEFT};
                break;
            case LEFT:
                prio = new Direction[]{Direction.LEFT, Direction.UP, Direction.DOWN, Direction.RIGHT};
                break;
            case DOWN:
                prio = new Direction[]{Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP};
                break;
            default:
                prio = new Direction[]{Direction.UP, Direction.RIGHT, Direction.LEFT, Direction.DOWN};
        }

        for (Direction d : prio) {
            if (canMove(d)) return d;
        }

        return directionActually;
    }

    /**
     * Vérifie si Lyann peut aller dans la direction donnée.
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
     * Met à jour l'animation selon direction.
     */
    private void animation(float delta) {
        ArrayList<Texture> frames;

        switch (directionActually) {
            case RIGHT: frames = framesRight; break;
            case UP:    frames = framesUp; break;
            case LEFT:  frames = framesLeft; break;
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
     * Affiche Lyann à l'écran.
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
     * Libère les textures de Lyann.
     */
    public void dispose() {
        framesRight.forEach(Texture::dispose);
        framesLeft.forEach(Texture::dispose);
        framesUp.forEach(Texture::dispose);
        framesDown.forEach(Texture::dispose);
    }
}

