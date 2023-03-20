//package solitaire;
//
//import java.util.TimerTask;
//
//public class ScoreClock extends TimerTask {
//	public ScoreClock() {
//	}
//
//	public void run() {
//		this.updateTimer();
//	}
//
//	void updateTimer() {
//		++Game.time;
//		if (Game.time % 10 == 0) {
//			Game.updateScore(Game.score, Game.scoreSubtracter);
//		}
//		String writeTime = "Seconds: " + Game.time;
//		Game.timeBox.setText(writeTime);
//		Game.timeBox.repaint();
//	}
//}


