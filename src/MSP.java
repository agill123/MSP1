import java.util.*;
import java.io.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

public class MSP {

    int[][] distance; // travel distance between meetings
    int[][] attends;  // attends[i][j] = 1 <-> agent_i is at meeting_j
    IntVar span;
    int n,m,timeSlots;
    Model model;
    Solver solver;
    int[] solution;
    boolean verbose;
    //
    // declare your constrained integer variables here
    //
    IntVar meeting_times[];
    IntVar latest_meeting;

    public MSP(int n, int m, int timeSlots, int[][] distance, int[][] attends){
        solution       = new int[n];
        model          = new Model("MSP");
        solver         = model.getSolver();
        this.n         = n;
        this.m         = m;
        this.timeSlots = timeSlots;
        this.attends   = attends;

        //
        // make your constrained integer variables here
        //
        meeting_times = model.intVarArray("Meeting",n,0,timeSlots-1);
        latest_meeting=model.intVar("Latest Meeting",0,timeSlots);
        span=model.intVar("span",0,timeSlots);

        //
        // post your constraints here
        //
        model.max(latest_meeting,meeting_times).post();


        //for every agent in attends
        for(int i=0;i<m;i++){
            //compare to find all their meetings
            for(int j=0;j<n;j++){
                for(int k=0;k<n;k++){
                   //post arrival time constraint for every pair of meetings
                    //specify j less than k to prevent repeating
                    if(attends[i][j]==1 && attends[i][k]==1 && j<k){
                        //there is no >= for model.distance currently
                        //since span is 1 can exclude it from expression for same result
                        model.distance( meeting_times[k],meeting_times[j],">",distance[j][k]).post();


                    }
                }
            }
        }

        //
        // maybe use a variable and value ordering heuristic
        //
        solver.setSearch(Search.minDomUBSearch(meeting_times));
    }

    void optimize(){
       //objective function
        model.setObjective(Model.MINIMIZE,latest_meeting);
        while (solver.solve()) {
            for (int i = 0; i < n; i++) {
                System.out.print(meeting_times[i] + " ");

            }
            System.out.println(latest_meeting);
        }



    }

    void solve(){
        if (solver.solve()) {
            for (int i = 0; i < n; i++) {
                System.out.println(meeting_times[i] + " ");
            }
            System.out.println(latest_meeting);

        }
    }

    void show(){
        solver.printShortStatistics();
    }
}
