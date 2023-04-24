package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.TreeMap;

/**
 * A class to streamline the menu drawing process
 */
public class Menu {
    private TERenderer ter;
    private int x;
    private int y;
    private final TreeMap<Integer, Color> colorMap;
    private Tileset set = new Tileset();
    public Menu(TERenderer terIn, int x, int y) {
        this.ter = terIn;
        this.x = x;
        this.y = y;
        this.colorMap = new TreeMap<>();
        colorMap.put(0, Color.MAGENTA);
        colorMap.put(1, Color.RED);
        colorMap.put(2, Color.ORANGE);
        colorMap.put(3, StdDraw.BOOK_BLUE);
        colorMap.put(4, Color.GREEN);
        colorMap.put(5, Color.BLUE);
        colorMap.put(6, Color.PINK);
        colorMap.put(7, Color.WHITE);
    }
    public void drawMenu() {
        Font old = StdDraw.getFont();
        Font title = new Font("Arial", Font.PLAIN, 40);
        Font subtitle = new Font("Arial", Font.PLAIN, 18);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(title);
        StdDraw.text(this.x / 2.0, this.y / 1.3, "CS61B: THE GAME");
        StdDraw.setFont(subtitle);
        StdDraw.text(this.x / 2.0, this.y / 1.7, "New Game (N)");
        StdDraw.text(this.x / 2.0, (this.y / 1.7 ) - 2, "Load Game (L)");
        StdDraw.text(this.x / 2.0, (this.y / 1.7 ) - 4, "Quit (Q)");
        StdDraw.setFont(old);
        StdDraw.show();

    }
    public void drawMenuCharacter(int a) {
        Font old = StdDraw.getFont();
        Font title = new Font("Arial", Font.PLAIN, 80);
        Font subtitle = new Font("Arial", Font.PLAIN, 18);
        StdDraw.setFont(subtitle);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(this.x / 6.0, this.y / 2.5, "Change Character (C)");
        StdDraw.setFont(title);
        StdDraw.setPenColor(colorMap.get(a % 8));
        StdDraw.text(this.x / 6.0, this.y / 1.85, "@");
        StdDraw.setFont(old);
        this.set.setPlayerColor(colorMap.get(a % 8));
        StdDraw.show();
    }
    public Long drawSeedMenu() {
        Font old = StdDraw.getFont();
        Font title = new Font("Arial", Font.PLAIN, 40);
        Font subtitle = new Font("Arial", Font.PLAIN, 18);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(title);
        StdDraw.text(this.x / 2.0, this.y / 1.3, "Enter Seed");
        StdDraw.setFont(subtitle);
        StdDraw.text(this.x / 2.0, this.y / 4.0, "S to confirm");
        boolean toggle = true;
        String collector = "";
        StdDraw.show();
        while (toggle) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 's' || c == 'S') {
                    toggle = false;
                } else {
                    System.out.println((int) c);
                    if (48 <= (int) c && (int) c <= 57) {
                        collector += c;
                        StdDraw.clear(StdDraw.BLACK);
                        StdDraw.setFont(title);
                        StdDraw.text(this.x / 2.0, this.y / 1.3, "Enter Seed");
                        StdDraw.setFont(subtitle);
                        StdDraw.text(this.x / 2.0, this.y / 3.0, collector);
                        StdDraw.text(this.x / 2.0, this.y / 4.0, "S to confirm");
                        StdDraw.show();
                    }
                }
            }
        }
        StdDraw.setFont(old);
        if (collector == "") {
            return Long.parseLong("100");
        }
        return Long.parseLong(collector);
    }
}
