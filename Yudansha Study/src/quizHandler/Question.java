package quizHandler;

public class Question {
	private String moveID;
	private String whatIs;

	public Question(String id, String q) {
		setMoveID(id);
		whatIs = q;
	}

	public String getMoveID() {
		return moveID;
	}

	public void setMoveID(String moveID) {
		this.moveID = moveID;
	}

	public String getWhatIs() {
		return whatIs;
	}

	public void setWhatIs(String whatIs) {
		this.whatIs = whatIs;
	}

	public String getCorrectAnswer() {
		return whatIs;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.whatIs = correctAnswer;
	}

	public boolean checkAnswer(String answer) {
		if (whatIs.equals(answer))
			return true;
		else
			return false;
	}
}
