package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Random;

public class MoveSequencer {
    private TETile[][] worldArray;
    private Random mainRand;
    public MoveSequencer(TETile[][] world, Random rand) {
        this.worldArray = world;
        this.mainRand = rand;
    }

    public int handleMove(char c, int playerX, int playerY, TERenderer ter) {
            if (c == 'w' || c == 'W') {
                if (this.worldArray[playerX][playerY + 1] == Tileset.FLOOR) {
                    this.worldArray[playerX][playerY] = Tileset.FLOOR;
                    this.worldArray[playerX][playerY + 1] = Tileset.AVATAR;
                    ter.renderFrame(this.worldArray);
                    return 11;
                }
            } else if (c == 'a' || c == 'A') {
                if (this.worldArray[playerX - 1][playerY] == Tileset.FLOOR) {
                    this.worldArray[playerX][playerY] = Tileset.FLOOR;
                    this.worldArray[playerX - 1][playerY] = Tileset.AVATAR;
                    ter.renderFrame(this.worldArray);
                    return 12;
                }
            } else if (c == 's' || c == 'S') {
                if (this.worldArray[playerX][playerY - 1] == Tileset.FLOOR) {
                    this.worldArray[playerX][playerY] = Tileset.FLOOR;
                    this.worldArray[playerX][playerY - 1] = Tileset.AVATAR;
                    ter.renderFrame(this.worldArray);
                    return 13;
                }
            } else if (c == 'd' || c == 'D') {
                if (this.worldArray[playerX + 1][playerY] == Tileset.FLOOR) {
                    this.worldArray[playerX][playerY] = Tileset.FLOOR;
                    this.worldArray[playerX + 1][playerY] = Tileset.AVATAR;
                    ter.renderFrame(this.worldArray);
                    return 14;
                }
            } else if (c == ':') {
                while (true) {
                    if (StdDraw.hasNextKeyTyped() && StdDraw.nextKeyTyped() == 'q') {
                        return 1;
                    }
                }
            }
            return 0;
    }
}
