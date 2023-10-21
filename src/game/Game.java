package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game {

    private int mana; // todo remove mana from game
    private int round;
    private int playerTurn;
    private List<List<SpecialCard>> table;
    private Player playerOne;
    private Player playerTwo;


    public Game(Statistics statistics) {
        mana = 1;
        round = 1;
        table = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            table.add(new ArrayList<>(5));
        }
        statistics.setTotalGamesPlayed(statistics.getTotalGamesPlayed() + 1);
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public List<List<SpecialCard>> getTable() {
        return table;
    }

    public void setTable(List<List<SpecialCard>> table) {
        this.table = table;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public void switchPlayerTurn() {
        playerTurn = 3 - playerTurn;
    }

    public void startNewRound() {
        round++;
    }

    private String validatePlaceCard(Card card) {

        if (!card.getSpecial()) {
            return "Cannot place environment card on table.";
        }

        if ((playerTurn == 1 && card.getMana() > playerOne.getMana()) ||
            (playerTurn == 2 && card.getMana() > playerTwo.getMana())   ) {
            return "Not enough mana to place card on table.";
        }

        if (playerTurn == 1) {
            if ((SpecialCard.frontRowCards.contains(card.getName()) && table.get(2).size() == 5) ||
                ((SpecialCard.backRowCards.contains(card.getName()) && table.get(3).size() == 5))) {
                return "Cannot place card on table since row is full.";
            }
        } else {
            if ((SpecialCard.frontRowCards.contains(card.getName()) && table.get(1).size() == 5) ||
                    ((SpecialCard.backRowCards.contains(card.getName()) && table.get(0).size() == 5))) {
                return "Cannot place card on table since row is full.";
            }
        }

        return null;
    }

    public String putCardOnTable(int handIdx) {

        Player player;

        if (playerTurn == 1) {
            player = getPlayerOne();
        } else {
            player = getPlayerTwo();
        }

        Card card = player.getHand().get(handIdx);

        String error = validatePlaceCard(card);

        if (null == error) {

            if (SpecialCard.frontRowCards.contains(card.getName()) && playerTurn == 1) {
                table.get(2).add((SpecialCard)card);
            } else if (SpecialCard.backRowCards.contains(card.getName()) && playerTurn == 1) {
                table.get(3).add((SpecialCard) card);
            } else if (SpecialCard.frontRowCards.contains(card.getName()) && playerTurn == 2) {
                table.get(1).add((SpecialCard)card);
            } else if (SpecialCard.backRowCards.contains(card.getName()) && playerTurn == 2) {
                table.get(0).add((SpecialCard)card);
            }

            player.decreaseMana(card.getMana());

            player.getHand().remove(card);

            // de mine
           // player.decreaseMana(card.getMana());
        }

        return error;
    }

    public List<Card> getDeck(int indexPlayer) {
        Player player;
        if (indexPlayer == 1) {
            player = getPlayerOne();
        } else {
            player = getPlayerTwo();
        }

        return player.cloneCardsList(player.getDeck());
    }

    public List<Card> getCardsInHand(int indexPlayer) {
        Player player;
        if (indexPlayer == 1) {
            player = getPlayerOne();
        } else {
            player = getPlayerTwo();
        }
        return player.cloneCardsList(player.getHand());
    }

    public List<List<SpecialCard>> getCardsOnTable() {
        List<List<SpecialCard>> copyTable = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            copyTable.add(new ArrayList<>());
            int n = table.get(i).size();
            for (int j = 0; j < n; j++) {
                copyTable.get(i).add((SpecialCard)table.get(i).get(j).copyCard());
            }
        }
        return table;
        // aici poate trebuia dierct sa returnez o lista de special cards

    }

    public HeroCard getHero(int indexPlayer) {
        Player player;
        if (indexPlayer == 1) {
            player = getPlayerOne();
        } else {
            player = getPlayerTwo();
        }

        return player.getHero().copyHeroCard() ;

    }

    public int getPlayerMana(int indexPlayer) {
        Player player;
        if (indexPlayer == 1) {
            player = getPlayerOne();
        } else {
            player = getPlayerTwo();
        }
        return player.getMana();
    }

    public String checkEnvironment(Card card, int affecedRow, int playerInd) {
        // verific daca e de tipul Environment
        if (card.getSpecial()) {
            return "Chosen card is not of type environment.";
        }
        if ((playerInd == 1 && playerOne.getMana() < card.getMana()) || (playerInd == 2 && playerTwo.getMana() < card.getMana())) {
            return "Not enough mana to use environment card.";
        }
        if ((playerInd == 1 && (affecedRow == 2 || affecedRow == 3)) || (playerInd == 2 && (affecedRow == 0 ||affecedRow == 1))) {
            return "Chosen row does not belong to the enemy.";
        }
        if (card.getName().equals("Heart Hound")) {
            if ((playerInd == 1 && table.get(3 - affecedRow).size() == 5) || (playerInd == 2 && table.get(3- affecedRow).size() == 5)) {
                return "Cannot steal enemy card since the player's row is full.";
            }
        }
        return null;
    }

    public String useEnvironmentCard(int handIdx, int affectedRow) {
        // stabilesc cine joaca
        Player player;


        if (playerTurn == 1) {
            player = getPlayerOne();

        } else {
            player = getPlayerTwo();

        }

        Card card = player.getHand().get(handIdx);
        String validate = checkEnvironment(card, affectedRow, playerTurn);
        if (validate == null) {
            // nu am erori si pot face actiunea
            if (card.getName().equals("Firestorm")) {
                useFirestormEnvironment(affectedRow);
            } else if (card.getName().equals("Winterfell")) {
                useWinterfellEnvironment(affectedRow);
            } else {
                // Heart Hound
                useWinterfellHeartHound(affectedRow);
            }
            //scad mana
            // o scot din mana jucatorului
            player.decreaseMana(card.getMana());
            player.getHand().remove(card);
        }
        return validate;
    }

    public int lookForMaxLife(List<SpecialCard> cards) {
        SpecialCard max = cards.get(0);
        int maxInd = 0;
        for ( int i = 0; i < cards.size(); i++) {
            if(max.getHealth() < cards.get(i).getHealth()) {
                max = cards.get(i);
                maxInd = i;
            }
        }
        return maxInd;
    }

    void useFirestormEnvironment(int affectedRow) {
        // scade cu 1 viata minionilor de pe rand si daca moare il dau afara
        List <SpecialCard> row = table.get(affectedRow);
        Iterator<SpecialCard> iterator = row.iterator();
        while(iterator.hasNext()) {
            SpecialCard minion =  iterator.next();
           minion.decreaseHealth(1); // aici e ok?
            if (minion.getHealth() <= 0 ) {
                // trebuie scoasa
                iterator.remove();
            }

        }
    }

    void useWinterfellEnvironment(int affectedRow) {
        for (int i = 0; i < table.get(affectedRow).size(); i++) {
            SpecialCard card = table.get(affectedRow).get(i);
            card.setFrozen(true);

        }
    }

    void useWinterfellHeartHound(int affectedRow) {
        //Se fură minionul adversarului cu cea mai mare viață de pe rând și se pune pe rândul “oglindit” aferent jucătorului.
       // Card card  = table.get(affectedRow).stream().max(Card::getHealth);
        int max = lookForMaxLife(table.get(affectedRow));
        SpecialCard stolenCard = table.get(affectedRow).get(max);
        table.get(3- affectedRow).add(stolenCard);
        table.get(affectedRow).remove(stolenCard);

    }

    public List<Card> getEnvironmentFromHand(int indexPlayer) {
        Player player;

        if (indexPlayer == 1) {
            player = getPlayerOne();

        } else {
            player = getPlayerTwo();
        }
        List<Card> environments = new ArrayList<>();
        for (int i = 0; i < player.getHand().size(); i++) {
            if (player.getHand().get(i).getSpecial() == false) {
                environments.add(player.getHand().get(i));
            }
        }
        return player.cloneCardsList(environments);
    }

    public List<SpecialCard> frozenCards() {
        List<SpecialCard> frozenCards = new ArrayList<>();
        for (int i = 0; i < table.size(); i++) {
            for ( int j = 0; j < table.get(i).size(); j++) {
                if (table.get(i).get(j).isFrozen() == true) {
                    frozenCards.add(new SpecialCard(table.get(i).get(j)));
                }
            }
        }
        return frozenCards;
    }

    public void unfrozeCards(int row1, int row2) {
        for (int i = 0; i < table.get(row1).size(); i++) {
            table.get(row1).get(i).setFrozen(false);
            table.get(row1).get(i).setUsed(false);
        }
        for (int i = 0; i < table.get(row2).size(); i++) {
            table.get(row2).get(i).setFrozen(false);
            table.get(row2).get(i).setUsed(false);
        }
    }

    public void unfroze() {
        int row1, row2;
        if (playerTurn == 1) {
            row1 = 2;
            row2 = 3;
            playerOne.getHero().setUsed(false);
        } else {
            row1 = 0;
            row2 = 1;
            playerTwo.getHero().setUsed(false);
        }
        unfrozeCards(row1, row2);
    }

    int checkTanks () {
        if (playerTurn == 1) {
            // randuri inamice 0 1
            for ( int i = 0; i < table.get(0).size(); i++) {
                if (table.get(0).get(i).isTank()) {
                    return 1; // exista tancuri la adversar
                }

            }
            for (int i = 0; i < table.get(1).size(); i++) {
                if (table.get(1).get(i).isTank()) {
                    return 1;
                }
            }
            return 0;
        } else {
            for ( int i = 0; i < table.get(2).size(); i++) {
                if (table.get(2).get(i).isTank()) {
                    return 1; // exista tancuri la adversar
                }

            }
            for (int i = 0; i < table.get(3).size(); i++) {
                if (table.get(3).get(i).isTank()) {
                    return 1;
                }
            }
            return 0;
        }
    }

    public String validateCardAttack(int xAttacker,int yAttacker,int xVictim,int yVictim) {
        if ((playerTurn == 1 && (xVictim == 2 || xVictim == 3)) || (playerTurn == 2 && (xVictim == 0 || xVictim == 1))) {
            return "Attacked card does not belong to the enemy.";
        }
        else if (table.get(xAttacker).get(yAttacker).isUsed()) {
            return "Attacker card has already attacked this turn.";
        }
        else if (table.get(xAttacker).get(yAttacker).isFrozen()) {
            return "Attacker card is frozen.";
        }
        else if (checkTanks() == 1) {
            // daca exista tanc la inamic
            if (table.get(xVictim).get(yVictim).isTank()) {
                    return null;
            } else {
                return "Attacked card is not of type 'Tank'.";
            }
        } else {
                return null;
        }

    }



    public String cardUsesAttack(int xAttacker, int yAttacker, int xVictim, int yVictim) {
        String check = validateCardAttack(xAttacker, yAttacker, xVictim, yVictim);
        if (check == null) {
            SpecialCard cardAttacker = table.get(xAttacker).get(yAttacker);
            SpecialCard cardVictim = table.get(xVictim).get(yVictim);
            cardVictim.decreaseHealth(cardAttacker.getAttackDamage());
            cardAttacker.setUsed(true);
            if (cardVictim.getHealth() <= 0) {
                table.get(xVictim).remove(yVictim);
            }
        }
        return check;
    }


    public String validateCardAbility(int xAttacker,int yAttacker,int xVictim,int yVictim) {
        SpecialCard cardAttacker = table.get(xAttacker).get(yAttacker);
        if (cardAttacker.isFrozen()) {
            return "Attacker card is frozen.";
        }
        if (cardAttacker.isUsed()) {
            return "Attacker card has already attacked this turn.";
        }
        if (cardAttacker.isDisciple()) {
            // daca e Disciple
            // veriific daca victima e a mea
            if ((playerTurn == 1 && ((xVictim == 0) || (xVictim == 1))) || (playerTurn == 2 && ((xVictim == 2) || (xVictim ==3)))) {
                return "Attacked card does not belong to the current player.";
            } else {
                return null;
            }
        } else if (cardAttacker.isNotDisciple()) {
            if ((playerTurn == 1 && ((xVictim == 2) || (xVictim == 3))) || (playerTurn == 2 && ((xVictim == 0) || (xVictim == 1)))) {
                return "Attacked card does not belong to the enemy.";
            }
            if (checkTanks() == 1) {
                // daca exista tanc la adversar
                if (table.get(xVictim).get(yVictim).isTank()) {
                    return  null;
                } else {
                    return "Attacked card is not of type 'Tank'.";
                }

            } else {
                return null;
            }

        }
        return null;
    }

    public String cardUsesAbility(int xAttacker, int yAttacker, int xVictim, int yVictim) {
        String check = validateCardAbility(xAttacker, yAttacker, xVictim, yVictim);
        if (check == null) {
            SpecialCard cardAttacker = table.get(xAttacker).get(yAttacker);
            SpecialCard cardVictim = table.get(xVictim).get(yVictim);
            if (cardAttacker.getName().equals("The Ripper")) {
                cardVictim.decreaseAttack(2);
                if (cardVictim.getAttackDamage() < 0) {
                    cardVictim.setAttackDamage(0);
                }
            } else if (cardAttacker.getName().equals("Miraj")) {
                int health1 = cardAttacker.getHealth();
                int health2 = cardVictim.getHealth();
                cardVictim.setHealth(health1);
                cardAttacker.setHealth(health2);
            } else if (cardAttacker.getName().equals("The Cursed One")) {
                int healthVictim = cardVictim.getHealth();
                int attackVictim = cardVictim.getAttackDamage();
                if (attackVictim == 0) {
                    //// <= 0
                    table.get(xVictim).remove(yVictim);
                } else {
                    cardVictim.setHealth(attackVictim);
                    cardVictim.setAttackDamage(healthVictim);
                }

            } else if (cardAttacker.getName().equals("Disciple")) {
                cardVictim.decreaseHealth(-2);
            }
            cardAttacker.setUsed(true);
        }
        return check;
    }

    public String validateCardAttackHero(int x, int y) {
        SpecialCard attack = table.get(x).get(y);
        if (attack.isFrozen()) {
            return "Attacker card is frozen.";
        }
        if (attack.isUsed()) {
            return "Attacker card has already attacked this turn.";
        }
        if (checkTanks() == 1) {
            // exista
            return "Attacked card is not of type 'Tank'.";
        }
        return null;
    }

    public String cardAttackHero(int x, int y) {
        String check = validateCardAttackHero(x, y);
        if (check == null) {
            if (playerTurn == 1) {
                playerTwo.getHero().hitHealth(table.get(x).get(y).getAttackDamage());

            } else {
                playerOne.getHero().hitHealth(table.get(x).get(y).getAttackDamage());
            }
            table.get(x).get(y).setUsed(true);
        }
        return check;
    }

    public String useHeroAbility(int affectedRow) {
        String check = validateUserHeroAbility(affectedRow);
        if (check == null) {
            if (playerTurn == 1) {
                playerOne.getHero().useAbility(table.get(affectedRow));
                playerOne.decreaseMana(playerOne.getHero().getMana());
            } else {
                playerTwo.getHero().useAbility(table.get(affectedRow));
                playerTwo.decreaseMana(playerTwo.getHero().getMana());
            }
        }
        return check;

    }

    public boolean isEnemyRow(int row) {
        if (playerTurn == 1) {
            if (row == 0 || row == 1) {
                return true;
            }
            return false;
        } else {
            if (row == 2 || row == 3) {
                return true;
            }
            return false;
        }
    }

    private String validateUserHeroAbility(int affectedRow) {
        Player player;
        if (playerTurn == 1) {
            player = playerOne;
        } else {
            player = playerTwo;
        }
        if (player.getMana() < player.getHero().getMana()) {
            return "Not enough mana to use hero's ability.";
        }
        if (player.getHero().isUsed()) {
            return "Hero has already attacked this turn.";
        }
        if (player.getHero().getName().equals("Lord Royce") || player.getHero().getName().equals("Empress Thorina")) {
            if (isEnemyRow(affectedRow) == false) {
                return "Selected row does not belong to the enemy.";
            }
            return null;
        } else {
            if (isEnemyRow(affectedRow) == true) {
                return "Selected row does not belong to the current player.";
            }
            return null;
        }

    }
}

/*
placeCard

 */
