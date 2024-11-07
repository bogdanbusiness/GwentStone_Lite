package gameobjects.cards.card_classes;

import gameobjects.cards.genericCard;
import fileio.CardInput;
import utils.GameConstants;

public class Goliath extends genericCard {
    public Goliath() {
        super();
        super.setTank(true);
    }

    public Goliath(CardInput card) {
        super(card);
        super.setTank(true);
    }

    @Override
    public int getRowPlacement (int playerIndex) {
        return playerIndex == 1 ? GameConstants.PLAYER1_FRONT_ROW : GameConstants.PLAYER2_FRONT_ROW;
    }
}
