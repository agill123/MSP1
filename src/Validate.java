import java.util.*;
import java.io.*;

public class Validate {

    static int[][] distance;    // travel distance between meetings
    static boolean[][] present; // present[i][j] <-> agent i is at meeting j
    static int[] meeting;       // start time of meeting
    static int n,m,timeSlots;
    static Scanner sc;

    static void skip(int k){
        for (int i=0;i<k;i++) sc.next();
    }
    //
    // skip k input tokens
    //

    static boolean intersects(int mtg_j,int mtg_k){
        for (int i=0;i<m;i++)
            if (present[i][mtg_j] && present[i][mtg_k]) return true;
        return false;
    }
    //
    // do these two meetings intersect in time?
    //

    static boolean validate(String problem,String solution) throws IOException {
        sc           = new Scanner(new File(problem));
        n            = sc.nextInt();       // number of meetings
        m            = sc.nextInt();       // number of agents
        timeSlots    = sc.nextInt();       // number of time slots, 0 to timeslots-1
        present      = new boolean[m][n];  // present[i][j] <-> agent i attends meeting j
        meeting      = new int[n];         // start times of meetings
        for (int i=0;i<m;i++){
            skip(1);
            for (int j=0;j<n;j++) present[i][j] = sc.nextInt() == 1;
        }
        distance = new int[n][n];
        for (int i=0;i<n;i++){
            skip(1);
            for (int j=0;j<n;j++) distance[i][j] = sc.nextInt();
        }
        sc.close();
        sc = new Scanner(new File(solution));
        for (int i=0;i<n;i++) meeting[sc.nextInt()] = sc.nextInt();
        sc.close();
        for (int i=0;i<n;i++){
            if (i < 10) System.out.print(" "+ i +": "); else System.out.print(i +": ");
            for (int j=0;j<meeting[i];j++) System.out.print("-");
            System.out.println("X ("+ meeting[i] +")");
        }
        for (int i=0;i<n-1;i++)
            for (int j=i+1;j<n;j++)
                if (intersects(i,j) && Math.abs(meeting[i] - meeting[j]) < 1 + distance[i][j]){
                    System.out.println("conflict: meeting["+ i +"] and meeting["+ j +"]");
                    return false;
                }
        return true;
    }

    public static void main(String[] args)  throws IOException {
        if (args.length == 0) System.out.println("java Validate problem.txt solution.txt");
        else{
            System.out.println("Validate: "+ args[0] +" "+ args[1]);
            System.out.println(validate(args[0],args[1]));
        }
    }
}
