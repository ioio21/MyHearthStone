package fileio;

public class GameOverOutput extends OutputEntry {

    private String gameEnded;

    public GameOverOutput(String gameEnded) {
        this.gameEnded = gameEnded;
    }

    public String getGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(String gameEnded) {
        this.gameEnded = gameEnded;
    }
}
