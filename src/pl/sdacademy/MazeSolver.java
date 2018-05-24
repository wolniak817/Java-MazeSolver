package pl.sdacademy;

import java.util.*;

public class MazeSolver {
    private Maze maze;
    private Map<Point, State> stateByPoint;

    public MazeSolver(Maze maze,Point point) {
        this.maze = maze;
        this.stateByPoint = new HashMap<>();
        this.maze.setStartPoint(point);
    }

    public List<Point> solve() {
        Point startPoint = maze.getStartPoint();
        State state = new State(startPoint, null, getScoreFnValue(startPoint));
        stateByPoint.put(startPoint, state);

        while(!state.getPoint().equals(maze.getEndPoint())) {
            takeALookAround(state.getPoint());
            state.visit();
            state = findBestUnvisitedState();
        }

        List<Point> result = new ArrayList<>();
        while(!state.getPoint().equals(startPoint)) {
            result.add(state.getPoint());
            state = state.getOrigin();
        }
        result.add(startPoint);
        Collections.reverse(result);

//        result.forEach(p -> {
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    maze.printWithPoint(p);
//                });

        return result;
    }

    private State findBestUnvisitedState() {
        return stateByPoint.values()
                .stream()
                .filter(s -> !s.isVisited())
                .min((s1, s2) -> s1.getScoreFnValue() - s2.getScoreFnValue())
                .get();
    }

    private void takeALookAround(Point point) {
        List<Point> surrounding = Arrays.asList(
                new Point(point.getX(), point.getY() - 1),
                new Point(point.getX(), point.getY() + 1),
                new Point(point.getX() - 1, point.getY()),
                new Point(point.getX() + 1, point.getY())
        );
        for (Point surroundingPoint : surrounding) {
            PointType pointType = maze.getPointTypeAt(surroundingPoint);
            if(pointType != null && pointType != PointType.WALL
                    && !stateByPoint.containsKey(surroundingPoint)) {
                int scoreFnValue = getScoreFnValue(surroundingPoint);
                State state = new State(surroundingPoint, stateByPoint.get(point), scoreFnValue);
                stateByPoint.put(surroundingPoint, state);
            }
        }
    }

    private int getScoreFnValue(Point point) {
        Point endPoint = maze.getEndPoint();
        return Math.abs(point.getX() - endPoint.getX())
                + Math.abs(point.getY() - endPoint.getY());
    }
}
