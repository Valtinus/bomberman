package logic;

public class GameSettings {
    public int rounds;
    public int[] playercontrols;
    public String player1Name;
    public String player2Name;
    public int character1ID;
    public int character2ID;
    public int mapID;

    /**
     * Game settings for initializing the game model according to the set values
     *
     * @param rounds            number of rounds
     * @param playercontrols    player controls
     * @param player1Name       name of the first player
     * @param player2Name       name of the second player
     * @param character1ID      ID of the first players character
     * @param character2ID      ID of the second players character
     * @param mapID             ID of the chosen map
     */
    public GameSettings(int rounds, int[] playercontrols, String player1Name, String player2Name, int character1ID, int character2ID, int mapID) {
        this.rounds = rounds;
        this.playercontrols = playercontrols;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.character1ID = character1ID;
        this.character2ID = character2ID;
        this.mapID = mapID;
    }
}
