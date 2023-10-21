package game;

import fileio.CardInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Player {

    private List<Card> deck;
    private HeroCard hero;
    private List<Card> hand;
    private int mana;

    public Player() {
        hand = new ArrayList<>();
        mana = 0;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public HeroCard getHero() {
        return hero;
    }

    public void setHero(HeroCard hero) {
        this.hero = hero;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void shuffleDeck(int seed) {
        Collections.shuffle(deck, new Random(seed));
    }

    public static List<Card> createDeck(List<CardInput> cardInputs) {

        List<Card> cards = new ArrayList<>();

        for (CardInput cardInput : cardInputs) {
            cards.add(Card.createCard(cardInput));
        }

        return cards;
    }

    public List<Card> cloneCardsList(List<Card> cards) {
        List<Card> newList = new ArrayList<>();
        for(Card card : cards) {
            newList.add(card.copyCard());
        }
        return newList;
    }

    public void preparePlayer(List<CardInput> deck, CardInput hero, int shuffleSeed) {

        this.deck = Player.createDeck(deck);
        shuffleDeck(shuffleSeed);

       this.hero = HeroCard.createHero(hero);

    }

    public void addManaByRound(int round) {
        if (round <= 10) {
            mana += round;
        } else {
//            mana += 10;
        }
    }

    public void startRound(Game game) {

        if (!this.getDeck().isEmpty()) {
            this.getHand().add(this.getDeck().get(0));
            this.getDeck().remove(0);
        }

        this.addManaByRound(game.getRound());
    }

    public void decreaseMana(int cardMana) {
        this.mana -= cardMana;
    }
}
