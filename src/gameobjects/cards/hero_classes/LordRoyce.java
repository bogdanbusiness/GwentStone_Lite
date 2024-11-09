package gameobjects.cards.hero_classes;

import gameobjects.cards.GenericCard;
import gameobjects.cards.GenericHero;

import java.util.ArrayList;

public class LordRoyce extends GenericHero {
    public LordRoyce(final int mana, final String name,
                     final String description, final ArrayList<String> colors) {
        super(mana, name, description, colors);
    }

    @Override
    public GenericCard useAbility(final ArrayList<GenericCard> cards) {
        if (cards.isEmpty()) {
            return null;
        }

        // Freeze all the cards
        for (GenericCard enemy : cards) {
            this.freeze(enemy);
        }

        super.setHasAttacked(true);
        return null;
    }
}
