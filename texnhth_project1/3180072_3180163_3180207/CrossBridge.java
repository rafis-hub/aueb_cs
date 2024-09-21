import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CrossBridge {


    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> list = new ArrayList<Integer>();
        System.out.println("Welcome to cross bridge");
        System.out.println("Please enter the time of runners separated with spaces or enter!" +
                "\nEnter zero at the end of the inputs: ");
        int speed = scanner.nextInt();
        while (speed > 0) {
            list.add(speed);
            speed = scanner.nextInt();
        }
        State initialState = new State();
        initialState.setStartSide(list);
        System.out.println("InitialState = " +initialState);
        SpaceSearcher spaceSearcher = new SpaceSearcher();
        State terminalState = null;
        long start = System.currentTimeMillis();
        terminalState = spaceSearcher.BestFSClosedSet(initialState);
        long end = System.currentTimeMillis();
        if(terminalState == null)
        {
            System.out.println("Could not find solution");
        }
        else
        {
            State temp = terminalState;
            ArrayList<State> path = new ArrayList<State>();
            path.add(terminalState);
            while(temp.getFather()!=null)
            {
                path.add(temp.getFather());
                temp = temp.getFather();
            }
            Collections.reverse(path);
            System.out.println("Finished in "+path.size()+" steps!");
            for(State item : path)
            {
                System.out.println(item.toString());
            }
        }
        System.out.println("AStar search time: " + (double)(end - start) / 1000 + " sec.");
    }
}
