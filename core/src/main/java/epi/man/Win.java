package epi.man;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Class qui gère la victoire.
 * Elle affiche un message de victoire et le score.
 * L'écran reste actif.
 */
public class Win {

    /** indique si la victoire a été atteinte.*/
    private boolean isWon;

    /**Police du texte.*/
    private BitmapFont font;

    /** permet de calculer la taille et la position du texte. */
    private GlyphLayout layout;

    /** Temps écoulé depuis l'affichage de la victoire*/
    private float timer;

    /** durée d'affichage de l'écran victoire.*/
    private static final float DISPLAY_TIME = 5.0f;

    /**ceur du jeu*/
    private Core game;

    /**
     * les ressources nécessaires à l'affichage de l'écran.
     * @param game class importante du jeu, permettant l'accès à certaines ressources.
     */


    public Win(Core game) {
        this.game = game;
        this.isWon = false;
        this.font = new BitmapFont();
        this.font.getData().setScale(4f);
        this.font.setColor(Color.GOLD);
        this.layout = new GlyphLayout();
        this.timer = 0f;
    }

    /**
     * On vérifie si la condition de victoire est remplie.
     * La musique et l'affichage de la victoire est déclanché si c'est le cas.
     * @param board plateau du jeu pour connaitre les bonus restant.
     * @return true si c'est victoire, sinon false.
     */

    public boolean checkWinCondition(Board board) {
        if (!isWon && board.getRemainingBonuses() == 0) {
            isWon = true;
            timer = 0f;

            //  musique de victoire
            game.getAudio().playWin();
        }
        return isWon;
    }

    /**
     * Affiche la victoire sans score si c'est actif.
     * @param batch sprite batch utilisé pour déssiner les éléments.
     * @param delta temps écoulé.
     */


    public void render(SpriteBatch batch, float delta) {
        if (isWon) {
            timer += delta;

            String message = "VICTOIRE, VOUS AVEZ VOTRE DIPLÔME EPITECH !";
            String subMessage = "Score final : ";

            layout.setText(font, message);
            float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
            float y = (Gdx.graphics.getHeight() + layout.height) / 2f;

            font.draw(batch, message, x, y);
        }
    }

    /**
     * Affiche la victoire avec le score.
     * @param batch sprite batch pour l'affichage
     * @param delta temps écoulé depuis le dernier rendu.
     * @param score score final du joueur.
     */

    public void render(SpriteBatch batch, float delta, int score) {
        if (isWon) {
            timer += delta;

            String message = "VICTOIRE, VOUS AVEZ VOTRE DIPLÔME EPITECH !";

            /** Message de victoire*/
//            String message = "VICTOIRE !";

            layout.setText(font, message);
            float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
            float y = (Gdx.graphics.getHeight() + layout.height) / 2f + 50;
            font.draw(batch, message, x, y);

            /** Score final*/
            String scoreMessage = "Score final : " + score;
            layout.setText(font, scoreMessage);
            float scoreX = (Gdx.graphics.getWidth() - layout.width) / 2f;
            float scoreY = y - 80;
            font.draw(batch, scoreMessage, scoreX, scoreY);
        }
    }

    /**
     * Vérifie si la partie est gagnée.
     * @return true si la victoire est active et false sinon.
     */

    public boolean isWon() {
        return isWon;
    }

    /**
     * Réinitialise la victoire et remet le timer à 0.
     * Utiliser lorsqu'une nouvelle partie commence.
     */
    public void reset() {
        isWon = false;
        timer = 0f;
    }

    /**
     * Retourne le temps écoulé.
     * @return temps en  secondes.
     */

    public float getTimer() {
        return timer;
    }

    /**
     * Libère les ressources graphiques utilisées.
     * A appeler lors de la fermeture du jeu pour éviter les fuites mémoires.
     */

    public void dispose() {
        font.dispose();
    }

}
