package epi.man;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GameMusic {

    private Music menuMusic;
    private Music gameplayMusic;
    private Music gameOverMusic;
    private Music winMusic;

    private Sound loseLifeSound;

    private Music currentMusic;

    public GameMusic() {
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));
        gameplayMusic = Gdx.audio.newMusic(Gdx.files.internal("music/gameplay.mp3"));
        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("music/lose.mp3"));
        winMusic = Gdx.audio.newMusic(Gdx.files.internal("music/win.mp3"));

        // Charge le son de perte de vie
//        loseLifeSound = Gdx.audio.newSound(Gdx.files.internal("music/lose_life.wav"));
    }

    private void stopCurrent() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    public void playMenu() {
        stopCurrent();
        currentMusic = menuMusic;
        currentMusic.setLooping(true);
        currentMusic.play();
    }

    public void playGame() {
        stopCurrent();
        currentMusic = gameplayMusic;
        currentMusic.setLooping(true);
        currentMusic.play();
    }

    public void playGameOver() {
        stopCurrent();
        currentMusic = gameOverMusic;
        currentMusic.setLooping(false);
        currentMusic.play();
    }

    public void playWin() {
        stopCurrent();
        currentMusic = winMusic;
        currentMusic.setLooping(false);
        currentMusic.play();
    }

    public void playLoseLife() {
        loseLifeSound.play();
    }

    public void dispose() {
        menuMusic.dispose();
        gameplayMusic.dispose();
        gameOverMusic.dispose();
        winMusic.dispose();
        loseLifeSound.dispose();
    }
}

