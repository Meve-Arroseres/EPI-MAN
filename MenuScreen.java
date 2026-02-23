package epi.man.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import epi.man.Core;

/**
 * Représente l'écran du menu principal de l'application.
 * Affiche le titre et les boutons START, OPTIONS et QUIT.
 * Gère les interactions utilisateur pour naviguer dans l'application.
 *
 */
public class MenuScreen implements Screen {

    /** Référence vers le cœur de l'application pour la navigation entre écrans */
    private final Core core;

    /** Stage contenant tous les éléments UI du menu */
    private Stage stage;

    /** Police utilisée pour le titre du menu */
    private BitmapFont titleFont;

    /** Police utilisée pour les boutons du menu */
    private BitmapFont buttonFont;

    /**
     * Crée un nouveau menu principal.
     * Initialise le stage, les polices et tous les éléments UI (titre et boutons).
     * Configure les listeners pour gérer les clics sur les boutons.
     *
     * @param core Référence vers le cœur de l'application
     */
    public MenuScreen(Core core) {
        this.core = core;
        System.out.println("MenuScreen CONSTRUCTOR");

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Initialisation de la police pour le titre
        titleFont = new BitmapFont();
        titleFont.setColor(Color.WHITE);
        titleFont.getData().setScale(3f);

        // Initialisation de la police pour les boutons
        buttonFont = new BitmapFont();
        buttonFont.setColor(Color.WHITE);
        buttonFont.getData().setScale(2f);

        // Style des boutons
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = buttonFont;
        style.fontColor = Color.WHITE;

        // Table pour organiser les éléments UI
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Style et création du titre
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;

        Label title = new Label("EPI-MAN", titleStyle);

        // Création des boutons
        TextButton start = new TextButton("START", style);
        TextButton quit  = new TextButton("QUIT", style);
        TextButton options = new TextButton("OPTIONS", style);

        // Listener pour le bouton START : lance le jeu
        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("START CLICKED");
                core.startGame();
            }
        });

        // Listener pour le bouton QUIT : ferme l'application
        quit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Listener pour le bouton OPTIONS : affiche l'écran des options
        options.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                core.showOptions();
            }
        });

        // Ajout des éléments dans la table avec espacement
        table.add(title).padBottom(80);
        table.row();
        table.add(start).pad(10);
        table.row();
        table.add(options).pad(10);
        table.row();
        table.add(quit).pad(10);
    }

    /**
     * Appelée quand cet écran devient l'écran actif.
     * Redonne le focus au stage du menu pour capturer les entrées utilisateur.
     * Lance la musique du menu.
     */
    @Override
    public void show() {
        System.out.println("MenuScreen.show()");
        Gdx.input.setInputProcessor(stage);

        // Musique du menu quand on arrive sur l’écran
        core.getAudio().playMenu();
    }

    /**
     * Appelée à chaque frame pour mettre à jour et afficher le menu.
     * Met à jour le stage et le dessine à l'écran.
     *
     * @param delta Temps écoulé depuis la dernière frame en secondes
     */
    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }


    /**
     * Appelée quand la fenêtre est redimensionnée.
     * Met à jour le viewport du stage pour s'adapter aux nouvelles dimensions.
     *
     * @param width Nouvelle largeur de la fenêtre
     * @param height Nouvelle hauteur de la fenêtre
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Appelée quand l'application est mise en pause.
     * Non utilisée dans cette implémentation.
     */
    @Override public void pause() {}

    /**
     * Appelée quand l'application reprend après une pause.
     * Non utilisée dans cette implémentation.
     */
    @Override public void resume() {}

    /**
     * Appelée quand cet écran n'est plus l'écran actif.
     * Non utilisée dans cette implémentation.
     */
    @Override public void hide() {}

    /**
     * Libère les ressources utilisées par le menu (stage et polices).
     * Doit être appelée quand le menu n'est plus nécessaire.
     */
    @Override
    public void dispose() {
        stage.dispose();
        titleFont.dispose();
        buttonFont.dispose();
    }

    /**
     * Retourne le stage contenant les éléments UI du menu.
     *
     * @return Stage du menu
     */
    public Stage getStage() {
        return stage;
    }
}






