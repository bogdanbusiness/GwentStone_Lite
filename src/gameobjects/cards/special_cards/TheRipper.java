package gameobjects.cards.special_cards;

import gameobjects.cards.genericCard;
import fileio.CardInput;
import utils.GameConstants;

public class TheRipper extends genericCard {
    public TheRipper() {
        super();
        super.setTank(false);
    }

    public TheRipper(CardInput card) {
        super(card);
        super.setTank(false);
    }

    @Override
    public int getRowPlacement (int playerIndex) {
        return playerIndex == 1 ? GameConstants.PLAYER1_FRONT_ROW : GameConstants.PLAYER2_FRONT_ROW;
    }
}

