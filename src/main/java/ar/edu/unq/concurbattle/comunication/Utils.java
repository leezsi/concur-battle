package ar.edu.unq.concurbattle.comunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import ar.edu.unq.concurbattle.configuration.ConstsAndUtils;
import ar.edu.unq.concurbattle.exception.ConcurbattleRuntimeException;

/**
 * This class contains useful functions that simplify the usage of the Java
 * standard library for beginners.
 */
public class Utils {

	public static long calculateOffset(final long attack, final long deffence) {
		return attack / (attack + deffence);
	}

	/**
	 * Parses a <code>String</code> containing the representation of an integer
	 * number in base 10.
	 * 
	 * @param string
	 *            - a String containing the int representation to be parsed.
	 * 
	 * @return the integer value represented by the argument in decimal.
	 * 
	 * @throws NumberFormatException
	 *             - if the string does not contain a parsable integer.
	 */
	static int parseInt(final String string) {
		return Integer.parseInt(string);
	}

	/** Generates a pseudorandom number between 0.0 and 1.0. */
	public static double random() {
		return Math.random();
	}

	/**
	 * Generates an integer pseudorandom number between <b>low</b> and
	 * <b>up</b>.
	 */
	static int random(final int low, final int up) {
		return (int) ((Math.random() * (up - low)) + low);
	}

	/**
	 * Reads a line from an InputStream.
	 * 
	 * @param inputStream
	 *            an InputStream from where to read the line.
	 * 
	 * @return the String line read from the inputStream.
	 * 
	 * @throws RuntimeException
	 *             wrapping an IOException.
	 */
	public static String readLine(final InputStream inputStream) {
		String result = null;
		try {
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(inputStream));
			result = reader.readLine();
		} catch (final IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return result;
	}

	public static URL resourceURL(final String path) {
		return ClassLoader.getSystemResource(path);
	}

	public static void sleep() {
		try {
			Thread.sleep(ConstsAndUtils.DEFAULT_SLEEP);
		} catch (final InterruptedException e) {
			e.printStackTrace();
			throw new ConcurbattleRuntimeException(e);
		}
	}

}
