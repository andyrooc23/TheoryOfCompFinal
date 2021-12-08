import java.util.*;

public class StateNode {
    int val;
    Map<Integer, StateNode> children;

    public StateNode(int val, Map<Integer, StateNode> children) {
        this.val = val;
        this.children = children;
    }
}
