package io.SpaceInvahess.Game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.*;

public class DemoScreen implements Screen {

    private Camera camera;
    private Viewport viewport;
    private final Main game;

    private SpriteBatch batch;
    private Texture[] backgrounds;
    private Texture gameOverTexture;
    private Texture victoryTexture;
    private Texture explosionTexture;
    private LinkedList<Explosion> explosionList;

    private float[] backgroundOffsets = {0, 0, 0, 0};
    private float backgroundMaxScrollingSpeed;

    private final int WORLD_WIDTH = 128;
    private final int WORLD_HEIGHT = 72;

    private Warplane playerShip;
    private List<Troop> enemyList;
    private List<Bullet> playerBulletList;
    private List<Bullet> enemyBulletList;
    private Score score;
    private Hp hp;
    private Lives lives;
    private KeyBinds keyBinds;
    private boolean isPaused;
    private float pauseTimer;

    private Troop enemy1;
    private Troop enemy2;
    private Troop enemy3;

    private Texture invahess01 = new Texture("invahess01.png");

    public DemoScreen(Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        isPaused = false;
        keyBinds = new KeyBinds();

        gameOverTexture = new Texture("gameover00.png");
        victoryTexture = new Texture("victory00.png");

        backgrounds = new Texture[4];
        backgrounds[0] = new Texture("background_bluesky00.png");
        backgrounds[1] = new Texture("background_bluesky01.png");
        backgrounds[2] = new Texture("background_bluesky02.png");
        backgrounds[3] = new Texture("background_bluesky03.png");

        backgroundMaxScrollingSpeed = (float)(WORLD_WIDTH) / 2;

        score = new Score(WORLD_WIDTH - 30, WORLD_HEIGHT - 5);
        hp = new Hp(WORLD_WIDTH - 125, WORLD_HEIGHT - 5);

        if(keyBinds.isWarthog()){
            playerShip = new Warthog(WORLD_WIDTH/8, WORLD_HEIGHT/2, 50f);
        } else{
            playerShip = new Rafale(WORLD_WIDTH/8, WORLD_HEIGHT/2, 50f);
        }

        float start0X = WORLD_WIDTH * 8/9;
        float start1X = (WORLD_WIDTH * 2) * 10 / 12;

        float start1Y = WORLD_HEIGHT * 10 / 12;
        float start5Y = WORLD_HEIGHT * 6 / 12;
        float start10Y = WORLD_HEIGHT * 1 / 12;

        enemy1 = new Troop(start0X, start1Y, 0f, invahess01,"circular");
        enemy2 = new Troop(start1X, start5Y, 1f, invahess01,"circular");
        enemy3 = new Troop(start1X, start10Y, 1f, invahess01,"circular");

        enemyList = new ArrayList<>();
        enemyList.add(enemy1);
        enemyList.add(enemy2);
        enemyList.add(enemy3);

        playerBulletList = playerShip.getBullets();
        lives = new Lives(playerShip.getNumOfLife(), WORLD_WIDTH - 125, WORLD_HEIGHT - 4);
        explosionTexture = new Texture("explosion.png");
        explosionList = new LinkedList<>();

        batch = new SpriteBatch();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float deltaTime) {
        batch.begin();

        renderBackground(deltaTime);
        hp.draw(batch, playerShip.getHp());
        score.draw(batch);
        lives.draw(batch);
        Timer timer = new Timer();

        playerShip.update(deltaTime);
        for (Troop enemy : enemyList) {
            if(enemy == enemy1) {
                enemy.draw(batch);
            }
            if (enemy == enemy2 && !enemy1.isAlive()) {
                enemy.draw(batch);
                enemy.update(deltaTime);
                enemy.setCanShoot(false);
            }
            if(enemy == enemy3 && !enemy2.isAlive()) {
                enemy.draw(batch);
                enemy.update(deltaTime);
                enemyBulletList = enemy.getBullets();
                ListIterator<Bullet> enemyBulletIterator = enemyBulletList.listIterator();
                while(enemyBulletIterator.hasNext()) {
                    Bullet enemyBullet = enemyBulletIterator.next();
                    enemyBullet.draw(batch);
                    enemyBullet.update(deltaTime);
                    if (enemyBullet.getX() < -10 || enemyBullet.getX() > WORLD_WIDTH + 10) {
                        enemyBulletIterator.remove();
                    }
                }
            }
        }

        ListIterator<Bullet> playerBulletIterator = playerBulletList.listIterator();
        while(playerBulletIterator.hasNext()) {
            Bullet bullet = playerBulletIterator.next();
            bullet.draw(batch);
            bullet.update(deltaTime);
            if (bullet.getX() > WORLD_WIDTH) {
                playerBulletIterator.remove();
            }
        }

        if (enemyList.isEmpty() && playerShip.isAlive() && playerShip.getNumOfLife() > 0) {
            batch.draw(victoryTexture, 0, 0, 128, 72);

            if (!isPaused) {
                isPaused = true;
            }
            if (isPaused) {
                pauseTimer += deltaTime;
                if (pauseTimer >= 3f) {
                    game.gotoMenuScreen(this);
                    isPaused = false;
                }
            }
        }

        if(playerShip.isAlive() && playerShip.getNumOfLife() > 0) {
            playerShip.draw(batch);
        } else if (!playerShip.isAlive() && playerShip.getNumOfLife() > 0) {
            playerShip.setHp(150);
            playerShip.draw(batch);
            playerShip.setX(WORLD_WIDTH/8);
            playerShip.setY(WORLD_HEIGHT/2);
            playerShip.setAlive(true);
        } else {
            batch.draw(gameOverTexture, 0, 0, 128, 72);

            if (!isPaused) {
                isPaused = true;
            }

            if (isPaused) {
                pauseTimer += deltaTime;

                if (pauseTimer >= 3f) {
                    game.gotoMenuScreen(this);
                    isPaused = false;
                }
            }
        }

        detectCollisionsOnPlayer();
        detectCollisionsOnEnemy();
        detectCollisionBetweenPlayerAndEnemy();

        renderExplosions(deltaTime);

        batch.end();
    }

    private void detectCollisionBetweenPlayerAndEnemy() {
        ListIterator<Troop> enemyIterator = enemyList.listIterator();
        while (enemyIterator.hasNext()) {
            Troop enemy = enemyIterator.next();

            Warplane player = playerShip;

            if (player.intersects(enemy.getBoundingBox())) {
                if (player.isAlive()) {
                    player.receiveDamage(150);
                    enemy.receiveDamage(1000000);
                    explosionList.add(new Explosion(explosionTexture, new Rectangle(enemy.getBoundingBox()), 0.6f));
                    explosionList.add(new Explosion(explosionTexture, new Rectangle(player.getBoundingBox()), 0.6f));
                    enemyIterator.remove();
                    enemyList.remove(enemy);
                }

                if (!player.isAlive()) {
                    player.setNumOfLife(player.getNumOfLife() - 1);
                    System.out.println("You died !");
                    lives.setLives(player.getNumOfLife());
                    lives.draw(batch);
                    break;
                }
                break;
            }
        }
    }

    private void detectCollisionsOnPlayer() {
        for (Troop enemy : enemyList) {
            enemyBulletList = enemy.getBullets();
            ListIterator<Bullet> enemyBulletIterator = enemyBulletList.listIterator();
            while (enemyBulletIterator.hasNext()) {
                Bullet enemyBullet = enemyBulletIterator.next();

                Warplane player = playerShip;

                if (player.intersects(enemyBullet.getBoundingBox())) {
                    if (player.isAlive()) {
                        player.receiveDamage(enemyBullet.getDmg());
                        enemyBulletIterator.remove();
                    }

                    if (!player.isAlive()) {
                        player.setNumOfLife(player.getNumOfLife() - 1);
                        System.out.println("You died !");
                        lives.setLives(player.getNumOfLife());
                        lives.draw(batch);
                        break;
                    }
                    break;
                }
            }
        }
    }
    private void detectCollisionsOnEnemy(){
        ListIterator<Bullet> bulletIterator = playerBulletList.listIterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            ListIterator<Troop> enemyIterator = enemyList.listIterator();
            while (enemyIterator.hasNext()) {
                Troop enemy = enemyIterator.next();
                if (enemy.intersects(bullet.getBoundingBox())) {
                    if (enemy.isAlive()) {
                        enemy.receiveDamage(bullet.getDmg());
                        bulletIterator.remove();
                    }
                    if (!enemy.isAlive()) {
                        explosionList.add(new Explosion(explosionTexture, new Rectangle(enemy.getBoundingBox()), 0.6f));
                        enemyIterator.remove();
                        enemyList.remove(enemy);
                        score.updateScore(1000);
                        System.out.println("You killed the enemy!!");
                        break;
                    }
                    break;
                }
            }
        }
    }

    private void renderExplosions(float deltaTime){
        ListIterator<Explosion> explosionListIterator = explosionList.listIterator();
        while (explosionListIterator.hasNext()){
            Explosion explosion = explosionListIterator.next();
            explosion.update(deltaTime);
            if (explosion.isFinished()){
                explosionListIterator.remove();
            }
            else {
                explosion.draw(batch);
            }
        }
    }

    private void renderBackground(float deltaTime){
        backgroundOffsets[0] += deltaTime * backgroundMaxScrollingSpeed / 8;
        backgroundOffsets[1] += deltaTime * backgroundMaxScrollingSpeed / 4;
        backgroundOffsets[2] += deltaTime * backgroundMaxScrollingSpeed / 2;
        backgroundOffsets[3] += deltaTime * backgroundMaxScrollingSpeed;

        for (int layer = 0; layer < backgroundOffsets.length; layer ++){
            if (backgroundOffsets[layer] > WORLD_WIDTH){
                backgroundOffsets[layer] = 0;
            }
            batch.draw(backgrounds[layer], -backgroundOffsets[layer], 0, WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(backgrounds[layer], -backgroundOffsets[layer]+WORLD_WIDTH, 0, WORLD_WIDTH, WORLD_HEIGHT);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();

        for (Texture background : backgrounds) {
            background.dispose();
        }

        gameOverTexture.dispose();

        playerShip.dispose();

        for (Invahess enemy : enemyList) {
            enemy.dispose();
        }
    }
}
