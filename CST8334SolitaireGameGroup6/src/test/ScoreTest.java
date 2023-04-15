package test;

import static org.junit.Assert.*;
import org.junit.Test;
import javafx.scene.layout.Pane;
import solitaire.Pile;
import solitaire.Score;

public class ScoreTest {
	
	@Test
	public void testCreateScoreText() {
		Pane root = new Pane();
		Score score = new Score(0, root);
		score.createScoreText();
		assertEquals("Score: 0", score.scoreText.getText());
	}
	
	@Test
	public void testAddScore() {
		Pane root = new Pane();
		Score score = new Score(0, root);
		score.addScore(10);
		assertEquals(10, score.getCurrentScore());
	}
	
	@Test
	public void testSetScore() {
		Pane root = new Pane();
		Score score = new Score(0, root);
		score.setScore(20);
		assertEquals(20, score.getCurrentScore());
	}
	
	@Test
	public void testGetCurrentScore() {
		Pane root = new Pane();
		Score score = new Score(0, root);
		assertEquals(0, score.getCurrentScore());
	}
	
	@Test
	public void testSetCurrentScore() {
		Pane root = new Pane();
		Score score = new Score(0, root);
		score.setCurrentScore(30);
		assertEquals(30, score.getCurrentScore());
		assertEquals("Score: 30", score.scoreText.getText());
	}
	
	@Test
	public void testClearScore() {
		Pane root = new Pane();
		Score score = new Score(0, root);
		score.addScore(10);
		score.clearScore();
		assertEquals(0, score.getCurrentScore());
	}
	
	@Test
	public void testUpdateScore() {
		Pane root = new Pane();
		Score score = new Score(0, root);
		score.updateScore(10);
		assertEquals(10, score.getCurrentScore());
		assertEquals("Score: 0", score.scoreText.getText());
	}
	
	@Test
	public void testCalculateScore() {
		assertEquals(100, Score.calculateScore(90));
		assertEquals(200, Score.calculateScore(40));
	}
	
}
