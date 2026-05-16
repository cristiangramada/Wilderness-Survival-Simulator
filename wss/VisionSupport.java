package wss;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

final class VisionSupport {

    private VisionSupport() {
    }

    static boolean offsetInTable(int dx, int dy, int[][] table) {
        for (int[] o : table) {
            if (o[0] == dx && o[1] == dy) {
                return true;
            }
        }
        return false;
    }

    static boolean cellIsVisibleFrom(int px, int py, int gx, int gy, int[][] vis) {
        return offsetInTable(gx - px, gy - py, vis);
    }

    static List<PerceivedTile> legalSteps(Player player, Map map) {
        List<PerceivedTile> out = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                Move probe = new Move(map, player, dx, dy);
                if (!probe.isValid()) {
                    continue;
                }
                Square dest = map.getSquare(probe.getDestination()[0], probe.getDestination()[1]);
                Terrain t = dest.getTerrainType();
                if (t == null) {
                    continue;
                }
                int[] c = t.getCosts();
                out.add(new PerceivedTile(dx, dy, dest, c[0], c[1], c[2],
                        false, 0, 0));
            }
        }
        return out;
    }

    // returns all tiles the player can legally step to that also fall within the vision offset table
    static List<PerceivedTile> scopedSurvey(Player player, Map map, int[][] visOffsets) {
        List<PerceivedTile> legal = legalSteps(player, map);
        int px = player.getCurrentSquare().getCoordinates()[0];
        int py = player.getCurrentSquare().getCoordinates()[1];
        int eastAvg = averageMovementVisibleEastward(map, px, py, visOffsets);

        List<PerceivedTile> out = new ArrayList<>();
        for (PerceivedTile t : legal) {
            if (!offsetInTable(t.dx, t.dy, visOffsets)) {
                continue;
            }
            boolean risky = t.totalCost >= 13 || t.movementCost >= 7;
            int outlook = doubleStepMovementOutlook(player, map, t.dx, t.dy);
            out.add(new PerceivedTile(t.dx, t.dy, t.destination,
                    t.movementCost, t.waterCost, t.foodCost,
                    risky, outlook, eastAvg));
        }
        return out;
    }

    static int averageMovementVisibleEastward(Map map, int px, int py, int[][] visOffsets) {
        int sum = 0;
        int n = 0;
        for (int[] o : visOffsets) {
            if (o[0] <= 0) {
                continue;
            }
            int x = px + o[0];
            int y = py + o[1];
            if (!map.isInBounds(x, y)) {
                continue;
            }
            Terrain t = map.getSquare(x, y).getTerrainType();
            if (t != null) {
                sum += t.getCosts()[0];
                n++;
            }
        }
        return n == 0 ? 0 : sum / n;
    }

    static int doubleStepMovementOutlook(Player player, Map map, int dx, int dy) {
        int cx = player.getCurrentSquare().getCoordinates()[0];
        int cy = player.getCurrentSquare().getCoordinates()[1];
        Move first = new Move(map, player, dx, dy);
        if (!first.isValid()) {
            return 0;
        }
        int nx = cx + dx;
        int ny = cy + dy;
        int cost = 0;
        Terrain t1 = map.getSquare(nx, ny).getTerrainType();
        if (t1 != null) {
            cost += t1.getCosts()[0];
        }
        if (!map.isInBounds(nx + dx, ny + dy)) {
            return cost;
        }
        Terrain t2 = map.getSquare(nx + dx, ny + dy).getTerrainType();
        if (t2 != null) {
            cost += t2.getCosts()[0];
        }
        return cost;
    }

    static Path closestOfKind(Player player, Map map, int[][] vis, Item.Kind kind) {
        List<int[]> candidates = visibleCellsWithItem(player, map, vis, kind);
        return pathToBestCandidate(player, map, vis, candidates, true);
    }

    static Path closestTrader(Player player, Map map, int[][] vis) {
        List<int[]> candidates = new ArrayList<>();
        int px = player.getCurrentSquare().getCoordinates()[0];
        int py = player.getCurrentSquare().getCoordinates()[1];
        for (int[] o : vis) {
            int gx = px + o[0];
            int gy = py + o[1];
            if (!map.isInBounds(gx, gy)) {
                continue;
            }
            if (map.getSquare(gx, gy).hasTrader()) {
                candidates.add(new int[] { gx, gy });
            }
        }
        return pathToBestCandidate(player, map, vis, candidates, true);
    }

    static Path secondClosestOfKind(Player player, Map map, int[][] vis, Item.Kind kind) {
        List<int[]> candidates = visibleCellsWithItem(player, map, vis, kind);
        return pathToBestCandidate(player, map, vis, candidates, false);
    }

    static Path secondClosestTrader(Player player, Map map, int[][] vis) {
        List<int[]> candidates = new ArrayList<>();
        int px = player.getCurrentSquare().getCoordinates()[0];
        int py = player.getCurrentSquare().getCoordinates()[1];
        for (int[] o : vis) {
            int gx = px + o[0];
            int gy = py + o[1];
            if (!map.isInBounds(gx, gy)) {
                continue;
            }
            if (map.getSquare(gx, gy).hasTrader()) {
                candidates.add(new int[] { gx, gy });
            }
        }
        return pathToBestCandidate(player, map, vis, candidates, false);
    }

    static Path easiestVisiblePath(Player player, Map map, int[][] vis) {
        int px = player.getCurrentSquare().getCoordinates()[0];
        int py = player.getCurrentSquare().getCoordinates()[1];
        List<int[]> candidates = new ArrayList<>();
        for (int[] o : vis) {
            int gx = px + o[0];
            int gy = py + o[1];
            if (!map.isInBounds(gx, gy)) {
                continue;
            }
            candidates.add(new int[] { gx, gy });
        }
        if (candidates.isEmpty()) {
            return Path.empty(map);
        }
        Collections.sort(candidates, (a, b) -> {
            Terrain ta = map.getSquare(a[0], a[1]).getTerrainType();
            Terrain tb = map.getSquare(b[0], b[1]).getTerrainType();
            int ma = ta == null ? 999 : ta.getCosts()[0];
            int mb = tb == null ? 999 : tb.getCosts()[0];
            if (ma != mb) {
                return Integer.compare(ma, mb);
            }
            int da = Math.abs(a[0] - px) + Math.abs(a[1] - py);
            int db = Math.abs(b[0] - px) + Math.abs(b[1] - py);
            if (da != db) {
                return Integer.compare(da, db);
            }
            return Integer.compare(b[0], a[0]);
        });
        int[] goal = candidates.get(0);
        return buildPathFromBfs(player, map, goal[0], goal[1]);
    }

    private static List<int[]> visibleCellsWithItem(Player player, Map map, int[][] vis, Item.Kind kind) {
        List<int[]> out = new ArrayList<>();
        int px = player.getCurrentSquare().getCoordinates()[0];
        int py = player.getCurrentSquare().getCoordinates()[1];
        for (int[] o : vis) {
            int gx = px + o[0];
            int gy = py + o[1];
            if (!map.isInBounds(gx, gy)) {
                continue;
            }
            Square sq = map.getSquare(gx, gy);
            for (Item it : sq.getItems()) {
                if (it.getKind() == kind) {
                    out.add(new int[] { gx, gy });
                    break;
                }
            }
        }
        return out;
    }

    // firstBest=false returns the second-ranked candidate (used when two options tie)
    private static Path pathToBestCandidate(Player player, Map map, int[][] vis,
            List<int[]> candidates, boolean firstBest) {
        if (candidates.isEmpty()) {
            return Path.empty(map);
        }
        int px = player.getCurrentSquare().getCoordinates()[0];
        int py = player.getCurrentSquare().getCoordinates()[1];
        List<int[]> ranked = new ArrayList<>(candidates);
        Collections.sort(ranked, foodWaterGoldComparator(map, px, py));
        if (!firstBest && ranked.size() < 2) {
            return Path.empty(map);
        }
        int index = firstBest ? 0 : 1;
        int[] goal = ranked.get(index);
        return buildPathFromBfs(player, map, goal[0], goal[1]);
    }

    private static Comparator<int[]> foodWaterGoldComparator(Map map, int px, int py) {
        return (a, b) -> {
            int da = bfsStepDistance(map, px, py, a[0], a[1]);
            int db = bfsStepDistance(map, px, py, b[0], b[1]);
            if (da != db) {
                return Integer.compare(da, db);
            }
            Terrain ta = map.getSquare(a[0], a[1]).getTerrainType();
            Terrain tb = map.getSquare(b[0], b[1]).getTerrainType();
            int ma = ta == null ? 999 : ta.getCosts()[0];
            int mb = tb == null ? 999 : tb.getCosts()[0];
            if (ma != mb) {
                return Integer.compare(ma, mb);
            }
            return Integer.compare(b[0], a[0]);
        };
    }

    private static int bfsStepDistance(Map map, int sx, int sy, int gx, int gy) {
        if (sx == gx && sy == gy) {
            return 0;
        }
        int w = map.getWidth();
        int h = map.getHeight();
        int[][] dist = new int[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                dist[y][x] = -1;
            }
        }
        ArrayDeque<int[]> q = new ArrayDeque<>();
        dist[sy][sx] = 0;
        q.add(new int[] { sx, sy });
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int cx = cur[0];
            int cy = cur[1];
            if (cx == gx && cy == gy) {
                return dist[cy][cx];
            }
            for (int[] d : dirs) {
                int nx = cx + d[0];
                int ny = cy + d[1];
                if (!map.isInBounds(nx, ny) || dist[ny][nx] != -1) {
                    continue;
                }
                dist[ny][nx] = dist[cy][cx] + 1;
                q.add(new int[] { nx, ny });
            }
        }
        return Integer.MAX_VALUE / 4;
    }

    private static Path buildPathFromBfs(Player player, Map map, int gx, int gy) {
        int sx = player.getCurrentSquare().getCoordinates()[0];
        int sy = player.getCurrentSquare().getCoordinates()[1];
        if (sx == gx && sy == gy) {
            return Path.empty(map);
        }
        int w = map.getWidth();
        int h = map.getHeight();
        int[][] dist = new int[h][w];
        int[][] pxp = new int[h][w];
        int[][] pyp = new int[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                dist[y][x] = -1;
                pxp[y][x] = -1;
                pyp[y][x] = -1;
            }
        }
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };
        ArrayDeque<int[]> q = new ArrayDeque<>();
        dist[sy][sx] = 0;
        q.add(new int[] { sx, sy });
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int cx = cur[0];
            int cy = cur[1];
            if (cx == gx && cy == gy) {
                break;
            }
            for (int[] d : dirs) {
                int nx = cx + d[0];
                int ny = cy + d[1];
                if (!map.isInBounds(nx, ny) || dist[ny][nx] != -1) {
                    continue;
                }
                dist[ny][nx] = dist[cy][cx] + 1;
                pxp[ny][nx] = cx;
                pyp[ny][nx] = cy;
                q.add(new int[] { nx, ny });
            }
        }
        if (dist[gy][gx] < 0) {
            return Path.empty(map);
        }
        ArrayList<int[]> cells = new ArrayList<>();
        int x = gx;
        int y = gy;
        while (x != sx || y != sy) {
            cells.add(new int[] { x, y });
            int px = pxp[y][x];
            int py = pyp[y][x];
            if (px < 0) {
                return Path.empty(map);
            }
            x = px;
            y = py;
        }
        cells.add(new int[] { sx, sy });
        Collections.reverse(cells);
        return pathFromCellChain(map, cells);
    }

    private static Path pathFromCellChain(Map map, List<int[]> cells) {
        if (cells.size() < 2) {
            return Path.empty(map);
        }
        int tm = 0;
        int tw = 0;
        int tf = 0;
        List<int[]> deltas = new ArrayList<>();
        for (int i = 1; i < cells.size(); i++) {
            int x0 = cells.get(i - 1)[0];
            int y0 = cells.get(i - 1)[1];
            int x1 = cells.get(i)[0];
            int y1 = cells.get(i)[1];
            int dx = x1 - x0;
            int dy = y1 - y0;
            deltas.add(new int[] { dx, dy });
            Terrain t = map.getSquare(x1, y1).getTerrainType();
            if (t != null) {
                int[] c = t.getCosts();
                tm += c[0];
                tw += c[1];
                tf += c[2];
            }
        }
        return Path.planned(map, deltas, tm, tw, tf);
    }
}
