package gameobjects.cards.card_classes;

import gameobjects.cards.genericCard;
import fileio.CardInput;
import utils.GameConstants;

public class Sentinel extends genericCard {
    public Sentinel() {
        super();
        super.setTank(false);
    }

    public Sentinel(CardInput card) {
        super(card);
        super.setTank(false);
    }

    @Override
    public int getRowPlacement (int playerIndex) {
        return playerIndex == 1 ? GameConstants.PLAYER1_BACK_ROW : GameConstants.PLAYER2_BACK_ROW;
    }
}
