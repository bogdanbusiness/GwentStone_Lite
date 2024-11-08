package gameobjects.cards.special_cards;

import gameobjects.cards.GenericCard;
import fileio.CardInput;
import utils.GameConstants;

public class Disciple extends GenericCard {
    public Disciple() {
        super();
        super.setTank(false);
    }

    public Disciple(final CardInput card) {
        super(card);
        super.setTank(false);
    }

    @Override
    public final int getRowPlacement(final int playerIndex) {
        return playerIndex == 1 ? GameConstants.PLAYER1_BACK_ROW : GameConstants.PLAYER2_BACK_ROW;
    }

    @Override
    public final void useAbility(final GenericCard ally) {
        ally.setHealth(ally.getHealth() + GameConstants.DISCIPLE_HEAL);
    }
}
