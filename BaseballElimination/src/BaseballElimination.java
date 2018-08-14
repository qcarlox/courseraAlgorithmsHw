
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author windows
 */
public class BaseballElimination {
    private ArrayList<String> teams;
    private HashMap<String, Integer> teamToId;
    private ArrayList<HashMap<HashSet<Integer>, Integer>> vertexLabelToIdListOfHashMaps;
    private int[] wins;
    private int[] losses;
    private int[] remainingGames;
    private int[][] gamesRemainingBetweenTeams;
    private int numberOfTeams;
    private FlowNetwork[] flowNetworks;
    private FordFulkerson[] fordFulkersonList;
    
    public BaseballElimination(String filename){
        // create a baseball division from given filename in format specified below
        In fileIn = new In(filename);
        this.numberOfTeams = Integer.parseInt(fileIn.readLine());
        this.teams = new ArrayList<String>(this.numberOfTeams);
        this.wins = new int[this.numberOfTeams];
        this.losses = new int[this.numberOfTeams];
        this.remainingGames = new int[this.numberOfTeams];
        this.gamesRemainingBetweenTeams = new int[this.numberOfTeams][this.numberOfTeams];
        this.teamToId = new HashMap<String, Integer>();
        this.flowNetworks = new FlowNetwork[this.numberOfTeams];
        this.fordFulkersonList = new FordFulkerson[this.numberOfTeams];
        for(int i=0; i<this.numberOfTeams; i++){
            String line = fileIn.readLine();
            String[] list = line.trim().split("\\s+");
            this.teams.add(i, list[0]);
            this.teamToId.put(list[0], i);
            this.wins[i] = Integer.parseInt(list[1]);
            this.losses[i] = Integer.parseInt(list[2]);
            this.remainingGames[i] = Integer.parseInt(list[3]);
            for(int j=0; j<this.numberOfTeams; j++){
                this.gamesRemainingBetweenTeams[i][j] = Integer.parseInt(list[j+4]);
            }
            
        }
        constructGraphs();
        computeFlows();
        
    }
    private HashSet<HashSet<Integer>> vertexLabels(int numberOfTeams){
        HashSet<HashSet<Integer>> setOfSets = new HashSet<HashSet<Integer>>();
        for(int i=1; i<=numberOfTeams; i++){
            for(int j=1; j<=numberOfTeams; j++){
                HashSet<Integer> set = new HashSet<Integer>();
                set.add(i);
                set.add(j);
                setOfSets.add(set);
            }
        }
        HashSet<Integer> set = new HashSet<Integer>();
        set.add(0);
        setOfSets.add(set);
        set = new HashSet<Integer>();
        set.add(numberOfTeams+1);
        setOfSets.add(set);
        return setOfSets;
    }
    private void constructGraphs(){
        HashSet<HashSet<Integer>> setOfSets = vertexLabels(this.numberOfTeams);
        this.vertexLabelToIdListOfHashMaps = new ArrayList<HashMap<HashSet<Integer>, Integer>>(this.numberOfTeams);
        for(int i=0; i<this.numberOfTeams; i++){
            vertexLabelToIdListOfHashMaps.add(i, new HashMap<HashSet<Integer>, Integer>());
        }
        for(int i=0; i<this.numberOfTeams; i++){
            int j = 0;
            for(HashSet<Integer> set: setOfSets){
                if(!set.contains(i+1)){
                    this.vertexLabelToIdListOfHashMaps.get(i).put(set, j);
                    j++;
                }
            }
        }
        for(int i=0; i<this.numberOfTeams; i++){
            this.flowNetworks[i] = new FlowNetwork(this.vertexLabelToIdListOfHashMaps.get(i).size());
        }
        for(int i=0; i<this.numberOfTeams; i++){
            for(HashSet<Integer> set: setOfSets){
                if(set.contains(0)){
                    for(HashSet<Integer> set2: setOfSets){
                        if(!set2.contains(i+1) && set2.size() == 2){
                            int v = this.vertexLabelToIdListOfHashMaps.get(i).get(set);
                            int w = this.vertexLabelToIdListOfHashMaps.get(i).get(set2);
                            Integer[] temp = set2.toArray(new Integer[set2.size()]);
                            FlowEdge edge = new FlowEdge(v, w, this.gamesRemainingBetweenTeams[temp[0]-1][temp[1]-1]);
                            this.flowNetworks[i].addEdge(edge);
                        }
                    }
                }
                else if(set.size() == 2 && !set.contains(i+1)){
                    Integer[] temp = set.toArray(new Integer[set.size()]);
                    for(HashSet<Integer> set2: setOfSets){
                        if(set2.size() == 1 && (set2.contains(temp[0]) || set2.contains(temp[1]))){
                            int v = this.vertexLabelToIdListOfHashMaps.get(i).get(set);
                            int w = this.vertexLabelToIdListOfHashMaps.get(i).get(set2);
                            FlowEdge edge = new FlowEdge(v, w, Double.POSITIVE_INFINITY);
                            this.flowNetworks[i].addEdge(edge);
                        }
                    }
                }
                else if(set.size() == 1 && !set.contains(this.numberOfTeams+1) && !set.contains(i+1)){
                    Integer[] temp = set.toArray(new Integer[set.size()]);
                    for(HashSet<Integer> set2: setOfSets){
                        if(set2.contains(this.numberOfTeams+1)){
                            int v = this.vertexLabelToIdListOfHashMaps.get(i).get(set);
                            int w = this.vertexLabelToIdListOfHashMaps.get(i).get(set2);
                            //System.out.println("wins[" + i + "]: " + this.wins[i] + " rem[" + i + "]: " + this.remainingGames[i] + " wins[" + (temp[0]-1) + "]: " + this.wins[temp[0]-1]);
                            //System.out.println("v: " + set + " w: " + set2 + " weight: " + (this.wins[i]+this.remainingGames[i]-this.wins[temp[0]-1]));
                            FlowEdge edge = new FlowEdge(v, w, Math.max(0, this.wins[i]+this.remainingGames[i]-this.wins[temp[0]-1]));
                            this.flowNetworks[i].addEdge(edge);
                        }
                    }
                }
            }
        }
    }
    private void computeFlows(){
        for(int i=0; i< this.numberOfTeams; i++){
            FlowNetwork teamGraph = this.flowNetworks[i];
            HashSet<Integer> set = new HashSet<Integer>();
            set.add(0);
            int s = this.vertexLabelToIdListOfHashMaps.get(i).get(set);
            set.clear();
            set.add(this.numberOfTeams+1);
            int t = this.vertexLabelToIdListOfHashMaps.get(i).get(set);
            FordFulkerson ff = new FordFulkerson(teamGraph, s, t);
            this.fordFulkersonList[i] = ff;
        }
    }
    public int numberOfTeams(){   
        // number of teams
        return this.numberOfTeams;
    }
    public Iterable<String> teams(){   
        // all teams
        return this.teams;
    }
    public int wins(String team){ 
        // number of wins for given team
        return this.wins[this.teamToId.get(team)];
    }
    public int losses(String team){  
        // number of losses for given team
        return this.losses[this.teamToId.get(team)];
    }
    public int remaining(String team){  
        // number of remaining games for given team
        return this.remainingGames[this.teamToId.get(team)];
    }
    public int against(String team1, String team2){ 
        // number of remaining games between team1 and team2
        return this.gamesRemainingBetweenTeams[this.teamToId.get(team1)][this.teamToId.get(team2)];
    }
    public boolean isEliminated(String team){
        // is given team eliminated?
        if(isTrivialEliminated(team)){
            return true;
        }
        int teamId = this.teamToId.get(team);
        FordFulkerson teamFordFulkerson = this.fordFulkersonList[teamId];
        FlowNetwork teamFlowNetwork = this.flowNetworks[teamId];
        //System.out.println(teamFlowNetwork);
        for(FlowEdge edge: teamFlowNetwork.adj(0)){
            if((int)edge.capacity() != (int)edge.flow()){
                return true;
            }
        }
        return false;
    }
    private boolean isTrivialEliminated(String team){
        int maxWins = this.wins(team)+this.remaining(team);
        for(String team2: this.teams){
            if(maxWins < this.wins(team2)){
                return true;
            }
        }
        return false;
    }
    public Iterable<String> certificateOfElimination(String team){
        // subset R of teams that eliminates given team; null if not eliminated
        HashSet<String> eliminatingTeamsList = new HashSet<String>();
        if(isTrivialEliminated(team)){
            int maxWins = this.wins(team)+this.remaining(team);
            for(String team2: this.teams){
                if(maxWins < this.wins(team2)){
                    eliminatingTeamsList.add(team2);
                }
            }
            return eliminatingTeamsList;
        }
        eliminatingTeamsList.clear();
        int teamId = this.teamToId.get(team);
        HashSet<Integer> set = new HashSet<Integer>();
        FordFulkerson teamFordFulkerson = this.fordFulkersonList[teamId];
        FlowNetwork teamGraph = this.flowNetworks[teamId];
        //System.out.println(teamGraph);
        for(int i=0; i<this.numberOfTeams; i++){
            String otherTeam = this.teams.get(i);
            if(i != teamId){
                set.add(i+1);
                int v = this.vertexLabelToIdListOfHashMaps.get(teamId).get(set);
                if(teamFordFulkerson.inCut(v)){
                    eliminatingTeamsList.add(otherTeam);
                }
                set.clear();
            }
        }
        if(eliminatingTeamsList.isEmpty()){
            return null;
        }
        //System.out.println("valid certificate: " + isValidCertificate(team, eliminatingTeamsList));
        return eliminatingTeamsList;
    }
    private boolean isValidCertificate(String eliminatedTeam, Iterable<String> certificate){
        int w = 0;
        int g = 0;
        int count = 0;
        for(String team: certificate){
            w += this.wins(team);
            count++;
            for(String team2: certificate){
                g += against(team, team2);
            }
        }
        double a = (w+g)/(count+0.0);
        return a>(this.wins(eliminatedTeam)+this.remaining(eliminatedTeam));
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
        
    }
}
