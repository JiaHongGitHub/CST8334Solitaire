package solitaire;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This class represents a pile of cards in a card game. 
 * @author Jia Hong
 */
public class Pile extends Pane {
    //specifies the type of the pile, such as "stock"
    private PileType pileType;
    //name of the pile
    private String name;
    //gap between each card in the pile
    private double cardGap;
    //objects that represents the cards in the pile
    private ObservableList<Card> cards = FXCollections.observableArrayList();
    //constructor
    public Pile(PileType pileType, String name, double cardGap) {
        this.pileType = pileType;
        this.cardGap = cardGap;
    }

    public PileType getPileType() {
        return pileType;
    }

    public String getName() {
        return name;
    }

    public double getCardGap() {
        return cardGap;
    }

    public ObservableList<Card> getCards() {
        return cards;
    }

    public int numOfCards() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void clear() {
        cards.clear();
    }
    //adds a Card object to the pile and updates its layout
    public void addCard(Card card) {
        cards.add(card);
        card.setContainingPile(this);
        card.toFront();
        layoutCard(card);
    }
    //lays out a Card object in the pile according to its position and size
    private void layoutCard(Card card) {
        card.relocate(card.getLayoutX() + card.getTranslateX(), card.getLayoutY() + card.getTranslateY());
        card.setTranslateX(0);
        card.setTranslateY(0);
        card.setLayoutX(getLayoutX());
        card.setLayoutY(getLayoutY() + (cards.size() - 1) * cardGap);
    }

    public Card getTopCard() {
        if (cards.isEmpty())
            return null;
        else
            return cards.get(cards.size() - 1);
    }

    //sets a white stroke border around the pile
    public void setWhiteStrokeBackground() {
        setPrefSize(Card.WIDTH, Card.HEIGHT);
        BorderStroke borderStroke = new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2));
        Border border = new Border(borderStroke);
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        setBorder(border);
    }


    public enum PileType {
        STOCK,
        DISCARD,
        FOUNDATION,
        TABLEAU,
        HIDDEN
    }
}
