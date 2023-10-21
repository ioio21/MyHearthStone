package main;

import checker.Checker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;
import game.Card;
import game.Game;
import game.Player;
import game.Statistics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {

    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(
                        Arrays.stream(directory.listFiles())
                                .sorted(Comparator.comparing(File::getName)))
                /*.skip(11)
                .limit(1)*/
                .collect(Collectors.toList())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        //TODO add here the entry point to your implementation

        //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        Statistics statistics = new Statistics();

        for (GameInput gameInput : inputData.getGames()) {
            // prepareGame
            Game game = new Game(statistics);
            ArrayList<CardInput> playerOneDeckRaw = inputData.getPlayerOneDecks().getDecks().get(gameInput.getStartGame().getPlayerOneDeckIdx());
            CardInput playerOneHero = gameInput.getStartGame().getPlayerOneHero();

            Player playerOne = new Player();
            playerOne.preparePlayer(playerOneDeckRaw, playerOneHero, gameInput.getStartGame().getShuffleSeed());

            Player playerTwo = new Player();
            ArrayList<CardInput> playerTwoDeckRaw = inputData.getPlayerTwoDecks().getDecks().get(gameInput.getStartGame().getPlayerTwoDeckIdx());
            CardInput playerTwoHero = gameInput.getStartGame().getPlayerTwoHero();
            playerTwo.preparePlayer(playerTwoDeckRaw, playerTwoHero, gameInput.getStartGame().getShuffleSeed());

            game.setPlayerOne(playerOne);
            game.setPlayerTwo(playerTwo);

            int startingPlayer = gameInput.getStartGame().getStartingPlayer();
            game.setPlayerTurn(startingPlayer);

            // first round
            playerOne.startRound(game);
            playerTwo.startRound(game);
            CommandOutput commandOutput;
            ObjectNode objectNode;
            boolean gameOver = false;

            for (ActionsInput actionsInput : gameInput.getActions()) {

                if (skipAction(gameOver, actionsInput)) {
                    continue;
                }

                switch (actionsInput.getCommand()) {
                    case "endPlayerTurn":
                         game.unfroze();
                        if (startingPlayer == game.getPlayerTurn()) {
                            // second player plays
                            game.switchPlayerTurn();

                            // unfroze table cards

                        } else {

                            game.switchPlayerTurn();
                            // start new round
                            game.startNewRound();
                            playerOne.startRound(game);
                            playerTwo.startRound(game);

                            // unfroze table cards
                        }

                        break;
                    case "placeCard":

                        placeCard(objectMapper, output, game, actionsInput);

                        break;

                    case "getPlayerDeck":
                        getPlayerDeck(objectMapper, output, game, actionsInput);
                        break;

                    case "getPlayerHero":
                        getPlayerHero(objectMapper, output, game, actionsInput);
                        break;
                    case "getPlayerTurn":
                        commandOutput = new CommandOutput();
                        commandOutput.setCommand(actionsInput.getCommand());
                        commandOutput.setOutput(game.getPlayerTurn());
                        objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                        output.addPOJO(objectNode);
                        break;
                    case "getCardsInHand":
                        commandOutput = new CommandOutput();
                        commandOutput.setCommand(actionsInput.getCommand());
                        commandOutput.setPlayerIdx(actionsInput.getPlayerIdx());
                        commandOutput.setOutput(game.getCardsInHand(actionsInput.getPlayerIdx()));
                        objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                        output.addPOJO(objectNode);
                        break;
                    case "getCardsOnTable":
                        commandOutput = new CommandOutput();
                        commandOutput.setCommand(actionsInput.getCommand());
                        commandOutput.setOutput(game.getCardsOnTable());
                        objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                        output.addPOJO(objectNode);
                        break;

                    case "getPlayerMana":
                        commandOutput = new CommandOutput();
                        commandOutput.setCommand(actionsInput.getCommand());
                        commandOutput.setPlayerIdx(actionsInput.getPlayerIdx());
                        commandOutput.setOutput(game.getPlayerMana(actionsInput.getPlayerIdx()));
                        objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                        output.addPOJO(objectNode);
                        break;
                    case "useEnvironmentCard":
                       String err = game.useEnvironmentCard(actionsInput.getHandIdx(), actionsInput.getAffectedRow());
                        if (err != null) {
                            commandOutput = new CommandOutput();
                            commandOutput.setCommand(actionsInput.getCommand());
                            commandOutput.setHandIdx(actionsInput.getHandIdx());
                            commandOutput.setAffectedRow(actionsInput.getAffectedRow());
                            commandOutput.setError(err);
                            objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                            output.addPOJO(objectNode);
                        } else {

                        }
                        break;
                    case "getEnvironmentCardsInHand":
                        commandOutput = new CommandOutput();
                        commandOutput.setCommand(actionsInput.getCommand());
                        commandOutput.setPlayerIdx(actionsInput.getPlayerIdx());
                        commandOutput.setOutput(game.getEnvironmentFromHand(actionsInput.getPlayerIdx()));
                        objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                        output.addPOJO(objectNode);
                        break;
                    case "getCardAtPosition":
                        int x = actionsInput.getX();
                        int y = actionsInput.getY();
                        if (x >= 4 || x < 0 || y >= game.getTable().get(x).size() || y < 0) {
                            commandOutput = new CommandOutput();
                            commandOutput.setCommand(actionsInput.getCommand());
                            commandOutput.setX(actionsInput.getX());
                            commandOutput.setY(actionsInput.getY());
                            commandOutput.setOutput("No card available at that position.");
                            objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                            output.addPOJO(objectNode);
                        } else {
                            commandOutput = new CommandOutput();
                            commandOutput.setCommand(actionsInput.getCommand());
                            commandOutput.setX(actionsInput.getX());
                            commandOutput.setY(actionsInput.getY());
                            commandOutput.setOutput(game.getTable().get(x).get(y).copyCard());
                            objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                            output.addPOJO(objectNode);
                        }
                        break;
                    case "getFrozenCardsOnTable":
                        commandOutput = new CommandOutput();
                        commandOutput.setCommand(actionsInput.getCommand());
                        commandOutput.setOutput(game.frozenCards());
                        objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                        output.addPOJO(objectNode);
                        break;
                    case "cardUsesAttack":
                        String result = game.cardUsesAttack(actionsInput.getCardAttacker().getX(), actionsInput.getCardAttacker().getY(), actionsInput.getCardAttacked().getX(), actionsInput.getCardAttacked().getY());
                        if (result != null) {
                            commandOutput = new CommandOutput();
                            commandOutput.setCommand(actionsInput.getCommand());
                            commandOutput.setCardAttacker(actionsInput.getCardAttacker());
                            commandOutput.setCardAttacked(actionsInput.getCardAttacked());
                            commandOutput.setError(result);
                            objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                            output.addPOJO(objectNode);
                        } else {

                        }
                        break;
                    case "cardUsesAbility":
                        String result2 = game.cardUsesAbility(actionsInput.getCardAttacker().getX(), actionsInput.getCardAttacker().getY(), actionsInput.getCardAttacked().getX(), actionsInput.getCardAttacked().getY());
                        if (result2 != null) {
                            commandOutput = new CommandOutput();
                            commandOutput.setCommand(actionsInput.getCommand());
                            commandOutput.setCardAttacker(actionsInput.getCardAttacker());
                            commandOutput.setCardAttacked(actionsInput.getCardAttacked());
                            commandOutput.setError(result2);
                            objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                            output.addPOJO(objectNode);
                        } else {

                        }
                        break;
                    case "useAttackHero":
                        String result3 = game.cardAttackHero(actionsInput.getCardAttacker().getX(), actionsInput.getCardAttacker().getY());
                        if (result3 != null) {
                            commandOutput = new CommandOutput();
                            commandOutput.setCommand(actionsInput.getCommand());
                            commandOutput.setCardAttacker(actionsInput.getCardAttacker());
                            commandOutput.setError(result3);
                            objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                            output.addPOJO(objectNode);
                        } else {
                            // verific daca mai sunt in viata eroii
                            if (game.getPlayerOne().getHero().getHealth() <= 0) {
                                GameOverOutput over = new GameOverOutput("Player two killed the enemy hero.");
                                objectNode = objectMapper.convertValue(over, ObjectNode.class);
                                output.addPOJO(objectNode);
                                gameOver = true;
                                statistics.setPlayerTwoWins(statistics.getPlayerTwoWins() + 1);
                            } else if (game.getPlayerTwo().getHero().getHealth() <= 0) {
                                GameOverOutput over2 = new GameOverOutput("Player one killed the enemy hero.");
                                objectNode = objectMapper.convertValue(over2, ObjectNode.class);
                                output.addPOJO(objectNode);
                                gameOver = true;
                                statistics.setPlayerOneWins(statistics.getPlayerOneWins() + 1);
                            }
                        }
                        break;
                    case "useHeroAbility":
                        String result4 = game.useHeroAbility(actionsInput.getAffectedRow());
                        if (result4 != null) {
                            commandOutput = new CommandOutput();
                            commandOutput.setCommand(actionsInput.getCommand());
                            commandOutput.setAffectedRow(actionsInput.getAffectedRow());
                            commandOutput.setError(result4);
                            objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                            output.addPOJO(objectNode);
                        }
                        break;
                    case "getTotalGamesPlayed":
                        commandOutput = new CommandOutput();
                        commandOutput.setCommand(actionsInput.getCommand());
                        commandOutput.setOutput(statistics.getTotalGamesPlayed());
                        objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                        output.addPOJO(objectNode);
                        break;
                    case "getPlayerOneWins":
                        commandOutput = new CommandOutput();
                        commandOutput.setCommand(actionsInput.getCommand());
                        commandOutput.setOutput(statistics.getPlayerOneWins());
                        objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                        output.addPOJO(objectNode);
                        break;
                    case "getPlayerTwoWins":
                        commandOutput = new CommandOutput();
                        commandOutput.setCommand(actionsInput.getCommand());
                        commandOutput.setOutput(statistics.getPlayerTwoWins());
                        objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
                        output.addPOJO(objectNode);
                        break;

                }
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }

    private static boolean skipAction(boolean gameOver, ActionsInput actionInput) {

        return  (gameOver && !actionInput.getCommand().startsWith("get"));

    }

    private static void getPlayerHero(ObjectMapper objectMapper, ArrayNode output, Game game, ActionsInput actionsInput) {
        ObjectNode objectNode;
        CommandOutput commandOutput;
        commandOutput = new CommandOutput();
        commandOutput.setCommand(actionsInput.getCommand());
        commandOutput.setPlayerIdx(actionsInput.getPlayerIdx());
        commandOutput.setOutput(game.getHero(actionsInput.getPlayerIdx()));
        objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
        output.addPOJO(objectNode);
    }

    private static void getPlayerDeck(ObjectMapper objectMapper, ArrayNode output, Game game, ActionsInput actionsInput) {
        ObjectNode objectNode;
        CommandOutput commandOutput;
        commandOutput = new CommandOutput();
        commandOutput.setCommand(actionsInput.getCommand());
        commandOutput.setPlayerIdx(actionsInput.getPlayerIdx());
        commandOutput.setOutput(game.getDeck(actionsInput.getPlayerIdx()));
        objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
        output.addPOJO(objectNode);
    }

    private static void placeCard(ObjectMapper objectMapper, ArrayNode output, Game game, ActionsInput actionsInput) {
        CommandOutput commandOutput;
        ObjectNode objectNode;
        String error = game.putCardOnTable(actionsInput.getHandIdx());

        if (error != null) {
             commandOutput = new CommandOutput();
            commandOutput.setCommand(actionsInput.getCommand());
            commandOutput.setHandIdx(actionsInput.getHandIdx());
            commandOutput.setError(error);
            objectNode = objectMapper.convertValue(commandOutput, ObjectNode.class);
            output.addPOJO(objectNode);
        } else {

        }
    }

}
