package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Random;

public class Interactive {
    private TETile[][] worldArray;
    private WorldGenerator world;
    private Random random;
    private int x;
    private int y;
    private int playerX;
    private int playerY;
    private Files saveLog;
    private Path path;

    public Interactive(int x, int y, Random random, WorldGenerator world) {
        this.worldArray = world.handle();
        this.random = random;
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
    public TETile[][] handleI() {
        initializePos();
        return this.worldArray;
    }
    public void handle() {
        TETile[][] first = handleI();
        TERenderer ter = new TERenderer();
        ter.renderFrame(first);
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'w') {
                    if (this.worldArray[this.playerX][this.playerY + 1] == Tileset.FLOOR) {
                        this.worldArray[this.playerX][this.playerY] = Tileset.FLOOR;
                        this.worldArray[this.playerX][this.playerY + 1] = Tileset.AVATAR;
                        this.playerY++;
                    }
                } else if (c == 'a') {
                    if (this.worldArray[this.playerX - 1][this.playerY] == Tileset.FLOOR) {
                        this.worldArray[this.playerX][this.playerY] = Tileset.FLOOR;
                        this.worldArray[this.playerX - 1][this.playerY] = Tileset.AVATAR;
                        this.playerX--;
                    }
                } else if (c == 's') {
                    if (this.worldArray[this.playerX][this.playerY - 1] == Tileset.FLOOR) {
                        this.worldArray[this.playerX][this.playerY] = Tileset.FLOOR;
                        this.worldArray[this.playerX][this.playerY - 1] = Tileset.AVATAR;
                        this.playerY--;
                    }
                } else if (c == 'd') {
                    if (this.worldArray[this.playerX + 1][this.playerY] == Tileset.FLOOR) {
                        this.worldArray[this.playerX][this.playerY] = Tileset.FLOOR;
                        this.worldArray[this.playerX + 1][this.playerY] = Tileset.AVATAR;
                        this.playerX++;
                    }
                } else if (c == ':') {
                    if (StdDraw.nextKeyTyped() == 'q') {

                    }
                }
            }
            ter.renderFrame(this.worldArray);
        }
    }

}
