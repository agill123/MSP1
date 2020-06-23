import java.io.*;
import java.util.*;

public class Go {

    static int[][] distance; // travel distance between meetings
    static int[][] attends;  // attends[i][j] = 1 <-> agent_i is at meeting_j
    static int n,m,timeSlots;
    static Scanner sc;

    static void skip(int k){
        for (int i=0;i<k;i++) sc.next();
    }

    static void readMSP(String fname) throws IOException {
        sc           = new Scanner(new File(fname));
        n            = sc.nextInt();       // number of meetings
        m            = sc.nextInt();       // number of agents
        timeSlots    = sc.nextInt();       // number of time slots
        attends      = new int[m][n];      // attends[i][j] = 1 <-> agent i is attends meeting j
        distance     = new int[n][n];

        for (int i=0;i<m;i++){
            skip(1);
            for (int j=0;j<n;j++) attends[i][j] = sc.nextInt();
        }
        for (int i=0;i<n;i++){
            skip(1);
            for (int j=0;j<n;j++) distance[i][j] = sc.nextInt();
        }
        sc.close();
    }

    public static void main(String[] args)  throws IOException {
        if (args.length == 0 || args[0].equals("-h")){
            System.out.println("Go version 20181206 \n"+
                    "> java Go fname -solve ... \n"+
                    "> java Go fname -opt ... \n"+
                    "-time INT     Set CPU time limit in seconds (default infinity) \n"+
                    "-solve        Find a first solution (default) \n"+
                    "-opt          Optimize  \n" +
                    "-trace        trace \n"+
                    "-brief        not verbose \n"+
                    "-h            Print this help message");
            return;
        }
        String fname = args[0];
        boolean solve = true, optimize = false, trace = false, verbose = true;;
        long timeLimit = -1;
        for (int i=1;i<args.length;i++){
            if (args[i].equals("-time") || args[i].equals("-t")) timeLimit = 1000 * (long)Integer.parseInt(args[i+1]);
            if (args[i].equals("-solve")) solve = true;
            if (args[i].equals("-opt")) optimize = true;
            if (args[i].equals("-trace")) trace = true;
            if (args[i].equals("-brief")) verbose = false;;
        }
        readMSP(args[0]);
        MSP msp = new MSP(n,m,timeSlots,distance,attends);
        if (timeLimit > 0) msp.solver.limitTime(timeLimit);
        if (trace) msp.solver.showDecisions();
        msp.verbose = verbose;
        if (optimize) msp.optimize();
        else if (solve) msp.solve();
        msp.show();
    }
}


