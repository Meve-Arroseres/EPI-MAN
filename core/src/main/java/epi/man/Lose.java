package epi.man;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;

/**
 * Cette class gère le game over.
 * elle affiche une image game over et un texte indiquant au joueur comment recommencer la partie.
 */

public class Lose {
    /** la police du texte utilisé. */
    private BitmapFont font;

    /** pour indiquer si le game over est actuellement affiché. */
    private boolean active = false;

    /** l'image du game over*/
    private Texture gameOverImage;

    /** rappel le coeur (core) du jeu pour relancer la partie. */
    private Core core;

    /**
     * Ressources nécessaires pour faire l'écran Game over
     * @param core, class principale du jeu, permet toutes les interactions.
     */

    public Lose(Core core) {
        this.core = core;

        font = new BitmapFont();
        font.getData().setScale(2.5f);
        font.setColor(Color.WHITE);


        gameOverImage = new Texture(Gdx.files.internal("map/gameover.png"));

//        game.getAudio().playLoseLife();

    }

    /**
     * Activer l'écran Game Over et on lance la musique.
     * la méthode {@link #render(SpriteBatch)} affiche l'écran.
     */

    public void activate() {
        active = true;
        core.getAudio().playGameOver();  // musique Game Over
    }


    /**
     * On vérifie si l'écran game over est activé.
     * @return true si l'écran est affiché, sinon false
     */

    public boolean isActive() {
        return active;
    }

    /**
     * On affiche l'écran game over si il est actif.
     * On peut appuyer sur ENTER pour recommencer.
     * @param batch SpriteBatch est utilisé pour dessiner les éléments.
     */

    public void render(SpriteBatch batch) {
        if (!active) return;

        /** Nettoyage de l'écran*/
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        /** Affiche l'image en plein écran*/
        batch.draw(gameOverImage,
            0,
            0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        /** Texte pour recommencer*/
        font.draw(batch,
            "Appuie sur ENTER pour recommencer",
            Gdx.graphics.getWidth() / 2f - 300,
            100
        );

        batch.end();

        /** redémarrer le jeu si la touche enter est appuyé*/
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ENTER)) {
            active = false;
        }
    }

    /**
     * Libère les ressources graphiques pour l'écran Game over.
     * fermeture du jeu pour éviter les fuites mémoire.
     */
    public void dispose() {
        font.dispose();
        gameOverImage.dispose();
    }


}

