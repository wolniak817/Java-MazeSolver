package pl.sdacademy;

public class State {
    private Point point;
    private State origin;
    private boolean visited;
    private int scoreFnValue;

    public State(Point point, State origin, int scoreFnValue) {
        this.point = point;
        this.origin = origin;
        this.scoreFnValue = scoreFnValue;
        this.visited = false;
    }

    public Point getPoint() {
        return point;
    }

    public State getOrigin() {
        return origin;
    }

    public boolean isVisited() {
        return visited;
    }

    public int getScoreFnValue() {
        return scoreFnValue;
    }

    public void visit() {
        this.visited = true;
    }
}
