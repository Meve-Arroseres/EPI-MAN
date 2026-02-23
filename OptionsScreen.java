package epi.man.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import epi.man.Core;

/**
 * L'écran pour afficher les options du jeu.
 * Permet au joueur de modifier la luminosité.
 * Activer ou désactiver le son.
 * Retourner au menu principal.
 * Implémente l'interface {@link Screen}
 */

public class OptionsScreen implements Screen {
    /** Coeur du jeu.*/
    private Core core;
    /** contient les boutons et gère les interactions.*/
    private Stage stage;

    /**
     * Ecran d'options.
     * Gère les boutons et leur comportement.
     * @param core coeur du jeu.
     */
    public OptionsScreen(Core core) {
        this.core = core;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        /** Police des boutons*/
        BitmapFont font = new BitmapFont();
        font.getData().setScale(2f);

        /** Style des boutons*/
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.WHITE;

        /** Table pour organiser les boutons verticalement.*/
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        /** Création des boutons*/
        TextButton brightness = new TextButton("Luminosité +/-", style);
        TextButton mute = new TextButton("Son On/Off", style);
        TextButton back = new TextButton("Retour", style);

        /** L'action des boutons*/
        brightness.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                core.toggleBrightness();
            }
        });

        mute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                core.toggleSound();
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                core.showMenu();
            }
        });

        /** On ajoute les boutons à la table avec un padding.*/
        table.add(brightness).pad(20);
        table.row();
        table.add(mute).pad(20);
        table.row();
        table.add(back).pad(20);
    }

    /**
     * Retourne le stage de l'écran, afin de l'ajouter au render principal.
     * @return {@link Stage} contient les boutons.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * La méthode appelée lorsque l'écran devient visible.
     */
    @Override
    public void show() {

    }

    /**
     * Met à jour et affiche le stage à chaque frame.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    /**
     * On appelle cette méthode lorsque la taille de l'écran change.
     * @param width nouvelle largeur
     * @param height nouvelle hauteur
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * Appelé quand le jeu est en pause.
     */
    @Override
    public void pause() {

    }

    /**
     * Appelé dès que le jeu reprend.
     */
    @Override
    public void resume() {

    }

    /**
     * Appelé lorsque l'écran est masqué.
     */
    @Override
    public void hide() {

    }

    /**
     * Libère les ressources
     * A appeler lors de la fermeture de l'écran pour éviter les fuites mémoires.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
