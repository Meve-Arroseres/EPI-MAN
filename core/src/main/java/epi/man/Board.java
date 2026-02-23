package epi.man;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import epi.man.entities.Student;

/**
 * représente notre map
 */

public class Board {
    /**Taille d'une tuile de la carte*/
    public static final int TILE_SIZE = 50;
    /**Textures utilisées pour dessiner les assets*/
    private Texture table, chaise, distributeur, Ctable, TropheeTex, background;
    /**Carte chargé depuis un ficheir txt*/
    private char[][] map;
    /** liste des bonus sur la map*/
    private ArrayList<Bonus> bonuses = new ArrayList<>();
    /** nombre de bonus sur la carte*/
    private int totalBonuses = 0;
    /** nombre de bonus déjà collecté*/
    private int collectedBonuses = 0;
    /** Police pour le texte*/
    private BitmapFont font;
    /** notre joueur*/
    private Student student;

    /**
     * constructeur principal, il charge la map
     * @param mapFilename chemin du fichier comprenant la map.
     */
    public Board(String mapFilename) {
        loadTextures();
        map = loadMap(mapFilename);
        countInitialBonuses();

        /**Spawn du joueur sur la case 'P'*/
        Vector2 spawn = getPlayerSpawn();
        student = new Student(spawn.x, spawn.y, 200f, this);

        System.out.println("Map chargée (" + map.length + " lignes)");
        System.out.println("Joueur à la position: (" + spawn.x + ", " + spawn.y + ")");
        System.out.println("Nombre de trophées : " + totalBonuses);
    }

    /**
     * charge les textures utilisées.
     */
    private void loadTextures() {
        background = new Texture("map/parquet.jpg");
        table = new Texture("map/table.png");
        chaise = new Texture("map/chaise.png");
        distributeur = new Texture("map/distributeur.png");
        Ctable = new Texture("map/Ctable.png");
        TropheeTex = new Texture("map/Trophee.png");
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2f);
    }

    /**
     * charge la carte et génère les bonus
     * @param filename chemin vers le fichier map
     * @return les caractères qui représentent la map.
     */
    private char[][] loadMap(String filename) {
        FileHandle file = Gdx.files.internal(filename);
        String[] lines = file.readString().split("\n");

        char[][] mapData = new char[lines.length][];

        for (int y = 0; y < lines.length; y++) {
            mapData[y] = lines[y].toCharArray();
            for (int x = 0; x < lines[y].length(); x++) {
                if (lines[y].charAt(x) == 'b') {
                    float bx = x * TILE_SIZE;
                    float by = (lines.length - 1 - y) * TILE_SIZE;
                    bonuses.add(new Trophee((int) bx, (int) by, TropheeTex));
                }
            }
        }

        return mapData;
    }

    /**
     * Calcul et enregistre le nombre de bonus de base dans la carte
     */
    private void countInitialBonuses() {
        totalBonuses = bonuses.size();
        collectedBonuses = 0;
    }

    /**
     * vérifie si la position est traversable par le joueur.
     * @param worldX position X
     * @param worldY position Y
     * @return true si la tuile est travversable
     */
    public boolean isWalkableWorld(float worldX, float worldY) {
        int tileX = (int) (worldX / TILE_SIZE);
        int tileY = map.length - 1 - (int) (worldY / TILE_SIZE);

        return canMoveTo(tileX, tileY);
    }

    /**
     * Vérifie si la tuile correspond à un élément traversable
     */

    private boolean canMoveTo(int x, int y) {
        if (x < 0 || y < 0 || x >= map[0].length || y >= map.length)
            return false;

        char tile = map[y][x];

        return tile == '0' || tile == 'P' || tile == 'b'|| tile == '-';
    }

    /**
     * Maj l'état du joueur.
     * @param delta temps écoulé depuis la dernière frame
     */
    public void update(float delta) {
        if (student != null) {
            student.update(delta);
        }
    }

    /**
     *Vérifie si on touche un bonus
     * @return true si un bonus a été collecté
     */

    public boolean checkBonusCollisionAt(float x, float y, float w, float h) {


        for (int i = 0; i < bonuses.size(); i++) {
            Bonus b = bonuses.get(i);

            float bx = b.getX();
            float by = b.getY();
            float bw = b.getWidth();
            float bh = b.getHeight();

            boolean overlap =
                x < bx + bw &&
                    x + w > bx &&
                    y < by + bh &&
                    y + bh > by;

            if (overlap) {
                b.onCollected();
                bonuses.remove(i);
                collectedBonuses++;
                return true;
            }
        }
        return false;
    }

    /**
     * Affiche la carte, les bonus et les joueurs
     */
    public void render(SpriteBatch batch) {
        batch.draw(background, 0, 0, getMapWidth(), getMapHeight());

        /**tuiles*/
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char tile = map[y][x];
                int drawX = x * TILE_SIZE;
                int drawY = (map.length - 1 - y) * TILE_SIZE;

                switch (tile) {
                    case 'D':
                        batch.draw(distributeur, drawX, drawY, TILE_SIZE, TILE_SIZE);
                        break;
                    case 'C':
                        batch.draw(chaise, drawX, drawY, TILE_SIZE, TILE_SIZE);
                        break;
                    case 'S':
                        batch.draw(Ctable, drawX, drawY, TILE_SIZE, TILE_SIZE);
                        break;
                    case 'T':
                        batch.draw(table, drawX, drawY, TILE_SIZE, TILE_SIZE);
                        break;
                }
            }
        }

        for (Bonus b : bonuses)
            if (!b.isCollected()) b.render(batch);

        if (student != null) student.draw(batch);
    }

    /**
     * @return nombre de bonus restant
     */
    public int getRemainingBonuses() {
        return bonuses.size();
    }

    /**
     * @return nombre de bonus total au début
     */
    public int getTotalBonuses() {
        return totalBonuses;
    }

    /**
     * @return nombre de bonus déjà collecté
     */
    public int getCollectedBonuses() {
        return collectedBonuses;
    }

    /**
     * @return largeur de la map
     */
    public int getMapWidth() {
        return map[0].length * TILE_SIZE;
    }

    /**
     * @return hauteur de la map
     */
    public int getMapHeight() {
        return map.length * TILE_SIZE;
    }

    public Student getStudent() {
        return student;
    }

    public Vector2 getPlayerSpawn() {
        if (map == null) return new Vector2(200, 100);

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 'P') {
                    float px = x * TILE_SIZE;
                    float py = (map.length - 1 - y) * TILE_SIZE;
                    return new Vector2(px, py);
                }
            }
        }
        return new Vector2(0, 0);
    }

    public char[][] getMap() {
        return map;
    }

    public void dispose() {
    }

    public char getTileAt(float worldX, float worldY) {
        return 0;
    }

}



