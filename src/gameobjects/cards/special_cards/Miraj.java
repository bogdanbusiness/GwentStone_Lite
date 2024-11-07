package gameobjects.cards.special_cards;

import gameobjects.cards.genericCard;
import fileio.CardInput;
import utils.GameConstants;

public class Miraj extends genericCard {
    public Miraj() {
        super();
        super.setTank(false);
    }

    public Miraj(CardInput card) {
        super(card);
        super.setTank(false);
    }

    @Override
    public int getRowPlacement (int playerIndex) {
        return playerIndex == 1 ? GameConstants.PLAYER1_FRONT_ROW : GameConstants.PLAYER2_FRONT_ROW;
    }
}
