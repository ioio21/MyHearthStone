package game;

import fileio.CardInput;

import java.util.List;

public class EmpressThorinaHeroCard extends HeroCard {

    @Override
    public void useAbility(List<SpecialCard> cards) {
        int maxHealth = 0, index = -1;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getHealth() > maxHealth) {
                maxHealth = cards.get(i).getHealth();
                index = i;
            }
        }
        cards.remove(index);
        super.useAbility(cards);
    }

    public EmpressThorinaHeroCard() {
    }

    @Override
    public HeroCard copyHeroCard() {
        HeroCard hero = this;
        EmpressThorinaHeroCard card = new EmpressThorinaHeroCard();

        card.setHealth(hero.getHealth());
        card.setColors(hero.getColors());
        card.setMana(hero.getMana());
        card.setDescription(hero.getDescription());
        card.setName(hero.getName());
        card.setUsed(hero.isUsed());
        return  card;
    }
}
