package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    static File join(String first, String...others) {
        return Paths.get(first, others).toFile();
    }
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        TERenderer ter = new TERenderer();
        Menu mainMenu = new Menu(ter, WIDTH, HEIGHT);
        ter.initialize(WIDTH, HEIGHT);
        mainMenu.drawMenu();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if ((c == 'n' || c == 'N')) {
                    System.out.println("blah2");
                    Long seed = mainMenu.drawSeedMenu();
                    Random mainRand = new Random(seed);
                    WorldGenerator gen = new WorldGenerator(new TETile[WIDTH][HEIGHT], WIDTH, HEIGHT, mainRand, true);
                    Interactive inter = new Interactive(WIDTH, HEIGHT, mainRand, gen, ter, true, seed);
                    inter.handle();
                    break;
                } else if (c == 'l' || c == 'L') {
                    loadSave();
                }
            }
        }
    }
    public TETile[][] loadSave() {
        Scanner sc = null;
        try {
            sc = new Scanner(new File("./Save.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int temp = 0;
        int x = sc.nextInt();
        int y = sc.nextInt();
        Long seed = sc.nextLong();
        int pX = sc.nextInt();
        int pY = sc.nextInt();
        TETile[][] returner = new TETile[x][y];
        while (sc.hasNextInt()) {
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    temp = sc.nextInt();
                    if (temp == 1) {
                        returner[i][j] = Tileset.NOTHING;
                    } else if (temp == 2) {
                        returner[i][j] = Tileset.WALL;
                    } else if (temp == 3) {
                        returner[i][j] = Tileset.FLOOR;
                    } else {
                        returner[i][j] = Tileset.AVATAR;
                        pX = i;
                        pY = j;
                    }
                }
            }
        }
        Random mainRand = new Random(seed);
        WorldGenerator gen = new WorldGenerator(returner, WIDTH, HEIGHT, mainRand, false);
        Interactive inter = new Interactive(WIDTH, HEIGHT, mainRand, gen, ter, false, seed);
        inter.handle();
        return returner;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @return the 2D TETile[][] representing the state of the world
     **/
    public TETile[][] interactWithInputString(String seed) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        int pointer1 = 0;
        int pointer2 = 0;
        Boolean toggle = true;
        while (toggle) {
            if (seed.charAt(pointer1) == 'N') {
                pointer1++;
                toggle = false;
            } else {
                pointer1++;
                if (pointer1 >= seed.length()) {
                    throw new RuntimeException("you suck");
                }
            }
        }
        toggle = true;
        pointer2 = pointer1 + 1;
        while (toggle) {
            if (seed.charAt(pointer2) == 'S') {
                toggle = false;
            } else {
                pointer2++;
                if (pointer2 >= seed.length()) {
                    throw new RuntimeException("you suck");
                }
            }
        }
        Long seedInt = Long.parseLong(seed.substring(pointer1, pointer2));
        Random mainRand = new Random(seedInt);
        String moveSequence = seed.substring(pointer2,  seed.length());
        WorldGenerator gen = new WorldGenerator(new TETile[WIDTH][HEIGHT], WIDTH, HEIGHT, mainRand, true);
        return gen.handle();
    }
}
