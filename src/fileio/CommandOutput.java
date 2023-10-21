package fileio;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)

public class CommandOutput extends OutputEntry {

    private String command;
    private Integer playerIdx;
    private Integer x;
    private Integer y;
    private Integer handIdx;
    private Object output;
    private Coordinates cardAttacker;

    public Coordinates getCardAttacked() {
        return cardAttacked;
    }

    public void setCardAttacked(Coordinates cardAttacked) {
        this.cardAttacked = cardAttacked;
    }

    private Coordinates cardAttacked;
    private String error;

    public Integer getAffectedRow() {
        return affectedRow;
    }

    public void setAffectedRow(Integer affectedRow) {
        this.affectedRow = affectedRow;
    }

    private Integer affectedRow;

    public CommandOutput() {
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Integer getPlayerIdx() {
        return playerIdx;
    }

    public void setPlayerIdx(Integer playerIdx) {
        this.playerIdx = playerIdx;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getHandIdx() {
        return handIdx;
    }

    public void setHandIdx(Integer handIdx) {
        this.handIdx = handIdx;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    public Coordinates getCardAttacker() {
        return cardAttacker;
    }

    public void setCardAttacker(Coordinates cardAttacker) {
        this.cardAttacker = cardAttacker;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
