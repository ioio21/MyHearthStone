package game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fileio.CardInput;

import java.util.ArrayList;

public class Card {

    private int mana;
    private String description;
    private  ArrayList<String> colors;
    private String name;

    @JsonIgnore
   private boolean special;

    public Card() {
        special = false;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    //////////////////////////////////////

    public Card copyCard() {

        if (this.getSpecial() == true) {
            SpecialCard source = (SpecialCard) this;
            SpecialCard copy = new SpecialCard(source);
            copy.setSpecial(true);
            copy.setFrozen(source.isFrozen());
            copy.setHealth(source.getHealth());
            copy.setAttackDamage(source.getAttackDamage());
            return copy;

        } else {
            Card copy = new Card();
            copy.mana = this.mana;
            copy.name = this.name;
            copy.description = this.description;
            copy.colors = this.getColors();
            return copy;
        }


    }


    public static Card createCard(CardInput cardInput) {

        if (SpecialCard.minionNames.contains(cardInput.getName())) {

            SpecialCard specialCard = new SpecialCard();
            specialCard.setName( cardInput.getName());
            specialCard.setDescription(cardInput.getDescription());
            specialCard.setMana(cardInput.getMana());
            specialCard.setColors(cardInput.getColors());

            specialCard.setAttackDamage(cardInput.getAttackDamage());
            specialCard.setHealth(cardInput.getHealth());

            // cred ca aici trebuie si set special
            specialCard.setSpecial(true);

            return specialCard;

        } else {

            Card card = new Card();
            card.name = cardInput.getName();
            card.description = cardInput.getDescription();
            card.mana = cardInput.getMana();
            card.colors = cardInput.getColors();

            return card;
        }
    }




}