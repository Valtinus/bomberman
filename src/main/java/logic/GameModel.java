package logic;

import entities.active.*;
import entities.passive.*;
import view.GameFrame;
import view.GameView;
import view.MenuFrame;
import view.WinnerPopUpWindows;

import java.util.ArrayList;

public class GameModel{
    public GameView gameView;
    public Player testPlayer;
    public Player testPlayer2;
    public Monster baseMonster;
    public Monster ghostMonster;
    public Monster smartMonster;
    public Monster junctionMonster;
    public KeyHandler keyHandler;
    public KeyHandler keyHandler2;
    public int rounds;

    public ArrayList<Sprite> sprites = new ArrayList<>();
    public ArrayList<Entity> entities;
    public String player1Name;
    public String player2Name;
    public int character1ID;
    public int character2ID;
    public int mapID;
    public GameSettings settings;

    /**
     * Initialize the game model with the provided settings and view
     *
     * @param gameView  gameview class - manages view and appearance of the game
     * @param settings  game settings like players names, controls, map, etc
     */
    public GameModel(GameView gameView, GameSettings settings) {
        this.gameView = gameView;
        this.settings = settings;
        this.rounds = settings.rounds;
        this.player1Name = settings.player1Name;
        this.player2Name = settings.player2Name;
        this.character1ID = settings.character1ID;
        this.character2ID = settings.character2ID;
        this.mapID = settings.mapID;
        keyHandler = new KeyHandler(settings.playercontrols);
        keyHandler2 = new KeyHandler(settings.playercontrols);

        testPlayer = new Player(gameView,character1ID,player1Name);
        testPlayer2 = new Player(gameView,character2ID,player2Name);

        baseMonster = new BaseMonster(gameView);
        ghostMonster = new GhostMonster(gameView);
        smartMonster = new SmartMonster(gameView);
        junctionMonster = new JunctionMonster(gameView);

        sprites.add(testPlayer);
        sprites.add(testPlayer2);

        sprites.add(baseMonster);
        sprites.add(ghostMonster);
        sprites.add(smartMonster);
        sprites.add(junctionMonster);

        testPlayer.position.setPosX(gameView.tileSize);
        testPlayer.position.setPosY(gameView.tileSize);

        testPlayer2.position.setPosX(gameView.maxScreenRow * gameView.tileSize - gameView.tileSize * 2);
        testPlayer2.position.setPosY(gameView.maxScreenCol * gameView.tileSize - gameView.tileSize * 2);

        entities = gameView.tileManager.loadBoxes();
    }

    /**
     * Reset the game to start a new round
     */
    public void reset(){
        testPlayer.reset();
        testPlayer2.reset();

        baseMonster = new BaseMonster(gameView);
        ghostMonster = new GhostMonster(gameView);
        smartMonster = new SmartMonster(gameView);
        junctionMonster = new JunctionMonster(gameView);

        sprites.clear();
        entities.clear();

        sprites.add(testPlayer);
        sprites.add(testPlayer2);

        sprites.add(baseMonster);
        sprites.add(ghostMonster);
        sprites.add(smartMonster);
        sprites.add(junctionMonster);

        testPlayer.position.setPosX(gameView.tileSize);
        testPlayer.position.setPosY(gameView.tileSize);

        testPlayer2.position.setPosX(gameView.maxScreenRow * gameView.tileSize - gameView.tileSize * 2);
        testPlayer2.position.setPosY(gameView.maxScreenCol * gameView.tileSize - gameView.tileSize * 2);

        entities = gameView.tileManager.loadBoxes();
    }

    /**
     * Update the game state including player movements, monster movements, and bomb placements, etc
     */
    private String winnerMsg = "";
    private Player winner = null;
    private String winnerName = "";
    private boolean isDraw = false;
    private boolean isRoundOver = false;
    public void update() {
        checkEndOfRound();
        if(isRoundOver){
            handleEndMessage(winnerMsg, winner, winnerName, isDraw);
            winnerMsg = "";
            winner = null;
            winnerName = "";
            isDraw = false;
            isRoundOver = false;
        }

        removeDeadSprites();
        moveMonsters();
        updateExplosions();

        movePlayers();

        placeBombsInstantly();
        placeBombs();
        placeWalls();
        detonateBombs();
    }

    /**
     * Check if the current round has ended and updates game state accordingly
     */

    public void checkEndOfRound() {

        boolean isBombPresent = false;
        boolean isExplosionPresent = false;
        isRoundOver = false;

        for(Entity entity : entities){
            if(entity instanceof Bomb){
                isBombPresent = true;
            }
            if(entity instanceof Explosion) {
                isExplosionPresent = true;
            }
        }

        if(rounds > 0) {
            if (!testPlayer.isAlive && !testPlayer2.isAlive && !isBombPresent && !isExplosionPresent) {
                // If both players are dead and no bombs or explosions are present, the round is a draw
                System.out.println("round over, draw");
                if(rounds - 1 != 0) {
                    gameView.stop();
                    winnerMsg = "round";
                    winner = null;
                    winnerName = null;
                    isDraw = true;
                }

                isRoundOver = true;
            }

            if ((!testPlayer.isAlive || !testPlayer2.isAlive) && !isBombPresent && !isExplosionPresent) {
                if (testPlayer.isAlive) {
                    // If player 1 is alive and the other is not, player 1 wins
                    System.out.println("round over, player 1 wins");
                    testPlayer.score++;
                    if(rounds - 1 != 0){
                        gameView.stop();
                        winnerMsg = "round";
                        winner = testPlayer;
                        winnerName = player1Name;
                        isDraw = false;
                    }

                    isRoundOver = true;
                } else {
                    // If player 2 is alive and the other is not, player 2 wins
                    System.out.println("round over, player 2 wins");
                    testPlayer2.score++;
                    if(rounds - 1 != 0){
                        gameView.stop();
                        winnerMsg = "round";
                        winner = testPlayer2;
                        winnerName = player2Name;
                        isDraw = false;
                    }

                    isRoundOver = true;
                }
            }
        }

        if (isRoundOver) {
            rounds--;
        }

        if(rounds == 0){
            gameEnd();
        }
    }

    /**
     * Handle the end of the game by determining the winner
     */
    public void gameEnd(){
        if(testPlayer.score > testPlayer2.score){
            gameView.stop();
            handleEndMessage("game", testPlayer, player1Name, false);
        }
        else if(testPlayer.score < testPlayer2.score){
            gameView.stop();
            handleEndMessage("game", testPlayer2, player2Name, false);
        }
        else{
            gameView.stop();
            handleEndMessage("game", null, null, true);
        }
    }

    public void handleEndMessage(String msg, Player player, String playerName, boolean isDraw){
        WinnerPopUpWindows winnerPopUpWindows = new WinnerPopUpWindows(msg, player, playerName, isDraw, this, player2Name, player1Name, testPlayer.score, testPlayer2.score);
    }

    /**
     * Reset the game state for the next round
     */
    public void nextRound(){
        gameView.reset();
        reset();
    }

    /**
     * Start a new game by returning to the main menu
     */
    public void nextGame(){
        gameView.stop();
        GameFrame.disposeFrame();
        MenuFrame menuFrame = new MenuFrame();
        menuFrame.setVisible(true);
    }

    /**
     * Update the state of explosions, removing expired ones
     */
    public void updateExplosions() {
        ArrayList<Entity> entitiesCopy = new ArrayList<>(entities);
        ArrayList<Entity> removableEntities = new ArrayList<>();
        for (Entity entity : entitiesCopy) {
            if (entity instanceof Explosion) {
                ((Explosion) entity).update();
                if (((Explosion) entity).isRemovable()) {
                    removableEntities.add(entity);
                }
            }
        }
        entities.removeAll(removableEntities);
    }

    /**
     * Move all monsters according to their behavior
     */
    public void moveMonsters() {
        for (Sprite monster : sprites) {
            if (monster instanceof Monster) {
                ((Monster) monster).moveMonster();
            }
        }
    }

    /**
     * Remove sprites that are no longer alive from the game
     */
    public void removeDeadSprites() {
        ArrayList<Sprite> diedSprites = new ArrayList<>();
        for (Sprite sprite : sprites) {
            if (!sprite.isAlive) {
                diedSprites.add(sprite);
            }
        }
        sprites.removeAll(diedSprites);
    }

    /**
     * Move players based on their input controls
     */
    public void movePlayers() {
        if (keyHandler.up1Pressed || keyHandler.down1Pressed || keyHandler.left1Pressed || keyHandler.right1Pressed) {
            testPlayer.move(keyHandler.direction1);
        }
        if (keyHandler.up2Pressed || keyHandler.down2Pressed || keyHandler.left2Pressed || keyHandler.right2Pressed) {
            testPlayer2.move(keyHandler.direction2);
        }
    }

    /**
     * Instantly place bombs for players who have the instant bomb powerup
     */
    public void placeBombsInstantly() {
        placeInstantBombForPlayer(testPlayer);
        placeInstantBombForPlayer(testPlayer2);
    }

    /**
     * Place a bomb instantly for the given player
     *
     * @param player    the player who places the bomb
     */
    private void placeInstantBombForPlayer(Player player) {
        if (player.placeBombInstantlyActive) {
            Bomb bomb = player.placeBomb();
            if (bomb != null) {
                entities.add(bomb);
                startBombTimer(player, bomb);
            }
        }
    }

    /**
     * Place bombs for players based on their input controls
     */
    public void placeBombs() {
        placeBombForPlayer(testPlayer, keyHandler.setBombPressed);
        placeBombForPlayer(testPlayer2, keyHandler.setBombPressed2);
    }


    /**
     * Place a bomb for the given player
     *
     * @param player            the player who places the bomb
     * @param setBombPressed    is the key pressed for setting a bomb
     */
    public void placeBombForPlayer(Player player, boolean setBombPressed) {
        if (setBombPressed && !player.detonatorActive && player.lastDetonated + 200 <= System.currentTimeMillis()) {
            Bomb bomb = player.placeBomb();
            if (bomb != null) {
                entities.add(bomb);
                startBombTimer(player, bomb);
            }
        } else if (setBombPressed && player.lastDetonated + 200 <= System.currentTimeMillis() && player.availableBombs > player.placedBombs.size()) {
            Bomb bomb = player.placeBomb();
            if (bomb != null) {
                entities.add(bomb);
            }
        }
    }

    /**
     * Detonate bombs for the players
     */
    public void detonateBombs() {
        detonateBombsForPlayer(testPlayer, keyHandler.setBombPressed);
        detonateBombsForPlayer(testPlayer2, keyHandler.setBombPressed2);
    }

    /**
     * Detonate all the player's bombs if all the conditions apply
     *
     * @param player            the player who detonates the bomb(s)
     * @param setBombPressed    is the key pressed for detonating a bomb(s)
     */
    public void detonateBombsForPlayer(Player player, boolean setBombPressed) {
        if (setBombPressed
                && player.detonatorActive
                && !player.placedBombs.isEmpty()
                && player.availableBombs == player.placedBombs.size()
                && !player.disabledBombActive
                && player.isDelayOver(System.currentTimeMillis())) {

            ArrayList<Bomb> copiedBombs = new ArrayList<>(player.placedBombs);

            for (Bomb bomb : copiedBombs) {
                bomb.explode();
                player.placedBombs.remove(bomb);
                entities.remove(bomb);
            }

            player.lastDetonated = System.currentTimeMillis();
            player.detonatorActive = false;
            player.removeActivePowerUp(Detonator.class);
        }
    }

    /**
     * Start the timer for the placed bomb
     *
     * @param player    the player who placed down the bomb
     * @param bomb      the bomb
     */
    private void startBombTimer(Player player, Bomb bomb) {
        BombTimer bombTimer = new BombTimer(this, player, bomb);
        bombTimer.start();
    }

    /**
     * Place walls for players based on their input controls
     */
    public void placeWalls() {
        placeWallForPlayer(testPlayer, keyHandler.setBarrierPressed);
        placeWallForPlayer(testPlayer2, keyHandler.setBarrierPressed2);
    }

    /**
     * Place a wall for the given player
     *
     * @param player            the player who places a wall
     * @param setBarrierPressed is the key pressed for placing a wall
     */
    public void placeWallForPlayer(Player player, boolean setBarrierPressed) {
        if (setBarrierPressed) {
            Wall wall = player.placeWall();
            if (wall != null) {
                entities.add(wall);
            }
        }
    }
}
