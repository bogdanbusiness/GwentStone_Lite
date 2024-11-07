package gameobjects.cards.card_classes;
import gameobjects.cards.GenericCard;
import fileio.CardInput;
import utils.GameConstants;

public class Berserker extends GenericCard {
    public Berserker() {
        super();
        super.setTank(false);
    }

    public Berserker(final CardInput card) {
        super(card);
        super.setTank(false);
    }

    @Override
    public final int getRowPlacement(final int playerIndex) {
        return playerIndex == 1 ? GameConstants.PLAYER1_BACK_ROW : GameConstants.PLAYER2_BACK_ROW;
    }
}
