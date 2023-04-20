package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.util.Random;

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
        Random r = new Random();
        int seedLength = r.nextInt(0, 10);
        String seed = "";
        for (int i = 0; i < seedLength; i++) {
            String s = Integer.toString(r.nextInt(0, 10));
            seed = seed + s;
        }
        int seed2 = Integer.parseInt(seed);
        File save = join("/tmp/sp23-proj3-g1277", "save.txt");
        Path save1 = Files.createFile(save.toPath());
        Files.
        WorldGenerator gen = new WorldGenerator(new TETile[WIDTH][HEIGHT], WIDTH, HEIGHT, new Random(seed2));
        Interactive inter = new Interactive(WIDTH, HEIGHT, new Random(seed2), gen);
        inter.handle();
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
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        Long blah = Long.parseLong(input.substring(1, input.length() - 2));
        WorldGenerator gen = new WorldGenerator(new TETile[WIDTH][HEIGHT], WIDTH, HEIGHT, new Random(blah));
        return gen.handle();
    }
}
