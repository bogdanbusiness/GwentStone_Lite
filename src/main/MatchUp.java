package main;

import gameobjects.cards.genericCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;

import gameobjects.GameField;
import gameobjects.Player;

import lombok.Getter;
import lombok.Setter;
import utils.GameConstants;
import utils.Point;

import java.util.ArrayList;

@Getter @Setter
public class MatchUp {
    private static MatchUp instance = null;
    private GameField field;
    private Player player1;
    private Player player2;
    private int shuffleSeed;

    private int playerTurn;
    private int turnCounter;

    private int debug_breakpoint_counter = 0;

    // Constructors

    // The match-up is a singleton
    private MatchUp(Input input) {
        field = new GameField();
        player1 = new Player(input.getPlayerOneDecks());
        player2 = new Player(input.getPlayerTwoDecks());
        turnCounter = 1;
    }

    // Methods

    /**
     * Singleton method for class MatchUp
     * @return Returns the MatchUp instance
     */
    public static MatchUp getInstance(Input input) {
        if (instance == null)
            return new MatchUp(input);
        return instance;
    }

    /**
     * Resets the entire field
     */
    public void resetMatchUp() {
        field.resetField();
        player1.resetPlayer();
        player2.resetPlayer();
        turnCounter = 1;
    }

    /**
     * This method starts a new game for this MatchUp
     * @param input The game played
     */
    public void startNewGame(GameInput input) {
        // Choose the deck for the players
        player1.chooseDeck(input.getStartGame().getPlayerOneDeckIdx());
        player2.chooseDeck(input.getStartGame().getPlayerTwoDeckIdx());

        // Choose the Heroes for the players
        player1.setGenericHero(input.getStartGame().getPlayerOneHero());
        player2.setGenericHero(input.getStartGame().getPlayerTwoHero());

        // Set the starting player and shuffle seed
        playerTurn = input.getStartGame().getStartingPlayer();
        shuffleSeed = input.getStartGame().getShuffleSeed();

        // Set the hero instances for the game field
        field.setPlayer1Hero(player1.getGenericHero());
        field.setPlayer2Hero(player2.getGenericHero());

        // Shuffle the player decks
        player1.shufflePlayerDeck(shuffleSeed);
        player2.shufflePlayerDeck(shuffleSeed);

        // Add the first card from the deck to the shuffle
        player1.drawCardFromDeck();
        player2.drawCardFromDeck();

        // Add mana to the players
        player1.receiveMana(turnCounter);
        player2.receiveMana(turnCounter);
    }

    /**
     * Handles the start of a new round
     */
    public void startRound() {
        // Add mana to the players
        player1.receiveMana(turnCounter);
        player2.receiveMana(turnCounter);

        // Draw cards from stockpile
        player1.drawCardFromDeck();
        player2.drawCardFromDeck();

        // Unfreezes the cards
        field.unfreezeCards();
    }

    /**
     * Plays a game and performs debug actions
     * @param actions The actions performed by players and/or debuggers
     */
    public ArrayNode playGame(ArrayList<ActionsInput> actions) {
        // Create the mapper and the output array
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode output = mapper.createArrayNode();

        // Iterate over the given actions
        for (ActionsInput action : actions) {
            // Create a new object node and add the command to it
            ObjectNode objectNode = mapper.createObjectNode();
            ArrayNode retArrayNode;
            ObjectNode retObjectNode;
            int retInt;
            String retStringError;

            // Execute the correct action
            switch (action.getCommand()) {
                case "getPlayerDeck":
                    objectNode.put("command", action.getCommand());
                    objectNode.put("playerIdx", action.getPlayerIdx());
                    retArrayNode = action.getPlayerIdx() == 1 ? player1.toJsonPlayerDeck() : player2.toJsonPlayerDeck();
                    objectNode.set("output", retArrayNode);
                    break;

                case "getPlayerHero":
                    objectNode.put("command", action.getCommand());
                    objectNode.put("playerIdx", action.getPlayerIdx());
                    retObjectNode = action.getPlayerIdx() == 1 ? player1.toJsonPlayerHero() : player2.toJsonPlayerHero();
                    objectNode.set("output", retObjectNode);
                    break;

                case "endPlayerTurn":
                    playerTurn = playerTurn == 1 ? 2 : 1;
                    turnCounter++;
                    if (turnCounter % 2 == 1)
                        startRound();
                    break;

                case "getPlayerTurn":
                    objectNode.put("command", action.getCommand());
                    objectNode.put("output", playerTurn);
                    break;

                case "getCardsInHand":
                    objectNode.put("command", action.getCommand());
                    objectNode.put("playerIdx", action.getPlayerIdx());
                    retArrayNode = action.getPlayerIdx() == 1 ? player1.toJsonPlayerHand() : player2.toJsonPlayerHand();
                    objectNode.set("output", retArrayNode);
                    break;

                case "placeCard":
                    retStringError = handlePlaceCard(action);
                    if (retStringError == null)
                        break;
                    objectNode.put("command", action.getCommand());
                    objectNode.put("error", retStringError);
                    objectNode.put("handIdx", action.getHandIdx());
                    break;

                case "getPlayerMana":
                    objectNode.put("command", action.getCommand());
                    objectNode.put("playerIdx", action.getPlayerIdx());
                    retInt = action.getPlayerIdx() == 1 ? player1.getMana() : player2.getMana();
                    objectNode.put("output", retInt);
                    break;

                case "getCardsOnTable":
                    objectNode.put("command", action.getCommand());
                    retArrayNode = field.printAllCardsOnTable();
                    objectNode.set("output", retArrayNode);
                    break;

                case "cardUsesAttack":
                    Point attackerCoords = new Point(action.getCardAttacker().getX(), action.getCardAttacker().getY());
                    Point defenderCoords = new Point(action.getCardAttacked().getX(), action.getCardAttacked().getY());
                    retStringError = handleAttackCard(attackerCoords, defenderCoords);
                    if (retStringError == null)
                        break;
                    objectNode.put("command", action.getCommand());
                    objectNode.set("cardAttacker", attackerCoords.toJson());
                    objectNode.set("cardAttacked", defenderCoords.toJson());
                    objectNode.put("error", retStringError);
                    break;

//                TODO: Remove debug case
                case "breakpoint":
                    debug_breakpoint_counter++;
                    break;

                default:
                    System.out.println("Error: Command not recognised.");
                    System.out.println("Command: " + action.getCommand());
            }

            if (!objectNode.isEmpty())
                output.add(objectNode);
        }

        return output;
    }

    /**
     * Handles the placement of cards on the field
     * @param action The command that was given
     * @return Null on success or an error string on failure
     */
    public String handlePlaceCard(ActionsInput action) {
        // Get the card from hand
        int handIndex = action.getHandIdx();
        genericCard card = playerTurn == 1 ? player1.placeCardFromHand(handIndex) : player2.placeCardFromHand(handIndex);
        if (card == null)
            return "Not enough mana to place card on table.";

        //   Check if we can place the card and return the card to the hand of the player if we cant
        int rowAffected = card.getRowPlacement(playerTurn);
        if (field.getRowOccupancy(rowAffected) == GameConstants.TABLE_COLUMNS) {
            if (playerTurn == 1)
                player1.returnCardToHand(card, handIndex);
            else
                player2.returnCardToHand(card, handIndex);
            return "Cannot place card on table since row is full.";
        }
        field.addCard(card, rowAffected);
        return null;
    }

    /**
     * Handles the attack command for a card
     * @param attackerCoords The coordinates of the attacking card
     * @param defenderCoords The coordinates of the defender card
     * @return Null on success or an error string on failure
     */
    public String handleAttackCard(Point attackerCoords, Point defenderCoords) {
        genericCard attackerCard = field.getCard(attackerCoords);
        genericCard defenderCard = field.getCard(defenderCoords);
        if (attackerCard == null || defenderCard == null)
            return "Card not found.";

        // Check if the defender is an enemy
        if (!field.isEnemy(defenderCoords, playerTurn))
            return "Attacked card does not belong to the enemy.";

        // Check if the attacker has attacked already this turn
        if (attackerCard.isHasAttacked())
            return "Attacker card has already attacked this turn.";

        if (attackerCard.isFrozen())
            return "Attacker card is frozen.";

        // Main logic of the function
        int attackDealt = attackerCard.attack(defenderCard);
        // If we dealt less damage then normal, then we killed the card
        if (attackDealt < attackerCard.getAttackDamage()) {
            field.removeCard(defenderCoords);
        }

        return null;
    }
}
