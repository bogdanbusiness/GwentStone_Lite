package gameobjects;

import gameobjects.cards.genericHero;
import gameobjects.cards.genericCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.Getter;
import lombok.Setter;
import utils.GameConstants;
import utils.Point;

public class GameField {
    private final genericCard[][] field = new genericCard[GameConstants.TABLE_ROWS][GameConstants.TABLE_COLUMNS];
    @Getter @Setter
    private genericHero player1Hero;
    @Getter @Setter
    private genericHero player2Hero;

    // Constructor

    public GameField() {
        player1Hero = null;
        player2Hero = null;

        for (int i = 0; i < GameConstants.TABLE_ROWS; i++)
            for (int j = 0; j < GameConstants.TABLE_COLUMNS; j++)
                field[i][j] = null;
    }

    // Methods

    /**
     * Adds a genericCard to the playing field
     * @param genericCard The genericCard that is placed down
     * @param rowNumber The index of the row where the card will be placed
     */
    public void addCard(genericCard genericCard, int rowNumber) {
        for (int i = 0; i < GameConstants.TABLE_COLUMNS; i++)
            if (field[rowNumber][i] == null) {
                field[rowNumber][i] = genericCard;
                break;
            }
    }

    /**
     * Gets the card on the field
     * @param point The coordinates of the card on the field
     * @return Returns the genericCard instance
     */
    public genericCard getCard(Point point) {
        return field[point.getRow()][point.getColumn()];
    }

    /**
     * Removes the card from the field
     * @param point The coordinates of the card on the field
     */
    public void removeCard(Point point) {
        // Shifts every column on the row to the left
        for (int i = point.getRow(); i < GameConstants.TABLE_ROWS; i++)
            field[point.getRow()][i] = field[point.getRow()][i + 1];
        field[point.getRow()][GameConstants.TABLE_COLUMNS - 1] = null;
    }

    /**
     * Gets the number of cards on the row
     * @param row The row checked
     * @return Returns the number of cards on the row checked
     */
    public int getRowOccupancy(int row) {
        int cards = 0;

        for (int i = 0; i < GameConstants.TABLE_COLUMNS; i++)
            if (field[row][i] != null)
                cards++;

        return cards;
    }

    /**
     * Completely resets the field
     */
    public void resetField() {
        for (int i = 0; i < GameConstants.TABLE_ROWS; i++)
            for (int j = 0; j < GameConstants.TABLE_COLUMNS; j++)
                field[i][j] = null;

        player1Hero = null;
        player2Hero = null;
    }

    public boolean isEnemy(Point cardCoordinates, int playerTurn) {
        int cardRow = cardCoordinates.getRow();
        if (playerTurn == 1 &&
                (cardRow == GameConstants.PLAYER1_FRONT_ROW || cardRow == GameConstants.PLAYER1_BACK_ROW))
            return false;
        else if (playerTurn == 2 &&
                 (cardRow == GameConstants.PLAYER2_FRONT_ROW || cardRow == GameConstants.PLAYER2_BACK_ROW))
            return false;

        return true;
    }

    /**
     * Unfreezes all the cards on the board
     */
    public void unfreezeCards() {

    }

    /**
     * Returns in JSON format the held hand of the player
     * @return ArrayNode with the formatted JSON
     */
    public ArrayNode printAllCardsOnTable () {
        // Creates the output
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode output = mapper.createArrayNode();

        // Iterate through the entire field
        for (int i = 0; i < GameConstants.TABLE_ROWS; i++) {
            ArrayNode row = mapper.createArrayNode();
            for (int j = 0; j < GameConstants.TABLE_COLUMNS; j++)
                if (field[i][j] != null)
                    row.add(field[i][j].printCard());
            output.add(row);
        }

        return output;
    }
}
