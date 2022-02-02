package Logic;

import java.awt.Point;
import java.security.SecureRandom;
import java.util.List;
import Tools.Tool;

//Represent the tools type and location via 64 bit hash key.

public class Zobrist {
	
	public static long[][] keys = new long[64][16];
	public static long hash;
	
	//Initialize zobrist keys 
	//Each square has key for each tool assignment.
	public static void initiateKeys() {
		for(int x = 0; x < 64; x++) {
			for(int y = 0; y < 16; y++) {
				keys[x][y] = random64();
			}
		}
	}
	
	//Returns hash that represents the current board
	public static long getHash(List<Tool> whiteTools, List<Tool> blackTools) {
		hash =  getHashOfArray(whiteTools) ^ getHashOfArray(blackTools);
		return hash;
	}
	
	//Returns new hash - XOR with old value and with new value.
	public static long getHash(Point srcLocation, Point dstLocation, int id) {
		hash ^= keys[(srcLocation.x - 2)* 8 + (srcLocation.y - 2)][id - 1];
		hash ^= keys[(dstLocation.x - 2)* 8 + (dstLocation.y - 2)][id - 1];
		return hash;
	}
	
	//There is a key for each tool assignment on the board.
	//This function make XOR between all those values and returns the result.
	private static long getHashOfArray(List<Tool> tools) {
		long returnedHash = 0;
		for(Tool t: tools) {
			Point location = t.getLocation();
			returnedHash ^= keys[(location.x - 2)* 8 + (location.y - 2)][t.getId() - 1];
		}
		return returnedHash;
	}
	
	//Returns 64bit long random
    public static long random64() {
        SecureRandom random = new SecureRandom();
        return random.nextLong();
    }
	
}
