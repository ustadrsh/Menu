package hk.ust.aed.menu;

import java.util.ArrayList;

/**
 * Created by Administrator on 16/08/2017.
 */

public class SRMTrialData {
    public int numSets = 4;
    public ArrayList<Set> sets;

    public class Set{
        public ArrayList<Boolean> correctness;
        public ArrayList<Integer> latencies;
        public int numAlternatives;
        public int numBoxes;
        public int presentationDurationMillis;
        public int trialDurationMillis;
        public int xDivisions;
        public int yDivisions;
    }
}
