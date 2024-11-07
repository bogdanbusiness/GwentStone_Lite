package gameobjects.cards.card_classes;

import gameobjects.cards.GenericCard;
import fileio.CardInput;
import utils.GameConstants;

public class Goliath extends GenericCard {
    public Goliath() {
        super();
        super.setTank(true);
    }

    public Goliath(final CardInput card) {
        super(card);
        super.setTank(true);
    }

    @Override
    public final int getRowPlacement(final int playerIndex) {
        return playerIndex == 1 ? GameConstants.PLAYER1_FRONT_ROW : GameConstants.PLAYER2_FRONT_ROW;
    }
}
