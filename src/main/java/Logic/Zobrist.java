package Logic;

import java.awt.Point;
import java.security.SecureRandom;
import java.util.List;
import Tools.Tool;

public class Zobrist {
	
	public static long[][] keys = new long[64][16];
	public static long hash;
	
	public static void initiateKeys() {
		for(int x = 0; x < 64; x++) {
			for(int y = 0; y < 16; y++) {
				keys[x][y] = random64();
			}
		}
	}
	
	public static long getHash(List<Tool> whiteTools, List<Tool> blackTools) {
		hash =  getHashOfArray(whiteTools) ^ getHashOfArray(blackTools);
		return hash;
	}
	
	public static long getHash(Point srcLocation, Point dstLocation, int id) {
		hash ^= keys[(srcLocation.x - 2)* 8 + (srcLocation.y - 2)][id - 1];
		hash ^= keys[(dstLocation.x - 2)* 8 + (dstLocation.y - 2)][id - 1];
		return hash;
	}
	
	private static long getHashOfArray(List<Tool> tools) {
		long returnedHash = 0;
		for(Tool t: tools) {
			Point location = t.getLocation();
			returnedHash ^= keys[(location.x - 2)* 8 + (location.y - 2)][t.getId() - 1];
		}
		return returnedHash;
	}
	
	
    public static long random64() {
        SecureRandom random = new SecureRandom();
        return random.nextLong();
    }
	
}
