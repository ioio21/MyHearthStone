package game;

import fileio.CardInput;

import java.util.List;

public class LordRoyceHeroCard extends HeroCard {


    @Override
    public void useAbility(List<SpecialCard> cards) {
        int maxAttack = -1, index = -1;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getAttackDamage() > maxAttack) {
                maxAttack = cards.get(i).getAttackDamage();
                index = i;
            }
        }
        cards.get(index).setFrozen(true);
        super.useAbility(cards);

    }

    @Override
    public HeroCard copyHeroCard() {
        HeroCard hero = this;
        LordRoyceHeroCard card = new LordRoyceHeroCard();

        card.setHealth(hero.getHealth());
        card.setColors(hero.getColors());
        card.setMana(hero.getMana());
        card.setDescription(hero.getDescription());
        card.setName(hero.getName());
        card.setUsed(hero.isUsed());
        return card;
    }

    public LordRoyceHeroCard() {

    }
}
