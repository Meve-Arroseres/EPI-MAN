package epi.man.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import epi.man.Board;
import java.util.ArrayList;

/**
 * Représente le joueur (l'étudiante) dans le jeu.
 * Gère :
 * - le déplacement case par case
 * - les collisions avec la map
 * - l'animation selon la direction
 * - le reset lorsqu'un ennemi touche le joueur
 * - l'état d'invincibilité temporaire après respawn
 */
public class Student extends Entity {

    // Animation
    ArrayList<Texture> framesRight, framesLeft, framesUp, framesDown;
    Texture currentTexture;
    int currentFrameIndex = 0;
    float animationTimer = 0f;
    String direction = "DOWN";

    private Board board;

    // déplacement case par case
    private boolean isMoving = false;
    private float targetX, targetY;

    // spawn / invincibilité
    private float startX, startY;
    private float invincibilityTimer = 0f;
    private final float INVINCIBILITY_DURATION = 0.5f;

    /**
     * Constructeur du joueur.
     * Initialise la position, la vitesse, la map associée et charge toutes les animations.
     */
    public Student(float x, float y, float speed, Board board) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.board = board;

        this.startX = x;
        this.startY = y;

        // SPRITES
        framesRight = new ArrayList<>();
        framesRight.add(new Texture("student/LEFT1.png"));
        framesRight.add(new Texture("student/LEFT2.png"));

        framesLeft = new ArrayList<>();
        framesLeft.add(new Texture("student/RIGHT1.png"));
        framesLeft.add(new Texture("student/RIGHT2.png"));

        framesUp = new ArrayList<>();
        framesUp.add(new Texture("student/UP1.png"));
        framesUp.add(new Texture("student/UP2.png"));

        framesDown = new ArrayList<>();
        framesDown.add(new Texture("student/DOWN1.png"));
        framesDown.add(new Texture("student/DOWN2.png"));

        currentTexture = framesDown.get(0);

        // Première destination = case actuelle
        targetX = x;
        targetY = y;
    }

    /**
     * Replace le joueur à sa position de départ après une collision avec un ennemi.
     * Active une courte invincibilité.
     */
    public void resetToStart() {
        x = startX;
        y = startY;
        targetX = x;
        targetY = y;
        isMoving = false;
        animationTimer = 0;
        currentFrameIndex = 0;
        invincibilityTimer = INVINCIBILITY_DURATION;
    }

    /**
     * Met à jour :
     * - la gestion d'invincibilité
     * - le déplacement case par case
     * - l'animation
     */
    public void update(float delta) {
        if (invincibilityTimer > 0f)
            invincibilityTimer -= delta;

        if (!isMoving)
            handleInput();

        moveTowardTarget(delta);
        updateAnimation(delta);
    }

    /**
     * Analyse les touches directionnelles,
     * vérifie la possibilité du mouvement et prépare la prochaine case.
     */
    private void handleInput() {

        int tile = Board.TILE_SIZE;

        // Recentrage parfait sur la grille
        x = Math.round(x / tile) * tile;
        y = Math.round(y / tile) * tile;

        float tryX = x;
        float tryY = y;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction = "RIGHT";
            tryX = x + tile;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction = "LEFT";
            tryX = x - tile;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction = "UP";
            tryY = y + tile;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction = "DOWN";
            tryY = y - tile;
        }
        else {
            return; // aucune touche
        }

        // Collision mur
        if (board.isWalkableWorld(tryX + 1, tryY + 1)) {
            targetX = tryX;
            targetY = tryY;
            isMoving = true;
        }
    }

    /**
     * Déplace progressivement vers targetX/targetY.
     * Gère l'arrivée sur une case et vérifie les collisions avec trophées.
     */
    private void moveTowardTarget(float delta) {
        if (!isMoving) return;

        float dist = speed * delta;

        // Arrivé sur la case
        if (Math.abs(targetX - x) <= dist &&
            Math.abs(targetY - y) <= dist) {

            x = targetX;
            y = targetY;
            isMoving = false;

            // Collision avec trophée
            board.checkBonusCollisionAt(x, y, getWidth(), getHeight());

            return;
        }

        // Mouvement
        if (x < targetX) x += dist;
        if (x > targetX) x -= dist;
        if (y < targetY) y += dist;
        if (y > targetY) y -= dist;
    }

    /**
     * Gère l'évolution de l'animation selon la direction et le mouvement du joueur.
     */
    private void updateAnimation(float delta) {
        ArrayList<Texture> frames;

        switch (direction) {
            case "RIGHT": frames = framesRight; break;
            case "LEFT": frames = framesLeft; break;
            case "UP": frames = framesUp; break;
            default: frames = framesDown; break;
        }

        if (isMoving) {
            animationTimer += delta;
            if (animationTimer > 0.15f) {
                currentFrameIndex = (currentFrameIndex + 1) % frames.size();
                animationTimer = 0f;
            }
        }
        else {
            currentFrameIndex = 0;
            animationTimer = 0;
        }

        currentTexture = frames.get(currentFrameIndex);
    }

    /**
     * Affiche le joueur avec son sprite actuel.
     */
    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(currentTexture, x, y, 40, 50);
    }

    /** @return position X du joueur */
    @Override
    public float getX() {
        return x;
    }

    /** @return position Y du joueur */
    @Override
    public float getY() {
        return y;
    }

    /** @return largeur du sprite */
    @Override public float getWidth() { return 40; }

    /** @return hauteur du sprite */
    @Override public float getHeight() { return 50; }

    /**
     * @return true si le joueur peut mourir (invincibilité terminée)
     */
    public boolean isVulnerable() {
        return invincibilityTimer <= 0f;
    }

    /** @return texture actuelle utilisée pour le rendu */
    public Texture getTexture() { return currentTexture; }

    /**
     * Libère les textures des animations.
     */
    public void dispose() {
        for (Texture t : framesRight) t.dispose();
        for (Texture t : framesLeft) t.dispose();
        for (Texture t : framesUp) t.dispose();
        for (Texture t : framesDown) t.dispose();
    }
}

