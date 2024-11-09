package gameobjects;

import gameobjects.cards.GenericCard;
import gameobjects.cards.GenericHero;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.Getter;
import lombok.Setter;
import utils.GameConstants;
import utils.Point;

import java.util.ArrayList;

public class GameField {
    private final GenericCard[][] field =
            new GenericCard[GameConstants.TABLE_ROWS][GameConstants.TABLE_COLUMNS];
    @Getter @Setter
    private GenericHero player1Hero;
    @Getter @Setter
    private GenericHero player2Hero;

    // Constructor

    public GameField() {
        player1Hero = null;
        player2Hero = null;

        for (int i = 0; i < GameConstants.TABLE_ROWS; i++) {
            for (int j = 0; j < GameConstants.TABLE_COLUMNS; j++) {
                field[i][j] = null;
            }
        }
    }

    // Methods

    /**
     * Adds a GenericCard to the playing field
     * @param genericCard The GenericCard that is placed down
     * @param rowNumber The index of the row where the card will be placed
     */
    public void addCard(final GenericCard genericCard, final int rowNumber) {
        for (int i = 0; i < GameConstants.TABLE_COLUMNS; i++) {
            if (field[rowNumber][i] == null) {
                field[rowNumber][i] = genericCard;
                break;
            }
        }
    }

    /**
     * Gets the card on the field
     * @param point The coordinates of the card on the field
     * @return Returns the GenericCard instance
     */
    public GenericCard getCard(final Point point) {
        return field[point.getRow()][point.getColumn()];
    }

    /**
     * Gets the coordinates of a card on the field
     * @param card The card on the field
     * @param startingRow The row from which the search starts
     * @return Returns the Point instance
     */
    public Point getCardPosition(final GenericCard card, int startingRow) {
        Point returnPoint = new Point();
        for (int i = startingRow; i < startingRow + GameConstants.TABLE_HALF_ROWS; i++) {
            for (int j = 0; j < GameConstants.TABLE_COLUMNS; j++) {
                if (field[i][j] != null && field[i][j].equals(card)) {
                    returnPoint.setRow(i);
                    returnPoint.setColumn(j);
                    return returnPoint;
                }
            }
        }

        return null;
    }

    /**
     * Removes the card from the field
     * @param point The coordinates of the card on the field
     */
    public void removeCard(final Point point) {
        // Shifts every column on the row to the left
        for (int i = 0; i < GameConstants.TABLE_COLUMNS - 1; i++) {
            field[point.getRow()][i] = field[point.getRow()][i + 1];
        }
        field[point.getRow()][GameConstants.TABLE_COLUMNS - 1] = null;
    }

    /**
     * Gets the number of cards on the row
     * @param row The row checked
     * @return Returns the number of cards on the row checked
     */
    public int getRowOccupancy(final int row) {
        int cards = 0;

        for (int i = 0; i < GameConstants.TABLE_COLUMNS; i++) {
            if (field[row][i] != null) {
                cards++;
            }
        }
        return cards;
    }

    /**
     * Gets the cards on a row
     * @param row The row requested
     * @return An ArrayList instance with all the cards requested
     */
    public ArrayList<GenericCard> getRowCards(final int row) {
        ArrayList<GenericCard> cards = new ArrayList<>(5);
        for (int i = 0; i < GameConstants.TABLE_COLUMNS; i++) {
            if (field[row][i] != null) {
                cards.add(field[row][i]);
            }
        }

        return cards;
    }

    /**
     * Completely resets the field
     */
    public void resetField() {
        for (int i = 0; i < GameConstants.TABLE_ROWS; i++) {
            for (int j = 0; j < GameConstants.TABLE_COLUMNS; j++) {
                field[i][j] = null;
            }
        }
        player1Hero = null;
        player2Hero = null;
    }

    /**
     * Checks if a card is an enemy
     * @param cardCoordinates The coordinates of the card checked
     * @param playerTurn The turn of the player who attacks
     * @return Returns whether a card is an enemy or not
     */
    public boolean isEnemy(final Point cardCoordinates, final int playerTurn) {
        int cardRow = cardCoordinates.getRow();
        // Check the respective rows associated with a player
        if (playerTurn == 1
            && (cardRow == GameConstants.PLAYER1_FRONT_ROW
                || cardRow == GameConstants.PLAYER1_BACK_ROW)) {
                return false;
        } else if (playerTurn == 2
                 && (cardRow == GameConstants.PLAYER2_FRONT_ROW
                 || cardRow == GameConstants.PLAYER2_BACK_ROW)) {
                return false;
        }
        return true;
    }

    /**
     * Gets the number of tanks on the front row of the other player
     * @param playerTurn The turn of the player
     * @return The number of tanks on the row
     */
    public int getTanksOnRow(final int playerTurn) {
        int tankNumber = 0;

        if (playerTurn == 1) {
            for (int i = 0; i < GameConstants.TABLE_COLUMNS; i++) {
                if (field[GameConstants.PLAYER2_FRONT_ROW][i] != null
                    && field[GameConstants.PLAYER2_FRONT_ROW][i].isTank()) {
                    tankNumber++;
                }
            }
        } else {
            for (int i = 0; i < GameConstants.TABLE_COLUMNS; i++) {
                if (field[GameConstants.PLAYER1_FRONT_ROW][i] != null
                    &&  field[GameConstants.PLAYER1_FRONT_ROW][i].isTank()) {
                    tankNumber++;
                }
            }
        }

        return tankNumber;
    }

    /**
     * Enables cards to attack again and unfreezes them
     */
    public void resetAttackForCards() {
        for (int i = 0; i < GameConstants.TABLE_ROWS; i++) {
            for (int j = 0; j < GameConstants.TABLE_COLUMNS; j++) {
                if (field[i][j] != null) {
                    field[i][j].setHasAttacked(false);
                }
            }
        }
    }

    /**
     * Unfreezes all the cards that belong to a player
     * @param playerTurn The player to which the cards belong to
     */
    public void unfreezePlayerCards(int playerTurn) {
        int startingRow = playerTurn == 1 ? 2 : 0;

        for (int i = startingRow; i < startingRow + GameConstants.TABLE_HALF_ROWS; i++) {
            for (int j = 0; j < GameConstants.TABLE_COLUMNS; j++) {
                if (field[i][j] != null) {
                    field[i][j].setHasAttacked(false);
                }
            }
        }
    }

    /**
     * Returns in JSON format the entire game field
     * @return ArrayNode with the formatted JSON
     */
    public ArrayNode printAllCardsOnTable() {
        // Creates the output
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode output = mapper.createArrayNode();

        // Iterate through the entire field
        for (int i = 0; i < GameConstants.TABLE_ROWS; i++) {
            ArrayNode row = mapper.createArrayNode();
            for (int j = 0; j < GameConstants.TABLE_COLUMNS; j++) {
                if (field[i][j] != null) {
                    row.add(field[i][j].printCard());
                }
            }
            output.add(row);
        }

        return output;
    }

    /**
     * Returns in JSON format the frozen cards on the game field
     * @return ArrayNode with the formatted JSON
     */
    public ArrayNode printAllFrozenCardsOnTable() {
        // Creates the output
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode output = mapper.createArrayNode();

        // Iterate through the entire field
        for (int i = 0; i < GameConstants.TABLE_ROWS; i++) {
//            ArrayNode row = mapper.createArrayNode();
            for (int j = 0; j < GameConstants.TABLE_COLUMNS; j++) {
                if (field[i][j] != null && field[i][j].isFrozen()) {
                    output.add(field[i][j].printCard());
                }
            }
//            output.add(row);
        }

        return output;
    }
}
