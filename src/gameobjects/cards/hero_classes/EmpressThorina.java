package gameobjects.cards.hero_classes;

import gameobjects.cards.GenericCard;
import gameobjects.cards.GenericHero;

import java.util.ArrayList;

public class EmpressThorina extends GenericHero {
    public EmpressThorina(final int mana, final String name,
                          final String description, final ArrayList<String> colors) {
        super(mana, name, description, colors);
    }

    @Override
    public GenericCard useAbility(final ArrayList<GenericCard> cards) {
        if (cards.isEmpty()) {
            return null;
        }

        // Find the card with the most health
        GenericCard enemyMostHP = cards.get(0);
        for (GenericCard enemy : cards) {
            if (enemyMostHP.getHealth() < enemy.getHealth()) {
                enemyMostHP = enemy;
            }
        }

        super.setHasAttacked(true);
        return enemyMostHP;
    }
}
