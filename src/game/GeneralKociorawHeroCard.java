package game;

import fileio.CardInput;

import java.util.List;

public class GeneralKociorawHeroCard extends HeroCard {

    @Override
    public void useAbility(List<SpecialCard> cards) {
        for (SpecialCard card : cards) {
            card.setAttackDamage(card.getAttackDamage() + 1);
        }
       super.useAbility(cards);
    }

    @Override
    public HeroCard copyHeroCard() {
        HeroCard hero = this;
        GeneralKociorawHeroCard card = new GeneralKociorawHeroCard();

        card.setHealth(hero.getHealth());
        card.setColors(hero.getColors());
        card.setMana(hero.getMana());
        card.setDescription(hero.getDescription());
        card.setName(hero.getName());
        card.setUsed(hero.isUsed());
        return card;
    }

    public GeneralKociorawHeroCard() {
    }
}
