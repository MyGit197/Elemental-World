/*
    Map.java

    This class is used to load the map from an image and create the level.
 */

import java.awt.image.BufferedImage;
import java.util.*;

public class Map {
    String map; // duong dan toi file anh ban do

    // kiich thước
    private int w = 1000;
    private int h = 27;
    public static Startpos startpos;

    // arraylists cac doi tuong
    ArrayList<Solid> solids;
    ArrayList<Spike> spikes;
    ArrayList<Portal> portals;
    ArrayList<Orb> orbs;
    ArrayList<Pad> pads;

    public static ArrayList<Checkpoint> checkpoints;

    public static int mapWidth; // do rong map pixel
    int[][] mapArr; //ma hoa ban do bang mang 2 chieu
    BufferedImage pic;

    // phuong thuc khoi tao
    public Map(String map) {
        this.map = map;
        solids = new ArrayList<Solid>();
        spikes = new ArrayList<Spike>();
        portals = new ArrayList<Portal>();
        checkpoints = new ArrayList<Checkpoint>();
        pads = new ArrayList<Pad>();
        orbs = new ArrayList<Orb>();
        mapArr = new int[6000][27];
        pic = Util.loadBuffImage(map);
        mapWidth = pic.getWidth();
        loadMap();
        makeMap();
    }


    public void loadMap(){

        // gan gia tri cho moi pixel dua tren mau sac, cac gia trị nay dung de tao doi tuong ban do
        /*
            Bang gan gia tri:
            1 - Solids
            2 - Spike (upright)
            3 - Slabs
            4 - small spike (upright)
            5 - Spike (downwards)
            6 - small spike (downwards)
            7 - slab (down)
            10 - ship portal
            11 - cube portal
         */
        int wIndex = 0;
//        for (String s: map) {
            int tempw = pic.getWidth();
            int temph = pic.getHeight();
            for (int x = wIndex; x < tempw + wIndex; x++) {
                for (int y = 0; y < temph; y++) {
                    int c = pic.getRGB(x - wIndex, y);
                    int v = 0;
                    if (c == 0xFF0026FF) {
//                    solids[y][x] = new Solid( x*50, y*50, "solid");
                        v = 1;
                    }
                    else if (c == 0xFFFF0000) {
                        v = 2;
                    }
                    else if (c == 0xFF00FFFF) {
                        v = 3;
                    }
                    else if (c == 0xFFFF00DC) {
                        v = 4;
                    }
                    else if (c == 0xFF7F0000) {
                        v = 5;
                    }
                    else if (c == 0xFF7F006E) {
                        v = 6;
                    }
                    else if (c == 0xFF007F7F) {
                        v = 7;
                    }
                    else if (c == 0xFF00FF21) {
                        v = 10;
                    }
                    else if (c == 0xFF007F0E) {
                        v = 11;
                    }
                    else if ( c == 0xFFFF6A00) {
                        v = 12;
                    }
                    else if ( c == 0xFFB200FF) {
                        v = 13;
                    }
                    else if (c == 0xFFFFD800) {
                        v = 14;
                    }
                    else if (c == 0xFF63FFA4) {
                        v = 999;
                    }
                    else if ( c == 0xFFD67FFF) {
                        v = 15;
                    }
                    else if ( c ==0xFFFF7FB6) {
                        v = 16;
                    }
                    else if (c == 0xFFFF7F7F) {
                        v = 17;
                    }
                    mapArr[x][y] = v;
                }
            }
    }

    public void makeMap() {
        // duyet mang dua vao gia tri da gan xac dinh doi tuong duoc tao ra

        // ground: 400

        for (int x=0; x<w; x++) {
            for (int y=0; y<h; y++) {
                int target = mapArr[x][y];
                if (target == 1) {
                    Solid s = new Solid(x * Consts.solidWidth , Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight, "solid");
                    solids.add(s);
                }
                else if (target == 2) {
                    Spike s = new Spike(x * Consts.solidWidth , Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight, 0, "normal");
                    spikes.add(s);
                }
                else if (target == 3) {
                    Solid s = new Solid(x * Consts.slabWidth , Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight, "slabUp");
                    solids.add(s);
                }
                else if (target == 4) {
                    Spike s = new Spike(x * Consts.solidWidth , Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight, 0, "small");
                    spikes.add(s);
                }
                else if (target == 5) {
                    Spike s = new Spike(x * Consts.solidWidth , Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight, 1, "normal");
                    spikes.add(s);
                }
                else if (target == 6) {
                    Spike s = new Spike(x * Consts.solidWidth , Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight, 1, "small");
                    spikes.add(s);
                }
                else if (target == 7) {
                    Solid s = new Solid(x * Consts.slabWidth , Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight, "slabDown");
                    solids.add(s);
                }
                else if (target == 10) {
                    Portal p = new Portal( x * Portal.width, Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight, "ship", 0 );
                    portals.add(p);
                }
                else if (target == 11) {
                    Portal p = new Portal( x * Portal.width, Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight, "cube", 0 );
                    portals.add(p);
                }
                else if (target == 13) {
                    Pad p = new Pad(x * Consts.slabWidth , Consts.floor - ((h-y-7) * Consts.solidHeight) - 30, Consts.solidWidth, 30);
                    pads.add(p);
                }
                else if ( target == 14) {
                    Orb o = new Orb(x * Consts.slabWidth , Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight, "yes" );
                    orbs.add(o);
                }
                else if (target == 999) {
                    startpos = new Startpos(x * Consts.solidWidth , Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight);
                }
                else if (target == 15) {
                    Portal p = new Portal( x * Portal.width, Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight, "reverse", 0 );
                    portals.add(p);
                }
                else if (target == 16) {
                    Portal p = new Portal( x * Portal.width, Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight, "upright", 0 );
                    portals.add(p);
                }
                else if (target == 17) {
                    Spike s = new Spike(x * Consts.solidWidth , Consts.floor - ((h-y-7) * Consts.solidHeight) - Consts.solidHeight, 1, "side");
                    spikes.add(s);
                }
            }
        }
    }


    public int getWidth() {
        return w ;
    }
    public int getHeight() {
        return h ;
    }

    public ArrayList<Solid> getSolids() {
        return solids;
    }
    public ArrayList<Spike> getSpikes() {
        return spikes;
    }
    public ArrayList<Portal> getPortals() {return portals;}
    public ArrayList<Pad> getPads() {return pads;}
    public ArrayList<Orb> getOrbs() {return orbs;}


}
