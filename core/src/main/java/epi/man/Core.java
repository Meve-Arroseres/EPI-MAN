package epi.man;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import epi.man.entities.Entity;
import epi.man.entities.Student;
import epi.man.entities.enemies.Florian;
import epi.man.entities.enemies.Lyann;
import epi.man.entities.enemies.Guillaume;
import epi.man.entities.enemies.Adrien;
import epi.man.screens.MenuScreen;
import epi.man.screens.OptionsScreen;

/**
 *Classe principale du jeu.
 * Gère la gestion des écrans, du joueur, des ennemis et du score.
 * Implémente ApplicationLister pour faire fonctionner avec LibGDX.
 */

public class Core implements ApplicationListener {

    /**Dessine tous éléments graphiques*/
    private SpriteBatch batch;
    /**Notre joueur*/
    private Student student;
    /** La caméra qui permet de suivre le joueur et la carte*/
    private OrthographicCamera camera;
    /**Adaptation pour différents écrans*/
    private Viewport viewport;

    /**Map du jeu*/
    private Board board;

    /**Ennemis présents sur la map, avec des IA différentes*/
    private Florian florian;
    private Lyann lyann;
    private Guillaume guillaume;
    private Adrien adrien;

    /** Score du joueur*/
    private int score = 0;
    /**Nombre de vies*/
    private int lives = 3;
    /** Texture utilisé pour afficher les coeurs de vie*/
    private Texture heart;

    /** Timer qui empêche le joueur de subir plusieurs dégâts */
    private float hitCooldown = 0f;
    /** Effet visuel quand le joueur est touché*/
    private float damageFlash = 0f;

    /**Police du texte*/
    private BitmapFont font;
    /**  Ecran d'accueil*/
    private MenuScreen menuScreen;
    /** Afin de savoir si on est dans le menu*/
    private boolean inMenu = true;
    /** la luminosité, modifiable dans les options*/
    private float brightness = 1f;
    /**Active ou coupe le son*/
    private boolean soundMuted = false;
    /** options*/
    private OptionsScreen optionsScreen;
    /** Ecran et condition de victoire*/
    private Win win;
    /** Ecran de game over*/
    private Lose loseScreen;
    /** gestion de la musique*/
    private GameMusic audio;

    /**
     * Méthode appelée lorsqu'on lance le jeu.
     * Gère la police, la musique et le menu.
     */
    @Override
    public void create() {
        System.out.println("Core.create()");

        /** Initialise le moteur de rendu*/
        batch = new SpriteBatch();
        /** Police*/
        font = new BitmapFont();
        font.getData().setScale(2f);
        /** module audio*/
        audio = new GameMusic();
        /** menu*/
        menuScreen = new MenuScreen(this);
        inMenu = true;

        /** musique du jeu */
        audio.playMenu();
    }

    /**
     * Démarrer une nouvelle partie
     */
    public void startGame() {

        System.out.println("Starting clean game…");

        /** Musique du game play*/
        audio.playGame();
        /** charge la map et on récupère le joueur*/
        board = new Board("map/map.txt");
        student = board.getStudent();
        /** asset des coeurs de vie*/
        heart = new Texture("map/coeur.png");
        /** caméra centré sur la map*/
        camera = new OrthographicCamera();
        viewport = new FitViewport(1900, 1000, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        camera.position.set(board.getMapWidth() / 2f, board.getMapHeight() / 2f, 0);
        camera.update();

        /**Position des ennemis*/
        float cx = board.getMapWidth() / 2f - Board.TILE_SIZE / 2f;
        float cy = board.getMapHeight() / 2f - Board.TILE_SIZE / 2f;
        /** Gestion écran victoire*/
        win = new Win(this);
        /** Ennemis*/
        florian = new Florian(cx, cy, 150f, board, student);
        lyann = new Lyann(cx + Board.TILE_SIZE, cy, 150f, board, student);
        guillaume = new Guillaume(cx - Board.TILE_SIZE, cy, 150f, board, student);
        adrien = new Adrien(cx, cy - Board.TILE_SIZE, 150f, board, student);
        /** Gestion écran défaite*/
        loseScreen = new Lose(this);
        /**variables du jeu*/
        score = 0;
        lives = 3;
        hitCooldown = 0f;
        damageFlash = 0f;
        /** on sort du menu*/
        inMenu = false;
    }

    /**
     * Méthode pour redimenssionner la taille de l'écran.
     * @param width the new width in pixels
     * @param height the new height in pixels
     */

    @Override
    public void resize(int width, int height) {
        if (viewport != null) {
            viewport.update(width, height, true);
        }
    }

    /**
     * Boucle principale, appelé à chaque frame.
     * Gère l'affichage, les collisions, l'UI, la cam et les écrans.
     */

    @Override
    public void render() {

        /** Si le game over est actif, on affiche celui ci*/
        if (loseScreen != null && loseScreen.isActive()) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            loseScreen.render(batch);
            return;
        }

        float delta = Gdx.graphics.getDeltaTime();

        /** On affiche le menu*/
        if (inMenu) {
            Gdx.gl.glClearColor(0f, 0f, 0.2f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (optionsScreen != null) {
                optionsScreen.render(delta);
            } else if (menuScreen != null) {
                menuScreen.render(delta);
            }
            return;
        }

        /** Réduction du cooldown des dégats*/
        if (hitCooldown > 0) hitCooldown -= delta;

        /** Maj de la map et du joueur*/
        board.update(delta);

        /** Maj des ennemis*/
        florian.update(delta);
        lyann.update(delta);
        guillaume.update(delta);
        adrien.update(delta);

        /**
         * Détection contact entre joueur et ennemis.
         * Si touché perte de vie et retour au début (spawn).
         */
        if (hitCooldown <= 0) {
            boolean touched =
                checkEnemyCollision(florian) ||
                    checkEnemyCollision(lyann) ||
                    checkEnemyCollision(guillaume) ||
                    checkEnemyCollision(adrien);

            if (touched) {
                lives--;
                student.resetToStart();
                /** 1sec d'invincibilité*/
                hitCooldown = 1.0f;
                /** flash visuel*/
                damageFlash = 0.3f;

                /** Si plus de vies on affiche Game over*/
                if (lives <= 0) {
                    loseScreen.activate();
                    audio.playGameOver();  //  musique Game Over
                }
            }
        }
        /** bonus ramassé, le score augmente*/
        if (board.checkBonusCollisionAt(student.getX(), student.getY(), student.getWidth(), student.getHeight())) {
            score += 50;
        }
        /** Vérification des conditions de victoire*/
        win.checkWinCondition(board);

        /** Efface l'écran*/
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        /** maj caméra et rendu de la map*/
        camera.update();
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        board.render(batch);
        /** dessin des ennemis*/
        florian.draw(batch);
        lyann.draw(batch);
        guillaume.draw(batch);
        adrien.draw(batch);
        /** Effet visuel quand le joueur est touché par un ennemis.*/
        if (hitCooldown > 0 && damageFlash > 0) {
            if ((int)(hitCooldown * 10) % 2 == 0) {
                batch.setColor(1, 1, 1, 0.4f);
            }
        }

        student.draw(batch);
        batch.setColor(1, 1, 1, 1);
        batch.end();

        /** rendu coordonnées  écran (UI) */
        OrthographicCamera uiCam = new OrthographicCamera();
        uiCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(uiCam.combined);

        batch.begin();
        /**
         * affichage du score au centre en bas
         */
        GlyphLayout layout = new GlyphLayout(font, "Score : " + score);
        float tx = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float ty = 30 + layout.height;
        font.draw(batch, layout, tx, ty);
        /** Affichage des coeurs de vies*/
        float heartX = tx + layout.width + 10;
        float heartY = ty - layout.height;

        for (int i = 0; i < lives; i++) {
            batch.draw(heart, heartX + i * 45, heartY, 40, 40);
        }
        /** on affiche l'écran de victoire si c'est gagné*/
        if (win.isWon()) win.render(batch, delta, score);
        /** lors d'une rencontre student et ennemis coeur rouge en gros et flash rouge*/
        if (damageFlash > 0) {
            damageFlash -= delta;
            batch.setColor(1, 0, 0, 0.4f);
            batch.draw(heart, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.setColor(1, 1, 1, 1);
        }

        batch.end();
    }

    /**
     * Vérifie si le student touche un objet soit collision
     * @param e student ennemis touché
     * @return true si collision
     */
    private boolean checkEnemyCollision(Entity e) {
        if (e == null || student == null) return false;

        float sx = student.getX(), sy = student.getY();
        float sw = student.getWidth(), sh = student.getHeight();

        float ex = e.getX(), ey = e.getY();
        float ew = e.getWidth(), eh = e.getHeight();

        boolean ox = sx < ex + ew && sx + sw > ex;
        boolean oy = sy < ey + eh && sy + sh > ey;

        return ox && oy;
    }

    /**
     * On redémarre la partie à 0
     */
    public void restartGame() {
        System.out.println("RESTART GAME");
        lives = 3;
        score = 0;
        hitCooldown = 0;
        damageFlash = 0;

        startGame();
    }

    /**
     * Affiche l'écran menu et options
     */
    public void showOptions() {
        optionsScreen = new OptionsScreen(this);
        inMenu = true;
        menuScreen = null;
        audio.playMenu();   //  musique menu
    }

    /**
     * retour au menu principal
     */
    public void showMenu() {
        menuScreen = new MenuScreen(this);
        optionsScreen = null;
        inMenu = true;

        audio.playMenu();  //  musique menu

        Gdx.input.setInputProcessor(menuScreen.getStage());
    }

    /**
     * Augmente la luminosité, retourne au minimum si trop élevée.
     */
    public void toggleBrightness() {
        brightness += 0.1f;
        if (brightness > 1.5f) brightness = 0.5f;
        System.out.println("Luminosité = " + brightness);
    }

    /**
     * Active et désactive le son global du jeu
     */
    public void toggleSound() {
        soundMuted = !soundMuted;
        System.out.println(soundMuted ? "Son coupé" : "Son activé");
    }

    @Override public void pause() {}
    @Override public void resume() {}

    /**
     * Libère les ressources
     * éviter les fuites mémoires.
     */
    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (board != null) board.dispose();
        if (font != null) font.dispose();
        if (heart != null) heart.dispose();
        if (florian != null) florian.dispose();
        if (lyann != null) lyann.dispose();
        if (guillaume != null) guillaume.dispose();
        if (adrien != null) adrien.dispose();
        if (loseScreen != null) loseScreen.dispose();
        if (menuScreen != null) menuScreen.dispose();
        if (win != null) win.dispose();
    }

    public GameMusic getAudio() {
        return audio;
    }
}

