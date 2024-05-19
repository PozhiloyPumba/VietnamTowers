package com.mygdx.towers;

import java.util.LinkedList;

import com.badlogic.gdx.utils.Array;

public class Bot_3 {
    public boolean enable = false;
    private HanoiTowers towers;
    static public int DeltaMoves = 10;

    private class CustomPair {
        public CustomPair(int f, int s) {
            src = f;
            dest = s;
        }
        public int src;
        public int dest;
    };

    private LinkedList<CustomPair> solution = new LinkedList<>();
    private long lastUpdate = System.currentTimeMillis();
    private Array<Array<Integer>> st = new Array<>();

    public Bot_3(HanoiTowers t) {
        towers = t;
    }

    private void solve(int n, int dest) {
        if (n == 0) return;

        if (st.get(dest).contains(n, false)) {
            solve(n - 1, dest);
            return;
        }
    
        int middle = 0;
        for (; middle < 3; ++middle) {
            Array<Integer> s = st.get(middle);

            if (!s.contains(n, false) && middle != dest) break;
        }
        solve(n - 1, middle);

        solution.addLast(new CustomPair(3 - dest - middle, dest));
        st.get(3 - dest - middle).removeValue(n, false);
        st.get(dest).add(n);

        solve(n - 1, dest);
        return;
    }

    public void createSolution() {
        st.clear();
        for (Stick s: towers.sticks)
            st.add(s.returnDisks());

        if (towers.sticks.get(1).getBiggest() > towers.sticks.get(2).getBiggest())
            solve(HanoiTowers.numDisks, 1);
        else
            solve(HanoiTowers.numDisks, 2);
    }

    public void clear() {
        solution.clear();
    }

    public void run() {
        if (!enable || ((System.currentTimeMillis() - lastUpdate) < DeltaMoves)) return;

        lastUpdate = System.currentTimeMillis();
        if (solution.isEmpty())  {
            createSolution();
            return;
        }
        
        CustomPair next = solution.removeFirst();
        towers.botMoves += Stick.move(towers.sticks.get(next.src), towers.sticks.get(next.dest));
    }
}
