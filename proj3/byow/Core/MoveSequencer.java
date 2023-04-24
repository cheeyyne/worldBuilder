package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MoveSequencer {
    private TETile[][] worldArray;
    private Random mainRand;
    private String sequence;
    private int playerX;
    private int playerY;
    private int x;
    private int y;
    private FileWriter writer;
    private Long seed;
    public MoveSequencer(TETile[][] world, Random rand, int x, int y, Long seed) {
        this.worldArray = world;
        this.seed = seed;
        this.mainRand = rand;
        this.x = x;
        this.y = y;
        try {
            this.writer = new FileWriter("Save.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setSequence(String sq) {
        this.sequence = sq;
    }
    public void setPlayerX(Integer x) {
        this.playerX = x;
    }
    public void setPlayerY(int y) {
        this.playerY = y;
    }

    public int handleMove(char c, int playerX, int playerY, TERenderer ter) {
            if (c == 'w' || c == 'W') {
                if (this.worldArray[playerX][playerY + 1] == Tileset.FLOOR) {
                    this.worldArray[playerX][playerY] = Tileset.FLOOR;
                    this.worldArray[playerX][playerY + 1] = Tileset.AVATAR;
                    ter.renderFrame(this.worldArray);
                    this.playerX = playerX;
                    this.playerY = playerY + 1;
                    return 11;
                }
            } else if (c == 'a' || c == 'A') {
                if (this.worldArray[playerX - 1][playerY] == Tileset.FLOOR) {
                    this.worldArray[playerX][playerY] = Tileset.FLOOR;
                    this.worldArray[playerX - 1][playerY] = Tileset.AVATAR;
                    ter.renderFrame(this.worldArray);
                    this.playerX = playerX - 1;
                    this.playerY = playerY;
                    return 12;
                }
            } else if (c == 's' || c == 'S') {
                if (this.worldArray[playerX][playerY - 1] == Tileset.FLOOR) {
                    this.worldArray[playerX][playerY] = Tileset.FLOOR;
                    this.worldArray[playerX][playerY - 1] = Tileset.AVATAR;
                    ter.renderFrame(this.worldArray);
                    this.playerX = playerX;
                    this.playerY = playerY - 1;
                    return 13;
                }
            } else if (c == 'd' || c == 'D') {
                if (this.worldArray[playerX + 1][playerY] == Tileset.FLOOR) {
                    this.worldArray[playerX][playerY] = Tileset.FLOOR;
                    this.worldArray[playerX + 1][playerY] = Tileset.AVATAR;
                    ter.renderFrame(this.worldArray);
                    this.playerX = playerX + 1;
                    this.playerY = playerY;
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
    public void initializePos() {
        boolean initialized = false;
        while (initialized == false) {
            int xpos = RandomUtils.uniform(this.mainRand, 0, this.x);
            int ypos = RandomUtils.uniform(this.mainRand, 0, this.y);
            if (this.worldArray[xpos][ypos] == Tileset.FLOOR) {
                this.worldArray[xpos][ypos] = Tileset.AVATAR;
                this.playerX = xpos;
                this.playerY = ypos;
                initialized = true;
            }
        }
    }
    public void stateToSaveString() {
        String returner = this.x + " " + this.y + " " + this.seed;
        returner += " " + this.playerX + " " + this.playerY;
        TETile temp = null;

        for (int i = 0; i < this.x; i++) {
            for (int j = 0; j < this.y; j++) {
                temp = worldArray[i][j];
                if (temp.equals(Tileset.NOTHING)) {
                    returner += " 1";
                } else if (temp.equals(Tileset.WALL)) {
                    returner += " 2";
                } else if (temp.equals(Tileset.FLOOR)) {
                    returner += " 3";
                } else if (temp.equals(Tileset.AVATAR)) {
                    returner += " 4";
                }
            }
        }
        try {
            writer.write(returner);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public TETile[][] handleString(String s) {
        TERenderer ter = new TERenderer();
        ter.initialize(this.x, this.y);
        char c;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            if (c == ':') {
                if (s.charAt(i + 1) == 'q' || s.charAt(i + 1) == 'Q') {
                    stateToSaveString();
                    i = s.length() + 1;
                }
            } else {
                handleMove(s.charAt(i), this.playerX, this.playerY, ter);
            }
        }
    return this.worldArray;
    }
}
