package solitaire;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.*;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.stage.Window;
import solitaire.Pile.PileType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import solitaire.Pile;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

/**
 * This is the controller class that consists all the method to play the game
 * @author Jia Hong
 */
public class Game extends Pane {

	private List<Card> deck;

    private Pile stockPile;
    private Pile discardPile;
    private List<Pile> foundationPiles = FXCollections.observableArrayList();
    private List<Pile> tableauPiles = FXCollections.observableArrayList();

    private double dragStartX, dragStartY;
    private List<Card> draggedCards = FXCollections.observableArrayList();

    private static double STOCK_GAP = 0.5;
    private static double FOUNDATION_GAP = 0;
    private static double TABLEAU_GAP = 30;
    private static double HIDDEN_GAP = 0;
    
    private int numMoves = 0;  //keeps track of the number of moves made by the player
    private Score score; //instance of the 'Score' class, which keeps track of the game score.
     

    private static Game instance; //declares a private static variable

    public Game() {
       deck = Card.createNewDeck();
        initPiles();
        dealCards();
        //creates a new instance of the Score class and assigns it to the score variable
        score = new Score(numMoves, discardPile);
        //call displayScore() on the score object to display the current score of the game
        score.displayScore();
    }

    /**
     * Returns the singleton instance of the Game class.
     * If the instance has not yet been created, this method creates it in a thread-safe way.
     * @return The singleton instance of the Game class.
     */
    public static Game getInstance() {
        if (instance == null) {
            synchronized (Game.class) {
                if (instance == null) {
                    instance = new Game();
                }
            }
        }
        return instance;
    }

    /**
     * Initializes the game.
     * This method sets up the game state, including creating a new deck of cards, initializing the piles,
     * and dealing the cards. It should be called once at the beginning of each game.
     */
    public void initialize() {
    }
    
    
 // a method to update the score
    public void updateScore(int newScore) {
        if (score != null) {
            score.setCurrentScore(newScore);
        }
    }
    
//    public void displayScore() {
//        if (score != null) {
//            score.displayScore();
//        }
//    }

	private EventHandler<MouseEvent> onMouseClickedHandler = e -> {
        Card card = (Card) e.getSource();
        /*The event handler first checks if the mouse click is a double click 
         * and if the clicked card is the top card of its containing pile 
         * and is not in the stock pile. If these conditions are true, 
         * then it attempts to find a possible move for the card by calling the possibleMove method
         */
        if (e.getClickCount() == 2 && card == card.getContainingPile().getTopCard() &&
                !card.getContainingPile().getPileType().equals(Pile.PileType.STOCK)) {
            Pile pileToMove = possibleMove(card);

            if (pileToMove != null) {
                draggedCards.add(card);
                handleValidMove(card, pileToMove);
                numMoves++; //score
            }
            draggedCards.clear();
            return;
        }
        /* If the clicked card is in the stock pile, then the top card of the stock pile is moved to the discard pile, 
         * flipped over if it was face down, and its mouseTransparent property is set to false. 
         * Finally, a message is printed to the console indicating that the card was placed in the waste pile.
         */
        if (card != null && card.getContainingPile().getPileType() == Pile.PileType.STOCK) {
            card = card.getContainingPile().getTopCard();
            card.moveToPile(discardPile);
            if (card.isFaceDown()) {
                card.flip();
            }
            card.setMouseTransparent(false);
            System.out.println("Placed " + card + " to the waste.");
        }
    };

	private Card[] cards;

    private EventHandler<MouseEvent> stockReverseCardsHandler = e -> {
        if (stockPile.isEmpty()) {
//            refillStockFromDiscard();
        	List<Card> cardsToMove = new ArrayList<>(discardPile.getCards());
        	for (Card card : cardsToMove) {
                card.flip();
                card.moveToPile(stockPile);
            }
            discardPile.clear();
            Collections.shuffle(deck);
            for (Card card : deck) {
                stockPile.addCard(card);
            }
            System.out.println("Reset stock pile");
       
        }
    };
    /* retrieves the x and y coordinates of the mouse press event using 
     * the getSceneX() and getSceneY() methods of the MouseEvent object e. 
     */
    private EventHandler<MouseEvent> onMousePressedHandler = e -> {
        dragStartX = e.getSceneX();
        dragStartY = e.getSceneY();
    };
    
    /* retrieves the Card object that corresponds to the node that triggered the mouse drag event 
     * using the getSource() method of the MouseEvent object e. 
     * It then retrieves the Pile object that contains the card using the getContainingPile() method of the Card object
     */
    private EventHandler<MouseEvent> onMouseDraggedHandler = e -> {
        Card card = (Card) e.getSource();
        Pile activePile = card.getContainingPile();
        // draggedCards list is then cleared to prepare for storing any cards that are currently being dragged
        draggedCards.clear();
        
        /* calculates the offset between the initial position of the mouse cursor when the drag operation started 
         * and the current position of the mouse cursor using the getSceneX() and getSceneY() methods of the MouseEvent object e.
         */
        double offsetX = e.getSceneX() - dragStartX;
        double offsetY = e.getSceneY() - dragStartY;

        draggedCards.clear();
        
        
        if (activePile.getPileType() == Pile.PileType.STOCK ||
                ((activePile.getPileType() == Pile.PileType.DISCARD ||
                        activePile.getPileType() == Pile.PileType.FOUNDATION) &&
                        card != activePile.getTopCard())) {
            return;
        } else if (activePile.getPileType() == Pile.PileType.TABLEAU && !card.isFaceDown()) {
            draggedCards.addAll(activePile.getCards());
            for (Card activePileCard : activePile.getCards()) {
                if (activePileCard.equals(card)) {
                    break;
                }
                draggedCards.remove(activePileCard);
            }

            offsetX = e.getSceneX() - dragStartX - 50;
            offsetY = e.getSceneY() - dragStartY - 50;
            for (Card draggedCard : draggedCards) {

                draggedCard.getDropShadow().setOffsetX(0);
                draggedCard.getDropShadow().setOffsetY(0);

                draggedCard.setTranslateX(offsetX);
                draggedCard.setTranslateY(offsetY);
                draggedCard.relocate(dragStartX, dragStartY);
            }
        } else if (!card.isFaceDown()) {
            draggedCards.add(card);
        } else {
            return;
        }

        card.getDropShadow().setRadius(20);
        card.getDropShadow().setOffsetX(10);
        card.getDropShadow().setOffsetY(10);

        card.toFront();
        card.setTranslateX(offsetX);
        card.setTranslateY(offsetY);
    };

    private EventHandler<MouseEvent> onMouseReleasedHandler = e -> {
        if (draggedCards.isEmpty())
            return;
        Card card = (Card) e.getSource();
        List<Pile> bothPiles = new ArrayList<>(tableauPiles);
        Pile pile = getValidIntersectingPile(card, bothPiles);
        if (draggedCards.size() > 1) {
            Pile additionalPile = new Pile(Pile.PileType.HIDDEN, "", HIDDEN_GAP);
            if (isMoveValid(draggedCards.get(0), pile)) {
                moveCardsToPile(pile);
            } else {
                moveCardsToPile(additionalPile);
                draggedCards.clear();
                draggedCards.addAll(additionalPile.getCards());
                moveCardsToPile(pile);
            }
        } else {
            bothPiles.addAll(foundationPiles);
            pile = getValidIntersectingPile(card, bothPiles);
            if (pile != null) {
                handleValidMove(card, pile);
            } else {
                draggedCards.forEach(MouseUtil::slideBack);
                draggedCards.clear();
            }
        }
        draggedCards.clear();
        if (pile.getPileType().equals(Pile.PileType.FOUNDATION)) {
            if (isGameWon() && pile.numOfCards() == 12) showModal("Congratulations!");
        }
    };

    private void moveCardsToPile(Pile pile){
        for (Card draggedCard : draggedCards) {
            draggedCard.moveToPile(pile);
        }
    }

	public boolean isGameWon() {
        int s = 0;
        for (Pile pile: foundationPiles) {
            if (pile.numOfCards() == 13) s++;
            if (s == 3) return true;
        }
        return false;
    }

    private void showModal(String msg) {
        final Stage dialog = new Stage();
        Text text = new Text(msg);
        Scene dialogScene;

        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        text.setStyle("-fx-font: 24 arial;");
        dialogVbox.getChildren().add(text);
        dialogScene = new Scene(dialogVbox, 250, 50);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void addMouseEventHandlers(Card card) {
        card.setOnMousePressed(onMousePressedHandler);
        card.setOnMouseDragged(onMouseDraggedHandler);
        card.setOnMouseReleased(onMouseReleasedHandler);
        card.setOnMouseClicked(onMouseClickedHandler);
    }

    public void refillStockFromDiscard() {
        stockPile.clear();
        /*This puts the cards back in their original order, 
         * so that they can be added to the stockPile in the correct sequence.
         */
        Collections.reverse(discardPile.getCards());
        for (Card card : discardPile.getCards()) {
            stockPile.addCard(card);
            if (!card.isFaceDown()) {
                card.flip();
            }
        }
        discardPile.clear();
        System.out.println("Stock refilled from discard pile.");
    }

    public boolean isMoveValid(Card card, Pile destPile) {
        if (destPile.getPileType().equals(Pile.PileType.TABLEAU)) {
            if (destPile.isEmpty()) {
                return card.getRank() == 13;
            } else {
                return (Card.isOppositeColor(card, destPile.getTopCard()) &&
                        Card.isHigherRank(destPile.getTopCard(), card));
            }
        } else if (destPile.getPileType().equals(Pile.PileType.FOUNDATION)) {
            if (destPile.isEmpty()) {
                return card.getRank() == 1;
            } else {
                return (Card.isSameSuit(card, destPile.getTopCard()) &&
                        Card.isHigherRank(card, destPile.getTopCard()));
            }
        }
        return false;
    }

    private Pile getValidIntersectingPile(Card card, List<Pile> piles) {
        Pile result = card.getContainingPile();
        for (Pile pile : piles) {
            if (!pile.equals(card.getContainingPile()) &&
                    isOverPile(card, pile) &&
                    isMoveValid(card, pile))
                result = pile;
        }
        return result;
    }

    private boolean isOverPile(Card card, Pile pile) {
        if (pile.isEmpty())
            return card.getBoundsInParent().intersects(pile.getBoundsInParent());
        else
            return card.getBoundsInParent().intersects(pile.getTopCard().getBoundsInParent());
    }

    private void handleValidMove(Card card, Pile destPile) {
        String msg = null;
        if (destPile.isEmpty()) {
            if (destPile.getPileType().equals(Pile.PileType.FOUNDATION))
                msg = String.format("Placed %s to the foundation.", card);
            if (destPile.getPileType().equals(Pile.PileType.TABLEAU))
                msg = String.format("Placed %s to a new pile.", card);
        } else {
            msg = String.format("Placed %s to %s.", card, destPile.getTopCard());
        }
        System.out.println(msg);
        MouseUtil.slideToDest(draggedCards, destPile);
        draggedCards.clear();
//        if (destPile.equals(Pile.PileType.FOUNDATION)) {
        if (destPile.getPileType().equals(Pile.PileType.FOUNDATION)) {
            if (isGameWon()) showModal("Congratulations!");

        }
        
     // Searches through the tableau piles to find the pile that contains the specified card.
     // If the card is found, sets the sourcePile variable to the pile containing the card.
        Pile sourcePile = null;
        for (Pile pile : tableauPiles) {
            if (pile.getTopCard() != null && pile.getTopCard().equals(card)) {
                sourcePile = pile;
                break;
            }
        }
        
        /**
        Checks if the sourcePile is null and if the top card of the discard pile equals to the card
        being dragged. If both conditions are met, sets the sourcePile as the discard pile.
        @param card The card being dragged
        @param discardPile The discard pile where the card is being dragged from
        */
        if (sourcePile == null && discardPile != null && discardPile.getTopCard() != null && discardPile.getTopCard().equals(card)) {
            sourcePile = discardPile;
        }

//        if (sourcePile != null && sourcePile.getPileType().equals(Pile.PileType.TABLEAU)) {
//            score.updateScore(-5);
//        }   
        

        
        /**
        Checks if the sourcePile is null and if the discard pile is not null and the top card of the discard pile
        equals to the card being dragged. If both conditions are met, sets the sourcePile as the discard pile.
        @param card The card being dragged
        @param discardPile The discard pile where the card is being dragged from
        */
        if (sourcePile == null && discardPile != null && discardPile.getTopCard() != null && discardPile.getTopCard().equals(card)) {
            sourcePile = discardPile;
        }

        //initialize an integer variable slideToDest to the value of 1.
        int slideToDest = 1;
        
        // call a method getMoveType() on an instance of the Score class, passing in three arguments sourcePile, destPile, and slideToDest. 
        //The returned integer value is stored in a variable moveType.
        int moveType = score.getMoveType(sourcePile, destPile, slideToDest);
        

        
        //updates the score based on the move that was made.
        score.updateScore(sourcePile, destPile, draggedCards, moveType);
        
        //calls a method displayScore() on the same Score instance, 
        //which displays the current score to the user.
        score.displayScore();
     
    }

  
    private void initPiles() {
        stockPile = new Pile(Pile.PileType.STOCK, "Stock", STOCK_GAP);
        stockPile.setWhiteStrokeBackground();
        stockPile.setLayoutX(95);
        stockPile.setLayoutY(20);
        stockPile.setOnMouseClicked(stockReverseCardsHandler);
        getChildren().add(stockPile);

        discardPile = new Pile(Pile.PileType.DISCARD, "Discard", STOCK_GAP);
        discardPile.setWhiteStrokeBackground();
        discardPile.setLayoutX(230);
        discardPile.setLayoutY(20);
        getChildren().add(discardPile);

        Button restartBtn = new Button("Restart");
        //restartBtn.setStyle("-fx-font: 18 arial; -fx-base: #666666;");
        restartBtn.setStyle("-fx-font: 16 arial; -fx-background-color: #ffffff; -fx-background-radius: 20; -fx-text-fill: #000000;");

        restartBtn.setLayoutX(5); // X coordinate
        restartBtn.setLayoutY(20); // Y coordinate
        getChildren().add(restartBtn);
        restartBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                restart();
                             
            }
        });

        Button exitBtn = new Button("   Exit   ");
        exitBtn.setStyle("-fx-font: 16 arial; -fx-background-color: #ffffff; -fx-background-radius: 20; -fx-text-fill: #000000;");
        exitBtn.setLayoutX(5);
        exitBtn.setLayoutY(80);
        getChildren().add(exitBtn);
        exitBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
            	Platform.exit();
            }
        });

        for (int i = 0; i < 4; i++) {
            Pile foundationPile = new Pile(Pile.PileType.FOUNDATION, "Foundation " + i, FOUNDATION_GAP);
            //foundationPile.setBlurredBackground();
            foundationPile.setWhiteStrokeBackground();
            //foundationPile.setLayoutX(610 + i * 180);
            foundationPile.setLayoutX(500 + i * 125);
            foundationPile.setLayoutY(20);
            foundationPiles.add(foundationPile);
            getChildren().add(foundationPile);
        }
        //Iterator<Card> deckIterator = deck.iterator();
        for (int i = 0; i < 7; i++) {
            Pile tableauPile = new Pile(Pile.PileType.TABLEAU, "Tableau " + i, TABLEAU_GAP);
            tableauPile.setWhiteStrokeBackground();
            //tableauPile.setLayoutX(95 + i * 180);
            tableauPile.setLayoutX(95 + i * 130);
            tableauPile.setLayoutY(275);
            tableauPiles.add(tableauPile);
            getChildren().add(tableauPile);
        }
    }

    public void dealCards() {
    	//the index of the tableau pile that the current card will be added to
        int numberOfPile = 0;
        //the index of the current card in the deck
        int numberOfCard = 0;
        //index of the first tableau pile
        int startPile = 0;
        //the number of cards that will be dealt to the tableau piles
        int countOfCardsToDeal = 27;
        int countOfPiles = 7;

        for (Card card : deck) {
            if (numberOfCard > countOfCardsToDeal) {
                stockPile.addCard(card);
                addActionToCard(card);
            } else {
                tableauPiles.get(numberOfPile).addCard(card);
                addActionToCard(card);
                numberOfPile++;

                if (numberOfPile == countOfPiles) {
                    startPile++;
                    numberOfPile = startPile;
                }
            }
            
            /*If numberOfPile is equal to startPile + 1, or if numberOfCard is equal to countOfCardsToDeal, 
             * flip the card using the flip() method. This flips over the first card in each tableau pile and the last card in the stock pile
             */
            if (numberOfPile == startPile + 1 || numberOfCard == countOfCardsToDeal)
                card.flip();

            numberOfCard++;
        }
    }

    private Card addActionToCard(Card card) {
        addMouseEventHandlers(card);
        getChildren().add(card);
        return card;
    }

    public void setTableBackground(Image tableBackground) {
        setBackground(new Background(new BackgroundImage(tableBackground,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    }

 

    private void restart() {
        clearPane();
        deck = Card.createNewDeck();
        initPiles();
        dealCards();
        score = new Score(numMoves, discardPile);//scoring
        score.displayScore();
        
   }

    private void clearPane() {
        stockPile.clear();
        discardPile.clear();
        foundationPiles.clear();
        tableauPiles.clear();
        this.getChildren().clear();
        
    }

    private Pile possibleMove(Card card) {
        for (Pile pile: foundationPiles)
            if(isMoveValid(card, pile)) {
                return pile;
            }
        for (Pile pile: tableauPiles)
            if(isMoveValid(card, pile)) {
                return pile;
            }
        return null;
    }



	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}
}
