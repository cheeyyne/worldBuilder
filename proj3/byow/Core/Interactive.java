package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Random;

public class Interactive {
    private TETile[][] worldArray;
    private Random random;
    private int x;
    private int y;
    private int playerX;
    private int playerY;
    private FileWriter writer;
    private TERenderer ter;
    private MoveSequencer mover;
    private boolean doFirst;
    private Long seed;
    public Interactive(int x, int y, Random random, WorldGenerator world, TERenderer ter, boolean first, Long seed) {
        this.worldArray = world.handle();
        this.doFirst = first;
        this.seed = seed;
        this.mover = new MoveSequencer(this.worldArray, random, this.x, this.y, this.seed);
        this.random = random;
        this.ter = ter;
        this.x = x;
        this.y = y;

    }

    public void initializePos() {
        boolean initialized = false;
        while (initialized == false) {
            int xpos = RandomUtils.uniform(this.random, 0, this.x);
            int ypos = RandomUtils.uniform(this.random, 0, this.y);
            if (this.worldArray[xpos][ypos] == Tileset.FLOOR) {
                this.worldArray[xpos][ypos] = Tileset.AVATAR;
                this.playerX = xpos;
                this.playerY = ypos;
                initialized = true;
            }
        }
    }
    public Integer[] findPlayer() {
        Integer[] returner = new Integer[2];
        for (int i = 0; i < this.x; i++) {
            for (int j = 0; j < y; j++) {
                if (this.worldArray[i][j] == Tileset.AVATAR) {
                    returner[0] = i;
                    returner[1] = j;
                    return returner;
                }
            }
        }
        return null;
    }
    public TETile[][] handleI() {
        initializePos();
        return this.worldArray;
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
            this.writer = new FileWriter("Save.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.write(returner);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public TETile[][] torchify() {
        TETile[][] returner = new TETile[this.x][this.y];
        for (int i = 0; i < this.x; i++) {
            for (int j = 0; j < this.y; j++) {
                if (distanceTo(i, j, this.playerX, this.playerY) > 4) {
                    returner[i][j] = Tileset.NOTHING;
                } else {
                    returner[i][j] = this.worldArray[i][j];
                }
            }
        }
        return returner;
    }
    public int distanceTo(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
    public void handle() {
        if (this.doFirst) {
            TETile[][] first = handleI();
            ter.renderFrame(first);
        } else {
            Integer[] blah = this.findPlayer();
            this.playerX = blah[0];
            this.playerY = blah[1];
            ter.renderFrame(this.worldArray);
        }
        Double mousePosx = .0;
        Double mousePosy = .0;
        String temp = "yeah";
        String disclaimer = "(z) Lights";
        boolean torchified = true;

        while (true) {
            Color oldColor = StdDraw.getPenColor();
            StdDraw.setPenColor(255, 255, 255);
            mousePosx = StdDraw.mouseX();
            mousePosy = StdDraw.mouseY();
            int arrayX = mousePosx.intValue();
            int arrayY = mousePosy.intValue();
            if (arrayY == 30) {
                arrayY--;
            }
            if (worldArray[arrayX][arrayY] == Tileset.NOTHING) {
                temp = "Void";
            } else if (worldArray[arrayX][arrayY] == Tileset.WALL) {
                temp = "Wall";
            } else if (worldArray[arrayX][arrayY] == Tileset.FLOOR) {
                temp = "Floor";
            } else {
                temp = "Player";
            }
            if (torchified) {
                if (distanceTo(arrayX, arrayY, this.playerX, this.playerY) < 5) {
                    StdDraw.text(this.x / 10.0, this.y / 9.1, temp);
                }
            } else {
                StdDraw.text(this.x / 10.0, this.y / 9.1, temp);
            }
            StdDraw.text(this.x / 10.0, this.y / 14.0, disclaimer);
            StdDraw.show();
            if (torchified) {
                ter.renderFrame(torchify());
            } else {
                ter.renderFrame(this.worldArray);
            }
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                int response = this.mover.handleMove(c, playerX, playerY, ter);
                if (response != 0) {
                    if (c == 'w' || c == 'W') {
                        this.playerY++;
                    } else if (c == 'a' || c == 'A') {
                        this.playerX--;
                    } else if (c == 's' || c == 'S') {
                        this.playerY--;
                    } else if (c == 'd' || c == 'D') {
                        this.playerX++;
                    } else if (c == ':') {
                        if (response == 1) {
                            this.stateToSaveString();
                            System.exit(0);
                        }
                    } else if (c == 'z') {
                        torchified = !torchified;
                    }
                }
                }
        }

    }

}
