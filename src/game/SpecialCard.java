package game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.List;

public class SpecialCard extends Card {

    public static final List<String> minionNames = Arrays.asList("Sentinel", "Berserker", "Goliath", "Warden" , "Miraj", "The Ripper", "Disciple", "The Cursed One");
    public static final List<String> frontRowCards = Arrays.asList("Goliath", "Warden" , "Miraj", "The Ripper");
    public static final List<String> backRowCards = Arrays.asList("Sentinel", "Berserker", "Disciple", "The Cursed One");

    private int attackDamage;
    private int health;

    @JsonIgnore
    private boolean frozen;

    @JsonIgnore
    private boolean used;

    public SpecialCard( Card card) {
        this.setMana(card.getMana());
        this.setColors(card.getColors());
        this.setDescription(card.getDescription());
        this.setName(card.getName());
        // aici mai trebuie sa pun si special?
        this.setSpecial(true);
    }
    public SpecialCard() {
        this.setSpecial(true);
    }

    public SpecialCard(SpecialCard card) {
        this.setMana(card.getMana());
        this.setColors(card.getColors());
        this.setDescription(card.getDescription());
        this.setName(card.getName());
        // aici mai trebuie sa pun si special?
        this.setSpecial(true);
        this.setHealth(card.getHealth());
        this.setAttackDamage(card.getAttackDamage());
        this.setFrozen(card.isFrozen());
        this.setUsed(card.isUsed());
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void decreaseHealth(int n) {
        health = health - n;
    }

    public void decreaseAttack(int n) {
        attackDamage = attackDamage - n;
    }

    @JsonIgnore
    public boolean isTank() {
        return (this.getName().equals("Goliath") || this.getName().equals("Warden"));
    }
    @JsonIgnore
    public boolean isDisciple() {
        return (this.getName().equals("Disciple"));
    }


    @JsonIgnore
    public boolean isNotDisciple() {
        return (this.getName().equals("The Ripper") || this.getName().equals("Miraj") || this.getName().equals("The Cursed One"));
    }
}
