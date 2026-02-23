//package epi.man.screens;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.GlyphLayout;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.utils.viewport.FitViewport;
//import com.badlogic.gdx.utils.viewport.Viewport;
//import com.badlogic.gdx.math.Vector2;
//
//import epi.man.Board;
//import epi.man.Core;
//import epi.man.entities.Student;
//import epi.man.entities.enemies.*;
//
//public class GameScreen implements Screen {
//
//    private final Core game;
//
//    private SpriteBatch batch;
//    private Student student;
//    private OrthographicCamera camera;
//    private Viewport viewport;
//
//    private Florian florian;
//    private Lyann lyann;
//    private Guillaume guillaume;
//    private Adrien adrien;
//
//    private Board board;
//    private int score = 0;
//    private BitmapFont font;
//
//    public GameScreen(Core game) {
//        this.game = game;
//    }
//
//    @Override
//    public void show() {
//        batch = new SpriteBatch();
//        board = new Board("map/map.txt");
//
//        camera = new OrthographicCamera();
//        viewport = new FitViewport(1900, 1000, camera);
//
//        camera.position.set(board.getMapWidth() / 2f, board.getMapHeight() / 2f, 0);
//        camera.update();
//
//        // --- PLAYER ---
//        Vector2 spawn = board.getPlayerSpawn();
//        student = board.getStudent(); // On utilise le student DU BOARD (pas un doublon)
//
//        // --- ENNEMIS ---
//        float cx = board.getMapWidth() / 2f - Board.TILE_SIZE / 2f;
//        float cy = board.getMapHeight() / 2f - Board.TILE_SIZE / 2f;
//
//        florian = new Florian(cx, cy, 80f, board, student);
//        lyann = new Lyann(cx + Board.TILE_SIZE, cy, 80f, board);
//        guillaume = new Guillaume(cx - Board.TILE_SIZE, cy, 80f, board, student);
//        adrien = new Adrien(cx, cy - Board.TILE_SIZE, 80f, board);
//
//        font = new BitmapFont();
//        font.getData().setScale(2f);
//    }
//
//    @Override
//    public void render(float delta) {
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) return;
//
//        // --- UPDATE ---
//        board.update(delta);          // student.update() est déjà dedans
//        florian.update(delta);
//        adrien.update(delta);
//        lyann.update(delta);
//        guillaume.update(delta);
//
//        if (board.checkBonusPickup()) {
//            score += 50;
//        }
//
//        // --- RENDER ---
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        camera.update();
//        viewport.apply();
//        batch.setProjectionMatrix(camera.combined);
//
//        batch.begin();
//
//        board.render(batch);
//        florian.draw(batch);
//        adrien.draw(batch);
//        lyann.draw(batch);
//        guillaume.draw(batch);
//
//        GlyphLayout layout = new GlyphLayout(font, "Score : " + score);
//        float textX = camera.position.x - layout.width / 2f;
//        float textY = camera.position.y - viewport.getWorldHeight() / 2f + 40;
//
//        font.draw(batch, layout, textX, textY);
//
//        batch.end();
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        if (width > 0 && height > 0)
//            viewport.update(width, height, true);
//    }
//
//    @Override public void pause() {}
//    @Override public void resume() {}
//    @Override public void hide() {}
//
//    @Override
//    public void dispose() {
//        batch.dispose();
//        florian.dispose();
//        adrien.dispose();
//        lyann.dispose();
//        guillaume.dispose();
//        board.dispose();
//        font.dispose();
//    }
//}



