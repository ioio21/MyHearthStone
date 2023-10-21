package game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fileio.CardInput;

import java.util.List;

public abstract class HeroCard extends Card {
    private int health;
    @JsonIgnore
    private boolean used;


    public static HeroCard createHero(CardInput card) {
        HeroCard hero = null;
        if (card.getName().equals("Lord Royce")) {
         hero  =  new LordRoyceHeroCard();
        } else if (card.getName().equals("Empress Thorina")) {
            hero = new EmpressThorinaHeroCard();
        } else if (card.getName().equals("King Mudface")) {
            hero = new KingMudfaceHeroCard();
        } else if (card.getName().equals("General Kocioraw")) {
            hero = new GeneralKociorawHeroCard();
        }
        hero.setHealth(30);
        hero.setMana(card.getMana());
        hero.setColors(card.getColors());
        hero.setDescription(card.getDescription());
        hero.setName(card.getName());
        return hero;
    }



    public abstract HeroCard copyHeroCard();

    public void useAbility(List<SpecialCard> cards) {
        this.setUsed(true);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void hitHealth(int n) {
       this.health -= n;
    }
}
