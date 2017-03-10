package engine;

public class ScoreKeeper {
	/*
	 * Score keeper - Keeps track of scores in the engine on the server side.
	 */
	private static class ScoreKeeperSingletonHolder {
		public static final ScoreKeeper instance = new ScoreKeeper();
	}

	public static ScoreKeeper getInstance() {
		return ScoreKeeperSingletonHolder.instance;
	}

	private static int[] m_scores = { 0, 0 };

	public void changeScores(int deltaTeam1, int deltaTeam2) {
		m_scores[0] += deltaTeam1;
		m_scores[1] += deltaTeam2;
	}

	private ScoreKeeper() {
	}

	public static int[] getScores() {
		return m_scores;
	}
}