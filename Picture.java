/*
 * Picture.java
 *
 * Version:
 *     $Id$
 *
 * Revisions:
 *     $Log$
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

/**
 * A program to create a version of 'Hangman Game', The user has to guess the
 * words to reveal/decode the picture as they progress with the correct guess.
 * It reads a word file to get the words for the game and picture file to read
 * the coded picture. The picture is coded with a '.' character and reveals only
 * the 'n+1'th character in the image. Where 'n' is the length of the string or
 * no number of characters that are yet to be guessed. The game ends as all the
 * words in the word.txt file are chosen randomly and played.
 * 
 * @author Lahari Chepuri
 * @author Smita Subhadarshinee Mishra
 */

public class Picture {

	/**
	 * The main program. This reads the wods.txt and picture.txt file and sends
	 * it to the game.
	 * 
	 * @param args command line arguments (ignored)
	 */

	public static void main( String[] args ) throws FileNotFoundException {

		// These variables will store the file path that is to be read
		File wordsFile;
		File pictureFile;

		// If we read the file path from command line then we store them in our
		// file objects.
		if ( args.length != 0 ) {

			// If the first argument is -words then we take the next argument to
			// be the word file and 3rd argument to be picture file and visa
			// versa.
			if ( args[0].equals( "-words" ) ) {
				wordsFile = new File( args[1] );
				pictureFile = new File( args[3] );
			} else {
				wordsFile = new File( args[3] );
				pictureFile = new File( args[1] );
			}
		} else {
			// We directly read the .txt files in our project
			wordsFile = new File( "words.txt" );
			pictureFile = new File( "hp.txt" );
		}

		// The vector object stores the words that we read from word file
		Vector< String > wordVector = new Vector< String >();

		// The scanner reads the word file line after line.
		Scanner wordScanner = new Scanner( wordsFile );

		while ( wordScanner.hasNextLine() ) {
			wordVector.add( wordScanner.nextLine() );
		}
		wordScanner.close();

		// The variable will store the picture text characters
		String pictureString = new String();

		// The scanner reads the picture file line after line
		Scanner pictureScanner = new Scanner( pictureFile );

		while ( pictureScanner.hasNextLine() ) {
			pictureString += pictureScanner.nextLine() + "\n";
		}
		pictureScanner.close();

		// Call the playGame method
		playGame( wordVector, pictureString );

	}

	/**
	 * Reads a random word from the wordVector and sends for each round of game.
	 * Once all the words in vector are done we end the game.
	 * 
	 * @param wordVector    Contains the array of words for the game.
	 * @param pictureString Contains the Picture in string format.
	 */

	private static void playGame( Vector< String > wordVector,
			String pictureString ) {

		// This variable stores the round number for each game.
		int roundNumber = 0;

		// Stores the number of words in the game
		int wordVectorLength = wordVector.size();

		System.out.println(
				"*****************************************************************" );
		System.out.println( "You will play a total of " + wordVectorLength
				+ " rounds and" );
		System.out.println(
				"You will be allowed to make 9 wrong guesses in each round" );
		System.out.println(
				"*****************************************************************" );

		// Creating an object of Random class to pick the index of the words in
		// random
		Random randomWord = new Random();

		// Play the game until we have words in the vector. Once we have played
		// for all the words, stop the game.
		while ( !wordVector.isEmpty() ) {

			// Stores the index of the word that would go into the next round
			int randomIndex = randomWord.nextInt( wordVector.size() );

			// Stores the word to be guessed in the coming round
			String wordToGuess = wordVector.elementAt( randomIndex );
			roundNumber++;

			System.out
					.println( "       		 		ROUND - " + roundNumber );

			// Call the gameRound for the randomly chosen word
			gameRound( pictureString, wordToGuess );

			// remove the word from the vector so that we don't pick it again
			wordVector.remove( randomIndex );
		}

		// Once all the words are done then stop the game and exit.
		if ( wordVector.isEmpty() ) {
			System.out.println( "No more words left to guess" );
			System.out.println( "I hope you enjoyed the game, bye!" );
		}

	}

	/**
	 * This method is for each round in a game. Where we have a word to be
	 * guessed and as the correct characters are guessed we reveal the picture.
	 * If we have more than 9 wrong guesses the they lose the round and we
	 * progress to the next word.
	 * 
	 * @param pictureString The string that stores the picture in string format.
	 * @param wordToGuess   The word to be guessed in the current round.
	 */

	private static void gameRound( String pictureString, String wordToGuess ) {

		// This holds the number of wrong guesses done for the particular word
		// as we progress.
		int wrongGuess = 0;

		// This holds a copy of the word to be guessed so that we can reserve
		// the original word.
		String requiredWord = wordToGuess.toLowerCase();

		// This holds the number of words that are yet to be guessed, used to
		// keep a track of the number of '.'
		int notGuessedCount = requiredWord.length();

		// Initializing the string with all dots of length of the word as no
		// characters are guessed yet.
		String guessProgress = ".".repeat( notGuessedCount );

		// Call the print image method to print the initial encoded image where
		// no words are guessed.
		printImage( pictureString, notGuessedCount );

		// Will hold the character that the used guessed
		char guess;

		// Creating an object of canner to read the user input for each guess
		Scanner guessSc = new Scanner( System.in );

		// We continue to take the user guess for the word either until all the
		// characters are guessed or until we haven't guessed wrong 9 times.
		while ( wrongGuess < 9 && notGuessedCount > 0 ) {

			// Print the characters in the word that are guessed correct till
			// this iteration.
			System.out.println( wrongGuess + ": " + guessProgress );

			// Take the user input for guess and convert to lower to avoid case
			// sensitivity.
			guess = guessSc.next().toLowerCase().charAt( 0 );

			// Get the index of the guessed character in the word
			int charecterIndex = requiredWord.indexOf( guess );

			// If the guess is correct then the character will be present in the
			// word and hence index > 0
			if ( charecterIndex >= 0 ) {

				// Update the word to print with the character that is guessed
				// correct.
				guessProgress = guessProgress.substring( 0, charecterIndex )
						+ requiredWord.charAt( charecterIndex )
						+ guessProgress.substring( charecterIndex + 1,
								guessProgress.length() );

				// Reduce the number of characters left for guessing, thus
				// reducing the coding of image
				notGuessedCount -= 1;

				// print the decoded image after the guess.
				printImage( pictureString, notGuessedCount );

				// update the word to be guessed by removing the character that
				// is already guessed, to avoid guessing the same word again.
				requiredWord = requiredWord.substring( 0, charecterIndex ) + "*"
						+ requiredWord.substring( charecterIndex + 1,
								requiredWord.length() );
			} else {

				// Increase the number of wrong guesses
				wrongGuess += 1;
				System.out.println( "incorrect guess" );

				// Print the image without decoding any further
				printImage( pictureString, notGuessedCount );
			}
		}
		System.out.println( "The word was: " + wordToGuess );

	}

	/**
	 * This method will print the coded image
	 * 
	 * @param pictureString   The image to be printed.
	 * @param notGuessedCount The number of coding characters. ie. the number of
	 *                        '.' before we reveal a charecter.
	 */

	private static void printImage( String pictureString,
			int notGuessedCount ) {

		// Will store the string to be printed
		String outputString = new String();

		// Keeps a track of the number of coding character '.' printer before we
		// reveal a character
		int codeGapCount = 0;

		// Iterate through the picture characters and hide and show based on the
		// number of coded characters
		for ( int i = 0; i < pictureString.length(); i++ ) {

			// If we encounter a new line in the original string the add a new
			// line to the string to be printed.
			if ( ( pictureString.charAt( i ) ) == ( '\n' ) ) {
				outputString += "\n";
			} else {

				// Print the code '.' as many number of times as the number of
				// words yet to be guessed.
				if ( codeGapCount < notGuessedCount ) {
					outputString += ".";
					codeGapCount += 1;
				} else {
					outputString += pictureString.charAt( i );
					codeGapCount = 0;
				}
			}

		}

		// Print the output image.
		System.out.println( outputString );

	}
}
