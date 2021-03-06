import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class minimizeDFA {
    public static void main(String[] args) throws FileNotFoundException {
        // Section 1
        List<Integer> edges = new ArrayList<>();
        List<StateNode> states = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        System.out.println("Provide a dfa file to minimize: ");
        String userInput = in.nextLine();
        File file = new File("../" + userInput);
        Scanner f = new Scanner(file);
        f.next(); // removes top left mark
        for(String s : f.nextLine().split(" ")){
            edges.add(Integer.parseInt(s));
        }
        while (f.hasNextLine()){
            String[] arr = f.nextLine().split(" ");
            int state = Integer.parseInt(arr[0]);
            StateNode thisNode = new StateNode(state);
            for(int i = 1; i < arr.length; i++){
                thisNode.children.put(edges.get(i-1), Integer.parseInt(arr[i]));
            }
            states.add(thisNode);
        }

        // Section 2

        List<Pair> grid = new ArrayList<>();
        List<Pair> marked = new ArrayList<>();
        for(StateNode node : states){
            int x = node.val;
            for(StateNode b : states){
                int y = b.val;
                if(x != y && !grid.contains(new Pair(String.valueOf(x),String.valueOf(y)))){
                    grid.add(new Pair(String.valueOf(x),String.valueOf(y)));
                    if((x.contains('F') && !y.contains('F'))||(!x.contains('F') && y.contains('F'))){
                        marked.add(new Pair(String.valueOf(x),String.valueOf(y)));
                    }
                }
            }
        }
        // Section 3
        for(int i = 0; i < grid.size() - marked.size(); i++){
            for(Pair p : grid){
                if(!marked.contains(p) && p.size > 0){
                    Pair temp = new Pair(p.x, p.y);
                    String oldx = temp.x;
                    String oldy = temp.y;
                    for(int edge : edges){
                        int newy = states.get(Integer.parseInt(String.valueOf(oldy.charAt(0)))).children.get(edge);
                        int newx = states.get(Integer.parseInt(String.valueOf(oldx.charAt(0)))).children.get(edge);

                        if(marked.contains(new Pair(String.valueOf(newx), String.valueOf(newy)))){
                            marked.add(p);
                        }
                    }

                }
            }
        }

        // Section 4

        marked = new ArrayList<Pair>(new HashSet<>(marked));
        for(Pair x : marked){
            grid.remove(x);
        }
        //Section 5

        Map<Integer, Set<Integer>> toDelete = new HashMap<>();
        for(Pair p : grid){
            Pair temp = new Pair(p.x, p.y);
            String first = temp.x;
            String second = temp.y;
            int i = Integer.parseInt(second);
            int j = Integer.parseInt(first);


            if(!toDelete.containsKey(j) || toDelete.containsKey(i)){
                if(!toDelete.containsKey(j) && toDelete.containsKey(i)){
                    toDelete.put(i, new HashSet<>());
                    toDelete.get(i).add(j);
                }else if(toDelete.containsKey(j)){
                    for(int a : toDelete.keySet()) {
                        toDelete.get(a).add(i);
                    }
                }else if(toDelete.containsKey(i)){
                    for(int a : toDelete.keySet()) {
                        toDelete.get(a).add(j);
                    }
                }
            }
            if(toDelete.containsKey(i) && toDelete.containsKey(j)) {
                for (int x : toDelete.get(j)) {
                    toDelete.get(i).add(j);
                }
                toDelete.remove(j);
            }else if(toDelete.containsKey(i)){
                toDelete.get(i).add(j);
            }else{
                toDelete.get(j).add(i);
            }
        }

        // Section 6
        Set<Integer> ds = new HashSet<>();
        for(int x : toDelete.keySet()){
            for(int y : toDelete.get(x)){
                ds.add(y);
            }
        }
        List<Integer> deleteStates = new ArrayList<Integer>(ds);
        deleteStates.sort();
        for (int i : deleteStates){
            for (int edge : edges){
                int stc = states.get(i).children.get(edge);
                if(deleteStates.contains(stc)){
                    for(int x : toDelete.keySet()){
                        if(toDelete.get(x).contains(i)){
                            if (i < states.size() && x < states.size()){
                                states.get(i).children.put(edge, states.get(x).val);
                            }
                        }
                    }
                }
            }
            states.remove(i);
        }

        // Section 7
        System.out.print("             ");
        for(int x : edges){
            System.out.print(x + "   ");
        }
        System.out.println();
        for(StateNode x : states){
            System.out.print("state: " + x.val);
            System.out.print("  | ");
            for(int y : x.children.values()){
                System.out.print(y + "   ");
            }
            System.out.println();
        }
        System.out.println("where: ");
        for(int x : toDelete.keySet()){
            System.out.println(x + " maps to ");
            for (int y : toDelete.get(x)){
                System.out.print(y + " : ");
            }
            System.out.println();
        }
    }

    private static class StateNode {
        int val;
        Map<Integer, Integer> children;

        public StateNode(int val) {
            this.val = val;
            children = new HashMap<>();
        }

        public StateNode(int val, Map<Integer, Integer> children) {
            this.val = val;
            this.children = children;
        }
    }

    private static class Pair{
        final int size = 2;
        String x;
        String y;

        public Pair(String x, String y) {
            this.x = x;
            this.y = y;
        }
    }

}
