package solitaire;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.*;

/**
 * This is a model class that contains the suit, rank and color. 
 * @author Jia Hong
 */
public class Card extends ImageView {

    private int suit;
    private int rank;
    private boolean faceDown;

    private Image backFace;
    private Image frontFace;
    private Pile containingPile;
    private DropShadow dropShadow;
 
    static Image cardBackImage;
    private static final Map<String, Image> cardFaceImages = new HashMap<>();
    public static final int WIDTH = 100;
    public static final int HEIGHT = 145;

    public Card(int suit, int rank, boolean faceDown) {
        this.suit = suit;
        this.rank = rank;
        this.faceDown = faceDown;
        this.dropShadow = new DropShadow(2, Color.gray(0, 0.75));
        

        backFace = cardBackImage;
        frontFace = cardFaceImages.get(getShortName());
        //If faceDown is true, then the backFace image is displayed, otherwise, the frontFace image is displayed
        setImage(faceDown ? backFace : frontFace);
        setEffect(dropShadow);
    }

    public int getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    public boolean isFaceDown() {
        return faceDown;
    }

    public String getShortName() {
        return "S" + suit + "R" + rank;
    }

    public DropShadow getDropShadow() {
        return dropShadow;
   }

    public Pile getContainingPile() {
        return containingPile;
    }

    public void setContainingPile(Pile containingPile) {
        this.containingPile = containingPile;
    }
    //move a card from its current pile to a specified destination pile
    public void moveToPile(Pile destPile) {
    	//gets the current pile containing the card and stores it in the myPile variable
        Pile myPile = this.getContainingPile();
        //removes the card from its current pile using the remove method
        this.getContainingPile().getCards().remove(this);
        //added to the destination pile using the addCard method
        destPile.addCard(this);
        if (!myPile.isEmpty()) {
            if (myPile.getTopCard().isFaceDown() &&
                    myPile.getPileType().equals(Pile.PileType.TABLEAU) &&
                    !destPile.getPileType().equals(Pile.PileType.HIDDEN)) {
                myPile.getTopCard().flip();
            }
        }
    }

//    public void autoflip() {
//        if(this.getContainingPile().getTopCard() != null &&
//                this.getContainingPile().getPileType().equals(Pile.PileType.TABLEAU) &&
//                !this.getContainingPile().getTopCard().isFaceDown()) {
//            this.getContainingPile().getTopCard().flip();
//        }
//    }

    public void flip() {
        faceDown = !faceDown;
        setImage(faceDown ? backFace : frontFace);
    }

    @Override
    public String toString() {
        return "The " + "Rank" + rank + " of " + "Suit" + suit;
    }

    public static boolean isOppositeColor(Card card1, Card card2) {
        String cardColor1 = getCardColor(card1);
        String cardColor2 = getCardColor(card2);

        return !cardColor1.equals(cardColor2);
    }

    public static  String getCardColor(Card card) {
        if(card.getSuit() == 1 || card.getSuit() == 2) {
            return "red";
        } else {
            return "black";
        }
    }

    public static boolean isHigherRank(Card card1, Card card2) {
        return card1.getRank() == card2.getRank() + 1;
    }

    public static boolean isSameSuit(Card card1, Card card2) {
        return card1.getSuit() == card2.getSuit();
    }

    public static List<Card> createNewDeck() {

        List<Card> result = new ArrayList<>();
        for (int suit = 1; suit < 5; suit++) {
            for (int rank = 1; rank < 14; rank++) {
                result.add(new Card(suit, rank, true));
            }
        }
        Collections.shuffle(result);
        return result;
    }

    public static void loadCardImages() {
        //cardBackImage = new Image("resources/card_images/card_back.png");
    	cardBackImage = new Image("resources/card_images/cardback.jpg", 100, 150, true, true);


        String suitName = "";
        for (int suit = 1; suit < 5; suit++) {
            switch (suit) {
                case 1:
                    suitName = "HEARTS";
                    break;
                case 2:
                    suitName = "DIAMONDS";
                    break;
                case 3:
                    suitName = "SPADES";
                    break;
                case 4:
                    suitName = "CLUBS";
                    break;
            }
            for (int rank = 1; rank < 14; rank++) {
                String cardName = suitName + rank;
                String cardId = "S" + suit + "R" + rank;
                String imageFileName = "resources/card_images/" + cardName + ".png";
                //cardFaceImages.put(cardId, new Image(imageFileName));
                cardFaceImages.put(cardId, new Image(imageFileName, 100, 145, true, true));

            }
        }
    }

}
