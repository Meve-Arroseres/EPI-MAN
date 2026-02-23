package epi.man.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import epi.man.Board;
import epi.man.entities.Student;

import java.util.ArrayList;

/**
 * Ennemi Guillaume.
 * Fonctionnement :
 * - Sort de l’enclos en montant tant qu’il marche sur '-'
 * - Une fois sorti, hasLeftPen devient true et il ne peut plus jamais revenir
 * - Utilise une IA basée sur un ordre de priorités différent (pattern unique)
 * - Anime ses mouvements selon la direction actuelle
 *
 * Chaque ennemi du jeu a un style d’IA différent,
 * mais conserve la même architecture que Florian et Adrien.
 */
public class Guillaume extends Enemies {

    /** Sprites pour chaque direction. */
    private ArrayList<Texture> framesRight, framesLeft, framesUp, framesDown;

    /** Texture affichée. */
    private Texture currentTexture;

    /** Gestion frames et timer. */
    private int currentFrameIndex = 0;
    private float animationTimer = 0f;

    /** Référence vers la map. */
    private Board board;

    /** Interdit le retour dans l’enclos après la sortie. */
    private boolean hasLeftPen = false;

    /**
     * Constructeur de Guillaume.
     *
     * @param x position X initiale
     * @param y position Y initiale
     * @param speed vitesse de déplacement
     * @param board map du jeu
     * @param student joueur (potentiel IA future)
     */
    public Guillaume(float x, float y, float speed, Board board, Student student) {
        super(x, y, speed, board);
        this.board = board;

        // Chargement des sprites
        framesRight = new ArrayList<>();
        framesRight.add(new Texture("enemies/guidroite1.png"));
        framesRight.add(new Texture("enemies/guidroite2.png"));

        framesLeft = new ArrayList<>();
        framesLeft.add(new Texture("enemies/guigauche1.png"));
        framesLeft.add(new Texture("enemies/guigauche2.png"));

        framesUp = new ArrayList<>();
        framesUp.add(new Texture("enemies/guidos1.png"));
        framesUp.add(new Texture("enemies/guidos2.png"));

        framesDown = new ArrayList<>();
        framesDown.add(new Texture("enemies/guiface1.png"));
        framesDown.add(new Texture("enemies/guiface2.png"));

        currentTexture = framesRight.get(0);
    }

    /**
     * Vérifie si Guillaume est encore dans l’enclos '-'.
     */
    private boolean isInPen(float x, float y) {
        int tileX = (int) (x / Board.TILE_SIZE);
        int tileY = board.getMap().length - 1 - (int) (y / Board.TILE_SIZE);
        return board.getMap()[tileY][tileX] == '-';
    }

    /**
     * Logique complète de Guillaume :
     * 1. S’il est dans l’enclos → monte jusqu’à sortir
     * 2. Dès qu’il sort → retour interdit
     * 3. Déplacements normaux basés sur chooseDirection()
     * 4. Mise à jour de l’animation
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
     * Déplace Guillaume dans une direction si possible.
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
     * IA avec ordre de priorités propre à Guillaume.
     */
    private Direction chooseDirection() {
        Direction[] prio;

        switch (directionActually) {
            case UP:
                prio = new Direction[]{Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};
                break;

            case RIGHT:
                prio = new Direction[]{Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP};
                break;

            case DOWN:
                prio = new Direction[]{Direction.DOWN, Direction.RIGHT, Direction.LEFT, Direction.UP};
                break;

            case LEFT:
                prio = new Direction[]{Direction.LEFT, Direction.DOWN, Direction.RIGHT, Direction.UP};
                break;

            default:
                prio = new Direction[]{Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN};
        }

        for (Direction d : prio) {
            if (canMove(d)) return d;
        }

        return directionActually;
    }

    /**
     * Vérifie si Guillaume peut aller dans une direction donnée.
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
     * Met à jour l'animation selon la direction actuelle.
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
     * Affiche Guillaume.
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
     * Libère les textures chargées.
     */
    public void dispose() {
        framesRight.forEach(Texture::dispose);
        framesLeft.forEach(Texture::dispose);
        framesUp.forEach(Texture::dispose);
        framesDown.forEach(Texture::dispose);
    }
}

