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

public class GameScreen implements Screen {
    private Camera camera;
    private Viewport viewport;
    private final Main game;

    private SpriteBatch batch;
    private Texture[] backgrounds;
    private Texture gameOverTexture;
    private Texture victoryTexture;

    private Texture explosionTexture;

    private float[] backgroundOffsets = {0, 0, 0, 0};
    private float backgroundMaxScrollingSpeed;

    private final int WORLD_WIDTH = 128;
    private final int WORLD_HEIGHT = 72;

    private Warplane playerShip;
    private List<Invahess> enemyList;
    private List<Bullet> playerBulletList;
    private List<Bullet> enemyBulletList;
    private Score score;
    private Hp hp;
    private Lives lives;
    private KeyBinds keyBinds;
    private boolean isPaused;
    private float pauseTimer;
    private LinkedList<Explosion> explosionList;

    private float start1Y = WORLD_HEIGHT * 10 / 12;
    private float start2Y = WORLD_HEIGHT * 9 / 12;
    private float start3Y = WORLD_HEIGHT * 8 / 12;
    private float start4Y = WORLD_HEIGHT * 7 / 12;
    private float start5Y = WORLD_HEIGHT * 6 / 12;
    private float start6Y = WORLD_HEIGHT * 5 / 12;
    private float start7Y = WORLD_HEIGHT * 4 / 12;
    private float start8Y = WORLD_HEIGHT * 3 / 12;
    private float start9Y = WORLD_HEIGHT * 2 / 12;
    private float start10Y = WORLD_HEIGHT * 1 / 12;

    private float start1X = (WORLD_WIDTH * 2) * 10 / 12;
    private float start2X = (WORLD_WIDTH * 2) * 9.5f / 12;
    private float start3X = (WORLD_WIDTH * 2) * 9 / 12;
    private float start4X = (WORLD_WIDTH * 2) * 8.5f / 12;
    private float start5X = (WORLD_WIDTH * 2) * 8 / 12;
    private float start6X = (WORLD_WIDTH * 2) * 7.5f / 12;
    private float start7X = (WORLD_WIDTH * 2) * 7 / 12;
    private float start8X = (WORLD_WIDTH * 2) * 6.5f / 12;
    private float start9X = (WORLD_WIDTH * 2) * 6 / 12;
    private float start10X = (WORLD_WIDTH * 2) * 5.5f / 12;

    private int numOfLevels;
    private Texture invahess00 = new Texture("invahess00.png");
    private Texture invahess01 = new Texture("invahess01.png");

    public GameScreen(Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        isPaused = false;
        keyBinds = new KeyBinds();
        numOfLevels = 1;

        explosionTexture = new Texture("explosion.png");



        gameOverTexture = new Texture("gameover00.png");
        victoryTexture = new Texture("victory00.png") ;
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

        explosionList = new LinkedList<>();

        enemyList = new ArrayList<>();

        playerBulletList = playerShip.getBullets();
        lives = new Lives(playerShip.getNumOfLife(), WORLD_WIDTH - 125, WORLD_HEIGHT - 4);

        firstLevel();

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

        for (Invahess enemy : enemyList) {
            enemy.draw(batch);
            enemy.update(deltaTime);

            enemyBulletList = enemy.getBullets();

            ListIterator<Bullet> enemyBulletIterator = enemyBulletList.listIterator();
            while(enemyBulletIterator.hasNext()) {
                Bullet enemyBullet = enemyBulletIterator.next();
                enemyBullet.draw(batch);
                enemyBullet.update(deltaTime);
                if (enemyBullet.getX() < -10 || enemyBullet.getX() > WORLD_WIDTH + 10 || enemyBullet.getY() < -10 || enemyBullet.getY() > WORLD_HEIGHT + 10) {
                    enemyBulletIterator.remove();
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

        launchLevel(deltaTime);

        if (enemyList.isEmpty() && playerShip.isAlive() && playerShip.getNumOfLife() > 0 && numOfLevels == 6) {
            batch.draw(victoryTexture, 0, 0, 128, 72);

            if (!isPaused) {
                isPaused = true;
            }
            if (isPaused) {
                pauseTimer += deltaTime;

                if (pauseTimer >= 5f) {
                    game.gotoMenuScreen(this);
                    isPaused = false;
                }
            }
        }
        if(playerShip.isAlive() && playerShip.getNumOfLife() > 0) {
            playerShip.draw(batch);
        }else if (!playerShip.isAlive() && playerShip.getNumOfLife() > 0) {
            playerShip.setHp(150);
            playerShip.draw(batch);
            playerShip.setX(WORLD_WIDTH/8);
            playerShip.setY(WORLD_HEIGHT/2);
            playerShip.setAlive(true);
        }
        else {
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

    private void launchLevel(float deltaTime) {
        if (enemyList.isEmpty()) {
            if (!isPaused) {
                isPaused = true;
            }
            if (isPaused) {
                pauseTimer += deltaTime;
                if (pauseTimer >= 1f) {
                    switch (numOfLevels) {
                        case 1:
                            numOfLevels++;
                        case 2:
                            secondLevel();
                            numOfLevels++;
                            break;
                        case 3:
                            thirdLevel();
                            numOfLevels++;
                            break;
                        case 4:
                            fourthLevel();
                            numOfLevels++;
                            break;
                        case 5:
                            bossLevel();
                            numOfLevels++;
                            break;
                    }
                    isPaused = false;
                }
            }
        }
    }

    private void firstLevel() {
        enemyList.add(new Troop(start5X, start5Y, 1f, invahess00, "circular"));
        enemyList.add(new Troop(start4X, start4Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start4X, start6Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start3X, start3Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start3X, start7Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start2X, start2Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start2X, start8Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start1X, start1Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start1X, start9Y, 1f, invahess00,"circular"));
    }

    private void secondLevel() {
        enemyList.add(new Troop(start1X, start1Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start1X, start3Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start1X, start5Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start1X, start7Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start1X, start9Y, 1f, invahess01,"linearUp"));

        enemyList.add(new Troop(start2X, start1Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start2X, start3Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start2X, start5Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start2X, start7Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start2X, start9Y, 1f, invahess01,"linearDown"));


        enemyList.add(new Troop(start3X, start1Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start3X, start3Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start3X, start5Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start3X, start7Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start3X, start9Y, 1f, invahess01,"linearUp"));

        enemyList.add(new Troop(start4X, start1Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start4X, start3Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start4X, start5Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start4X, start7Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start4X, start9Y, 1f, invahess01,"linearDown"));

    }

    private void thirdLevel() {
        enemyList.add(new Troop(start5X, start1Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start4X, start2Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start3X, start3Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start2X, start4Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start1X, start5Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start2X, start6Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start3X, start7Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start4X, start8Y, 1f, invahess00,"circular"));
        enemyList.add(new Troop(start5X, start9Y, 1f, invahess00,"circular"));


    }

    private void fourthLevel() {
        enemyList.add(new Troop(start1X, start1Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start1X, start3Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start1X, start5Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start1X, start7Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start1X, start9Y, 1f, invahess01,"linearDown"));

        enemyList.add(new Troop(start2X, start1Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start2X, start3Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start2X, start5Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start2X, start7Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start2X, start9Y, 1f, invahess01,"linearUp"));


        enemyList.add(new Troop(start3X, start1Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start3X, start3Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start3X, start5Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start3X, start7Y, 1f, invahess01,"linearDown"));
        enemyList.add(new Troop(start3X, start9Y, 1f, invahess01,"linearDown"));

        enemyList.add(new Troop(start4X, start1Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start4X, start3Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start4X, start5Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start4X, start7Y, 1f, invahess01,"linearUp"));
        enemyList.add(new Troop(start4X, start9Y, 1f, invahess01,"linearUp"));
    }

    private void bossLevel() {
        enemyList.add(new Boss(start2X, start5Y, 3f, "boss"));

    }

    private void detectCollisionBetweenPlayerAndEnemy() {
        ListIterator<Invahess> enemyIterator = enemyList.listIterator();
            while (enemyIterator.hasNext()) {
                Invahess enemy = enemyIterator.next();

                Warplane player = playerShip;

                if (player.intersects(enemy.getBoundingBox())) {
                    if (player.isAlive()) {
                        player.receiveDamage(150);
                        explosionList.add(new Explosion(explosionTexture, new Rectangle(player.getBoundingBox()), 0.6f));

                        if(enemy instanceof Troop) {
                            enemyIterator.remove();
                            enemyList.remove(enemy);
                            explosionList.add(new Explosion(explosionTexture, new Rectangle(enemy.getBoundingBox()), 0.6f));
                        }
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
        for (Invahess enemy : enemyList) {
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
            ListIterator<Invahess> enemyIterator = enemyList.listIterator();
            while (enemyIterator.hasNext()) {
                Invahess enemy = enemyIterator.next();
                if (enemy.intersects(bullet.getBoundingBox())) {
                    if (enemy.isAlive()) {
                        enemy.receiveDamage(bullet.getDmg());
                        bulletIterator.remove();
                    }
                    if (!enemy.isAlive()) {

                        explosionList.add(new Explosion(explosionTexture, new Rectangle(enemy.getBoundingBox()), 0.6f));

                        enemyIterator.remove();
                        enemyList.remove(enemy);
                        if(enemy instanceof Boss) {
                            score.updateScore(10000);
                        } else {
                            score.updateScore(1000);
                        }
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
