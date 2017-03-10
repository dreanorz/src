package gui;

import shared.Const;

/* Minimal GUI representation for the score keeping */
public class ScoreKeeperRepresentation {
	private static class ScoreKeeperSingletonHolder {
		public static final ScoreKeeperRepresentation instance = new ScoreKeeperRepresentation();
	}

	public static ScoreKeeperRepresentation getInstance() {
		return ScoreKeeperSingletonHolder.instance;
	}

	private int m_team1Score;
	private int m_team2Score;

	public void updateScore(int team1, int team2) {
		m_team1Score = team1;
		m_team2Score = team2;
	}

	private ScoreKeeperRepresentation() {
	}

	public void render(java.awt.Graphics g) {
		g.setFont(Const.SCORE_FONT);
		g.setColor(Const.TEAM1_COLOR);
		g.drawString(new Integer(m_team1Score).toString(), (int) Const.TEAM1_SCORE_TEXT_POSITION.getX(),
				(int) Const.TEAM1_SCORE_TEXT_POSITION.getY());

		g.setColor(Const.TEAM2_COLOR);
		g.drawString(new Integer(m_team2Score).toString(), (int) Const.TEAM2_SCORE_TEXT_POSITION.getX(),
				(int) Const.TEAM2_SCORE_TEXT_POSITION.getY());
	}
}