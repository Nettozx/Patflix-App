package edu.uci.ics.android;

public class Globals {
	
	private static Globals instance;
	private int questions = 0;
	private int correct = 0;
	private int numOfQuizzes = 0;
	
	private Globals(){}
	
	public void setQuestions(int newQuestions)
	{
		questions = questions + newQuestions;
	}

	public void setCorrect(int newCorrects)
	{
		correct = correct + newCorrects;
	}
	
	public void setNumOfQuizzes(int newQuizzes)
	{
		numOfQuizzes = numOfQuizzes + newQuizzes;
	}
	
	public int getQuestions()
	{
		return questions;
	}
	
	public int getCorrects()
	{
		return correct;
	}
	
	public int getNumOfQuizzes()
	{
		return numOfQuizzes;
	}
	
	public static synchronized Globals getInstance(){
	     if(instance==null){
	       instance=new Globals();
	     }
	     return instance;
	   }
}
