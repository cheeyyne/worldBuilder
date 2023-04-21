package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.ArrayList;
import java.util.Random;

public class WorldGenerator {
    private Integer[][] idArray;
    private TETile[][] worldArray;
    private int x;
    private int y;
    private Random random;
    private boolean doRandom;
    public WorldGenerator(TETile[][] worldArr, int x, int y, Random random, boolean dorand) {
        this.worldArray = worldArr;
        this.doRandom = dorand;
        this.idArray = new Integer[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                this.idArray[i][j] = 0;
            }
        }
        this.x = x;
        this.y = y;
        this.random = random;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Custom class to hold all relevant information for a room,
     *
     *                      MAKE HALLWAY SPOTS
     */
    private class Room {
        private int xbase;
        private int ybase;
        private int xsize;
        private int ysize;
        private int xWallPoint;
        private int yWallPoint;
        private Room parent;
        private boolean connected;
        private int quad;
        public Room(int xbase, int ybase, int xsize, int ysize) {
            this.xbase = xbase;
            this.ybase = ybase;
            this.xsize = xsize;
            this.ysize = ysize;
            this.quad = RandomUtils.uniform(random, 4);
            if (this.quad % 2 == 0) {
                this.xWallPoint = xbase + RandomUtils.uniform(random, xsize);
                this.yWallPoint = ybase + ((quad / 2) * (ysize - 1));
            } else {
                this.xWallPoint = xbase + (((quad - 1) / 2) * (xsize - 1));
                this.yWallPoint = ybase + RandomUtils.uniform(random, ysize);
            }
            this.connected = false;
        }
    }

    /**
     * Generates a list of Room objects which have relevant and valid positions
     */
    public ArrayList<Room> generateRoomIds(int x1, int y1) {
        int roomTotal = RandomUtils.uniform(random, 4) + 8;
        int xval = 0;
        int yval = 0;
        int xrange = 0;
        int yrange = 0;
        int counter = 0;
        int failcounter = 0;
        ArrayList<Room> returnList = new ArrayList<>();
        while (counter < roomTotal) {
            xval = RandomUtils.uniform(random, x1 - 1) + 1;
            yval = RandomUtils.uniform(random, y1 - 1) + 1;
            xrange = RandomUtils.uniform(random, 4) + 3;
            yrange = RandomUtils.uniform(random, 3) + 3;
            if (checkSpot(xval, yval, xrange, yrange)) {
                returnList.add(new Room(xval, yval, xrange, yrange));
                populate(xval, yval, xrange, yrange);
                counter++;
                failcounter = 0;
            } else {
                /**
                 * if there are frequent fails, abort instead of looping
                 */
                failcounter++;
                if (failcounter > x) {
                    counter += roomTotal;
                }
            }
        }
        /**
         * makes sequential rooms parent-child relationship
         */
        for (int i = 0; i < roomTotal - 1; i++) {
            returnList.get(i).parent = returnList.get(i + 1);
        }
        returnList.get(returnList.size() - 1).parent = returnList.get(0);
        return returnList;
    }

    /**
     * Assumes that the range is already valid, as done in checkSpot()
     */
    public void populate(int xval, int yval, int xsize, int ysize) {
        for (int i = xval; i < xval + xsize; i++) {
            for (int j = yval; j < yval + ysize; j++) {
                this.idArray[i][j] = 2;
            }
        }
    }
    public void populateFromList(ArrayList<Room> roomList) {
        for (Room r : roomList) {
            populate(r.xbase, r.ybase, r.xsize, r.ysize);
        }
    }
    public void hallify(ArrayList<Room> roomList) {
        for (Room room : roomList) {
            connect(room, room.parent);
        }
    }
    public void connect(Room room1, Room room2) {
        int direction = RandomUtils.uniform(random, 2);
        int xpos = room1.xWallPoint;
        int ypos = room1.yWallPoint;
        if (direction == 0) {
            travelX(room1.xWallPoint, room2.xWallPoint, room1.yWallPoint);
            travelY(room1.yWallPoint, room2.yWallPoint, room2.xWallPoint);
        } else {
            travelY(room1.yWallPoint, room2.yWallPoint, room1.xWallPoint);
            travelX(room1.xWallPoint, room2.xWallPoint, room2.yWallPoint);
        }
    }
    public void travelX(int x1, int x2, int y1) {
        int difference;
        if (x1 < x2) {
            difference = 1;
        } else {
            difference = -1;
        }
        while (x1 != x2) {
            this.idArray[x1][y1] = 2;
            x1 = x1 + difference;
        }
    }
    public void travelY(int y1, int y2, int x1) {
        int difference;
        if (y1 < y2) {
            difference = 1;
        } else {
            difference = -1;
        }
        while (y1 != y2) {
            this.idArray[x1][y1] = 2;
            y1 = y1 + difference;
        }
    }

    /**
     * Checks to see if given range (xsize by ysize) is valid with xpos, ypos as the lower left corner
     */
    public boolean checkSpot(int xpos, int ypos, int xsize, int ysize) {
        if ((xpos + xsize >= this.x - 1) || (ypos + ysize >= this.y - 1)) {
            return false;
        }
        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                if (this.idArray[i + xpos][j + ypos] == 2) {
                    return false;
                }
            }
        }
        return true;
    }
    public void populateRealArray() {
        for (int i = 0; i < this.x; i++) {
            for (int j = 0; j < this.y; j++) {
                if (this.idArray[i][j] == 0) {
                    this.worldArray[i][j] = Tileset.NOTHING;
                } else if (this.idArray[i][j] == 1) {
                    this.worldArray[i][j] = Tileset.WALL;
                } else if (this.idArray[i][j] == 2) {
                    this.worldArray[i][j] = Tileset.FLOOR;
                }
            }
        }
    }
    public void wallify(int xpos, int ypos) {
        for (int i = 0; i < xpos; i++) {
            for (int j = 0; j < ypos; j++) {
                if (idArray[i][j] == 2) {
                    pointify(i, j, xpos, ypos);
                }
            }
        }
    }
    public void pointify(int xpos, int ypos, int xmax, int ymax) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (idArray[xpos + i][ypos + j] != 2) {
                    idArray[xpos + i][ypos + j] = 1;
                }
            }
        }
    }
    public TETile[][] handle() {
        if (!this.doRandom) {
            return this.worldArray;
        }
        TERenderer ter = new TERenderer();
        ter.initialize(this.x, this.y);
        ArrayList<Room> blah = this.generateRoomIds(x, y);
        this.hallify(blah);
        this.wallify(x, y);
        this.populateRealArray();
        ter.renderFrame(this.worldArray);
        return this.worldArray;
    }

    /**
     * testing (bleh_)
     * @param args
     */

}
