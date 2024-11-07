package gameobjects.cards.special_cards;

import gameobjects.cards.genericCard;
import fileio.CardInput;
import utils.GameConstants;

public class Disciple extends genericCard {
    public Disciple() {
        super();
        super.setTank(false);
    }

    public Disciple(CardInput card) {
        super(card);
        super.setTank(false);
    }

    @Override
    public int getRowPlacement (int playerIndex) {
        return playerIndex == 1 ? GameConstants.PLAYER1_BACK_ROW : GameConstants.PLAYER2_BACK_ROW;
    }
}
