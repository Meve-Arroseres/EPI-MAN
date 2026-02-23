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

public class MenuScreen implements Screen {

    private final Core core;

    private Stage stage;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;

    public MenuScreen(Core core) {
        this.core = core;
        System.out.println("MenuScreen CONSTRUCTOR");

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);


        titleFont = new BitmapFont();
        titleFont.setColor(Color.WHITE);
        titleFont.getData().setScale(3f);

        buttonFont = new BitmapFont();
        buttonFont.setColor(Color.WHITE);
        buttonFont.getData().setScale(2f);


        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = buttonFont;
        style.fontColor = Color.WHITE;


        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;

        Label title = new Label("EPI-MAN", titleStyle);

        TextButton start = new TextButton("START", style);
        TextButton quit  = new TextButton("QUIT", style);
        TextButton options = new TextButton("OPTIONS", style);


        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("START CLICKED");
                core.startGame();
            }
        });

        quit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        options.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                core.showOptions();
            }
        });

        table.add(title).padBottom(80);
        table.row();
        table.add(start).pad(10);
        table.row();
        table.add(options).pad(10);
        table.row();
        table.add(quit).pad(10);
    }

    @Override
    public void show() {
        System.out.println("MenuScreen.show()");
        Gdx.input.setInputProcessor(stage);

        // Musique du menu quand on arrive sur l’écran
        core.getAudio().playMenu();
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        titleFont.dispose();
        buttonFont.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}






