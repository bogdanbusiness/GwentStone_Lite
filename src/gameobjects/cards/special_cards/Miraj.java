package gameobjects.cards.special_cards;

import gameobjects.cards.GenericCard;
import fileio.CardInput;
import utils.GameConstants;

public class Miraj extends GenericCard {
    public Miraj() {
        super();
        super.setTank(false);
    }

    public Miraj(final CardInput card) {
        super(card);
        super.setTank(false);
    }

    @Override
    public final int getRowPlacement(final int playerIndex) {
        return playerIndex == 1 ? GameConstants.PLAYER1_FRONT_ROW : GameConstants.PLAYER2_FRONT_ROW;
    }

    @Override
    public final void useAbility(final GenericCard enemy) {
        int enenmyHP = enemy.getHealth();
        int selfHP = super.getHealth();
        enemy.setHealth(selfHP);
        super.setHealth(enenmyHP);
        super.setHasAttacked(true);
    }
}
