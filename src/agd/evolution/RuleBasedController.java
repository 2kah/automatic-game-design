package agd.evolution;

import agd.gridgame.Action;
import agd.gridgame.Constants;
import agd.gridgame.Controller;
import agd.gridgame.GameState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Oct 9, 2008
 * Time: 5:15:43 PM
 */
public class RuleBasedController implements Recombinable, Controller, Constants {

    final int maxDepth = 3; 
    final int numberOfRules = 10;
    // the model
    private List<Rule> rules = new ArrayList<Rule>();
    private direction defaultAction;


    public RuleBasedController() {
        for (int i = 0; i < numberOfRules; i++) {
            rules.add (new Rule ());
        }
        defaultAction = direction.values()[(int) Math.random () * direction.values().length];
    }

    public RuleBasedController(List<Rule> rules, direction defaultAction) {

        this.rules = rules;
        this.defaultAction = defaultAction;
    }


    public RuleBasedController copy() {
        List<Rule> rulesCopy = new ArrayList<Rule>(rules.size());
        for (int i = 0; i < rules.size(); i++) {
            rulesCopy.add(rules.get(i).copy());
        }
        return new RuleBasedController(rulesCopy, defaultAction);
    }

    public void mutate() {
        double p = 1;
        while (Math.random() < p) {
            Rule rule = rules.get((int) Math.random() * rules.size());
            rule.mutate();
            p *= 0.8;
        }
        if (Math.random () < 0.1) {
            defaultAction = direction.values()[(int) Math.random () * direction.values().length];
        }
    }

    public Action control(GameState.Description state) {
        if (state == null) {
            throw new RuntimeException ("STATE");
        }
        Action action = new Action ();
        for (Rule rule : rules) {
            if (rule.evaluate(state)) {
                action.movement = rule.action;
                return action;
            }
        }
        action.movement = defaultAction;
        return action;
    }

    public void reset() {
    }

    private Node getNewNode(int depth) {
        Node node;
        boolean onlyTerminalsAllowed = depth >= maxDepth;
        node = getNewNode (onlyTerminalsAllowed, depth);
        return node;
    }

    private Node getNewNode (boolean terminal, int depth) {
        int type = (int) (Math.random () * (terminal ? 1 : 3));
        switch (type) {
            case 0:
                return new Probe ();
            case 1:
                return new And (depth);
            case 2:
                return new Or (depth);
            default:
                throw new RuntimeException ("Unknown node type");
        }
    }


    public RuleBasedController recombine(Recombinable other) {
        RuleBasedController offspring = (RuleBasedController) other.copy ();
        for (int i = 0; i < offspring.rules.size (); i++) {
            if (Math.random () < 0.5) {
                offspring.rules.set(i, this.rules.get (i).copy());
            }
        }
        if (Math.random () < 0.5) {
            offspring.defaultAction = this.defaultAction; 
        }
        return offspring;
    }

    class Rule {
        Node root;
        direction action;
        int size;

        public Rule () {
            size = 0;
            action = direction.values()[(int) Math.random () * direction.values().length];
            root = getNewNode (0);
        }

        public Rule copy() {
            Rule copy = new Rule();
            copy.root = root.copy();
            copy.action = action;
            copy.size = this.size;
            return copy;
        }

        public void mutate() {
            if (Math.random() < (1.0 / root.size())) {
                root = getNewNode(0);
            } else {
                root.mutate();
            }
            this.size = root.size ();
        }

        public boolean evaluate (GameState.Description state) {
            return root.evaluate (state);
        }

        public String toString () {
            return action + " if " + root;
        }

    }


    abstract class Node {
        Node[] children = new Node[2];
        int depth;
        abstract public boolean evaluate(GameState.Description state);

        public Node (){}

        public void mutate() {
            if (Math.random() < 0.2)
                children[(int) (Math.random() * children.length)] = getNewNode(depth);
            else
                children[(int) (Math.random() * children.length)].mutate();
        }

        public abstract Node copy ();
        public int size() {
            int size = 1;
            for (Node child : children) {
                size += child.size();
            }
            return size;
        }
    }

    class And extends Node {
        public And (int depth) {
            this.depth = depth;
            children[0] = getNewNode (depth + 1);
            children[1] = getNewNode (depth + 1);
        }
        public boolean evaluate(GameState.Description state) {
            return children[0].evaluate(state) && children[1].evaluate(state);
        }
        public Node copy () {
            And other = new And (depth);
            other.children[0] = this.children[0].copy ();
            other.children[1] = this.children[1].copy ();
            return other;
        }
        public String toString () {
            return "(" + children[0] + " and " + children[1] + ")";
        }

    }

    class Or extends Node {
        public Or (int depth) {
            this.depth = depth;
            children[0] = getNewNode (depth + 1);
            children[1] = getNewNode (depth + 1);
        }
        public boolean evaluate(GameState.Description state) {
            return children[0].evaluate(state) || children[1].evaluate(state);
        }
        public Node copy () {
            Or other = new Or (depth);
            other.children[0] = this.children[0].copy ();
            other.children[1] = this.children[1].copy ();
            return other;
        }
        public String toString () {
            return "(" + children[0] + " or " + children[1] + ")";
        }
    }

    class Probe extends Node {
        int x;
        int y;
        int color;

        public Probe() {
            children = new Node[0];
            mutate();   
        }

        public Probe(int x, int y, int what) {
            children = new Node[0];
            this.x = x;
            this.y = y;
            this.color = what;
        }

        public boolean evaluate(GameState.Description state) {
            int[] position = state.getAgentPosition();
            return state.thingThere(color, position[0] + x, position[1] + y);
        }

        public Node copy() {
            return new Probe(x, y, color);
        }

        public void mutate() {
            x = (int) (Math.random() * 5) - 2;
            y = (int) (Math.random() * 5) - 2;
            color = (int) (Math.random() * 3);
        }
        public String toString () {
            return "probe(" + x + "," + y + ":" + colorNames[color] + ")";
        }

    }

    public String toString () {
        StringBuffer sb = new StringBuffer ("RuleBasedController, " + numberOfRules + " rules, default " + defaultAction);
        Iterator<Rule> it = rules.iterator ();
        int count = 0;
        while (it.hasNext ()) {
            String ruleDescription = "Rule " + count + ": " + it.next ();
            count++;
        }
        return sb.toString ();
    }
}
