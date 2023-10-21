package game;

import fileio.CardInput;

import java.util.List;

public class KingMudfaceHeroCard extends HeroCard {


    @Override
    public void useAbility(List<SpecialCard> cards) {
        for (SpecialCard card : cards) {
            card.setHealth(card.getHealth() + 1);
        }
        super.useAbility(cards);
    }
    @Override
    public HeroCard copyHeroCard() {
        HeroCard hero = this;
        KingMudfaceHeroCard card = new KingMudfaceHeroCard ();

        card.setHealth(hero.getHealth());
        card.setColors(hero.getColors());
        card.setMana(hero.getMana());
        card.setDescription(hero.getDescription());
        card.setName(hero.getName());
        card.setUsed(hero.isUsed());
        return card;
    }

    public KingMudfaceHeroCard() {
    }
}
