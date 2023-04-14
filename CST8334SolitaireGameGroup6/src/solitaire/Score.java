package solitaire;

import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
The Score class represents the score of the Solitaire game and manages its updating and displaying.
It also includes methods for determining the score based on the move type and updating the score
accordingly.
*/
public class Score {
	private int currentScore; // current score of the game
	private Text scoreText = new Text("Score: " + currentScore); // Text object to display the score
	private Pane root; // the root pane of the game
	private static final int FLIP = 0; // move types
	private static final int TABLEAU_TO_FOUNDATION = 1;
	private static final int FOUNDATION_TO_TABLEAU = 2;
	private static final int TABLEAU_TO_TABLEAU = 3;
	private static final int WASTE_TO_FOUNDATION = 4;
	private static final int WASTE_TO_TABLEAU = 5;
	private static final int INVALID_MOVE = 0;// invalid move type


	/**
	Creates a Score object with the given initial score and root pane of the game.
	@param initialScore the initial score of the game
	@param root the root pane of the game
	*/
    public Score(int initialScore, Pane root) {
        this.currentScore = initialScore;
        this.root = root;
        createScoreText();
        displayScore();
    }

    /**
    Creates a Text object to display the score with the current score value and adds it to the root pane.
    */
    public void createScoreText() {
    	scoreText.setText("Score: " + currentScore);
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        scoreText.setTranslateX(150);
        scoreText.setTranslateY(20);
     }
 

    /**
    Adds the given amount to the current score.
    @param amount the amount to add to the current score
    */
    public void addScore(int amount) {
        currentScore += amount;
    }

    /**
    Sets the current score to the given score.
    @param score the score to set
    */
    public void setScore(int score) {
        this.currentScore = score;
    }
    
    /**
    Returns the current score of the game.
    @return the current score of the game
    */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
    Sets the current score to the given score and updates the score display.
    @param newScore the new score to set
    */
    public void setCurrentScore(int newScore) {
        currentScore = newScore;
        scoreText.setText("Score: " + currentScore);
    }

    /**
    Sets the current score to 0.
    */
    public void clearScore() {
        currentScore = 0;
    }

    /**
    Adds the given points to the current score and updates the score display.
    @param points the points to add to the current score
    */
    public void updateScore(int points) {
        currentScore += points;
    }
    
    /**
    Calculates the score for a move based on the number of moves made so far in the game.
    @param moves the number of moves made so far in the game
    @return the score for the move
    */
    public static int calculateScore(int moves) {
        return (10000 / (moves + 10));
    }

    /**
    Displays the score text on the root pane if it is not already displayed.
    */
    public void displayScore() {
            
        if (scoreText != null && !root.getChildren().contains(scoreText)) {
                root.getChildren().add(scoreText);
        }
        createScoreText();
    }


	// Update score based on move type
    public void updateScore(Pile sourcePile, Pile destinationPile, List<Card> draggedCards, int slideToDest) {
        int moveType = getMoveType(sourcePile, destinationPile, slideToDest);
        switch (moveType) {
            case FLIP:
                addScore(0);
                break;
            case TABLEAU_TO_FOUNDATION:
                addScore(10);
                break;
            case FOUNDATION_TO_TABLEAU:
                addScore(0);  
                break;
            case TABLEAU_TO_TABLEAU:
                addScore(5);
                break;
            case WASTE_TO_FOUNDATION:
                addScore(10);
                break;
            case WASTE_TO_TABLEAU:
                addScore(5);
                break;
        }
        displayScore();
    }

    // Determine the move type based on the source and destination piles
    public int getMoveType(Pile sourcePile, Pile destinationPile, int slideToDest) {
        if (sourcePile == null || destinationPile == null) {
            return INVALID_MOVE;
        }
        if (sourcePile.getPileType() == Pile.PileType.TABLEAU && destinationPile.getPileType() == Pile.PileType.TABLEAU) {
            return TABLEAU_TO_TABLEAU;
        } else if (sourcePile.getPileType() == Pile.PileType.DISCARD && destinationPile.getPileType() == Pile.PileType.TABLEAU) {
            return WASTE_TO_TABLEAU;
        } else if (sourcePile.getPileType() == Pile.PileType.DISCARD && destinationPile.getPileType() == Pile.PileType.FOUNDATION) {
            return WASTE_TO_FOUNDATION;
        } else if (sourcePile.getPileType() == Pile.PileType.TABLEAU && destinationPile.getPileType() == Pile.PileType.FOUNDATION) {
            return TABLEAU_TO_FOUNDATION;
        } else if (sourcePile.getPileType() == Pile.PileType.FOUNDATION && destinationPile.getPileType() == Pile.PileType.TABLEAU) {
            return FOUNDATION_TO_TABLEAU;
        } else if (slideToDest == 1 && destinationPile.getPileType() == Pile.PileType.FOUNDATION) {
            return TABLEAU_TO_FOUNDATION;
        } else {
            // The move type is not recognized
            return INVALID_MOVE;
        }
    }


        /**
         * Executes a move from a source pile to a destination pile.
         * @param sourcePile      the source pile
         * @param destinationPile the destination pile
         * @param slideToDest     whether to slide cards to the destination pile
         * @return true if the move was successful, false otherwise
         */
        public boolean move(Pile sourcePile, Pile destinationPile, boolean slideToDest) {
            // Determine the move type
            int moveType = getMoveType(sourcePile, destinationPile, slideToDest);

            // Execute the move based on the move type
            switch (moveType) {
                case TABLEAU_TO_TABLEAU:
                    return moveTableauToTableau(sourcePile, destinationPile, slideToDest);
                case WASTE_TO_TABLEAU:
                    return moveWasteToTableau(sourcePile, destinationPile);
                case WASTE_TO_FOUNDATION:
                    return moveWasteToFoundation(destinationPile);
                case TABLEAU_TO_FOUNDATION:
                    return moveTableauToFoundation(sourcePile, destinationPile);
                case FOUNDATION_TO_TABLEAU:
                    return moveFoundationToTableau(sourcePile, destinationPile);
                default:
                    return false;
            }
        }

		private boolean moveTableauToTableau(Pile sourcePile, Pile destinationPile, boolean slideToDest) {
			// TODO Auto-generated method stub
			return false;
		}

		private int getMoveType(Pile sourcePile, Pile destinationPile, boolean slideToDest) {
			// TODO Auto-generated method stub
			return 0;
		}

		private boolean moveFoundationToTableau(Pile sourcePile, Pile destinationPile) {
			// TODO Auto-generated method stub
			return false;
		}

		private boolean moveTableauToFoundation(Pile sourcePile, Pile destinationPile) {
			// TODO Auto-generated method stub
			return false;
		}

		private boolean moveWasteToFoundation(Pile destinationPile) {
			// TODO Auto-generated method stub
			return false;
		}

		private boolean moveWasteToTableau(Pile sourcePile, Pile destinationPile) {
			// TODO Auto-generated method stub
			return false;
		}
  
}
           

