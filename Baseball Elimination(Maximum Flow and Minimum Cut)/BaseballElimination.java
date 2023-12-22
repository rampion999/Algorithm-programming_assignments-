
import java.util.HashMap;
import java.util.LinkedList;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowEdge;

public class BaseballElimination {

    private final int teamNum;
    private final int[] wins;
    private final int[] loss;
    private final int[] left;
    private final int[][] games;
    private final HashMap<String, Integer> teams;
    private final String[] teamArr;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        In in = new In(filename);
        teamNum = Integer.parseInt(in.readLine());
        teams = new HashMap<String, Integer>();
        wins = new int[teamNum];
        loss = new int[teamNum];
        left = new int[teamNum];
        games = new int[teamNum][teamNum];
        teamArr = new String[teamNum];
        int cnt = 0;
        while (!in.isEmpty()) {
            String rd = in.readLine();
            String[] i;
            if (rd != null) {
                i = rd.trim().split("\\s+");
            } else {
                throw new IllegalArgumentException();
            }
            teams.put(i[0], cnt);
            teamArr[cnt] = i[0];
            wins[cnt] = Integer.parseInt(i[1]);
            loss[cnt] = Integer.parseInt(i[2]);
            left[cnt] = Integer.parseInt(i[3]);
            for (int j = 0; j < teamNum; j++) {
                games[cnt][j] = Integer.parseInt(i[4 + j]);
            }
            cnt++;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teamNum;
    }                        

    // all teams
    public Iterable<String> teams() {
        return teams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null || !teams.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return wins[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null || !teams.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return loss[teams.get(team)];
    }
    
    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null || !teams.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return left[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || team2 == null || !teams.containsKey(team1) || !teams.containsKey(team2)) {
            throw new IllegalArgumentException();
        }
        return games[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null || !teams.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        if (teamNum == 1) {
            return false;
        }

        if (teamNum == 2) {
            int aIdx;
            int bIdx;
            if (teams.get(team) == 0) {
                aIdx = 0;
                bIdx = 1;
            } else {
                aIdx = 1;
                bIdx = 0;
            }
            return wins[aIdx] + left[aIdx] < wins[bIdx];
        }
        int combCnts = (teamNum - 1) * (teamNum - 2) / 2;
        int dutIdx = teams.get(team);
        int dutW = wins[dutIdx];
        int dutR = left[dutIdx];
        FlowNetwork fn = new FlowNetwork(combCnts + teamNum + 1);
        int vIdx = teamNum;
        int maxCheck = 0;
        for (int i = 0; i < teamNum; i++) {
            if (i != dutIdx) {
                if (dutW + dutR - wins[i] < 0) {
                    return true;
                }
                fn.addEdge(new FlowEdge(i, combCnts + teamNum, (double) (dutW + dutR - wins[i])));
                for (int j = i + 1; j < teamNum; j++) {
                    if (j != dutIdx) {
                        fn.addEdge(new FlowEdge(dutIdx, vIdx, games[i][j]));
                        fn.addEdge(new FlowEdge(vIdx, i, Double.POSITIVE_INFINITY));
                        fn.addEdge(new FlowEdge(vIdx, j, Double.POSITIVE_INFINITY));
                        vIdx++;
                        maxCheck += games[i][j];
                    }
                }
            }
        }
        FordFulkerson ff = new FordFulkerson(fn, dutIdx, combCnts + teamNum);
        return ff.value() != maxCheck;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || !teams.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        if (teamNum == 1) {
            return null;
        }

        LinkedList<String> ans = new LinkedList<String>();

        if (teamNum == 2) {
            int aIdx;
            int bIdx;
            if (teams.get(team) == 0) {
                aIdx = 0;
                bIdx = 1;
            } else {
                aIdx = 1;
                bIdx = 0;
            }
            if (wins[aIdx] + left[aIdx] < wins[bIdx]) {
                ans.add(teamArr[bIdx]);
            }
            return ans;
        }

        
        int combCnts = (teamNum - 1) * (teamNum - 2) / 2;
        int dutIdx = teams.get(team);
        int dutW = wins[dutIdx];
        int dutR = left[dutIdx];
        FlowNetwork fn = new FlowNetwork(combCnts + teamNum + 1);
        int vIdx = teamNum;
        for (int i = 0; i < teamNum; i++) {
            if (i != dutIdx) {
                if (dutW + dutR - wins[i] < 0) {
                    ans.add(teamArr[i]);
                } else {              
                    fn.addEdge(new FlowEdge(i, combCnts + teamNum, (double) (dutW + dutR - wins[i])));
                    for (int j = i + 1; j < teamNum; j++) {
                        if (j != dutIdx) {
                            fn.addEdge(new FlowEdge(dutIdx, vIdx, games[i][j]));
                            fn.addEdge(new FlowEdge(vIdx, i, Double.POSITIVE_INFINITY));
                            fn.addEdge(new FlowEdge(vIdx, j, Double.POSITIVE_INFINITY));
                            vIdx++;
                        }
                    }
                }
            }
        }

        FordFulkerson ff = new FordFulkerson(fn, dutIdx, combCnts + teamNum);

        for (int i = 0; i < teamNum; i++) {
            if (i != dutIdx && ff.inCut(i)) {
                ans.add(teamArr[i]);
            }
        }
        if (ans.isEmpty()) {
            return null;
        }
        return ans;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }

        // StdOut.println(division.numberOfTeams());
        // for (String team : division.teams()) {
        //     StdOut.println(team);
        // }

        // StdOut.println("wins of New_York: " + division.wins("New_York"));
        // StdOut.println("losses of Philadelphia: " + division.losses("Philadelphia"));
        // StdOut.println("remaining of Montreal: " + division.remaining("Montreal"));
        // StdOut.println("Nums of Atlanta v.s New_York: " + division.against("Atlanta", "New_York"));
    }

}
