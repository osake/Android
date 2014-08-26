package com.eftimoff;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.io.Serializable;
import java.util.List;

@Entity
public class Question implements Serializable {

	@Id
	private String id;
	private String question;
	private List<String> answers;
	private int rightAnswer;

	public int getRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(int rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
