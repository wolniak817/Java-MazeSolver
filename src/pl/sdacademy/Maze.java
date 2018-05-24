package pl.sdacademy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Maze {
    private PointType[][] points;
    private Point startPoint;
    private Point endPoint;

    public Maze(String fileName) throws MazeCreationException {
        try {
            List<String> fileLines = Files.readAllLines(Paths.get(fileName));
            readPointsFromFileLines(fileLines);
        } catch (IOException e) {
            throw new MazeCreationException("Błąd odczytywania danych z pliku");
        }
    }

    private void readPointsFromFileLines(List<String> fileLines) throws MazeCreationException {
        fileLines.stream()
                .findFirst()
                .orElseThrow(() -> new MazeCreationException("Brak wierszy w pliku"));
        if (fileLines.stream()
                .map(fl -> fl.length())
                .distinct()
                .count() > 1) {
            throw new MazeCreationException("Wiersze w pliku mają różną długość");
        }

        points = new PointType[fileLines.size()][fileLines.get(0).length()];

        for (int i = 0; i < fileLines.size(); i++) {
            String fileLine = fileLines.get(i);
            PointType[] row = points[i];

            for (int j = 0; j < fileLine.length(); j++) {
                char character = fileLine.charAt(j);
                switch (character) {
                    case ' ':
                        row[j] = PointType.PATH;
                        break;
                    case '+':
                    case '-':
                    case '|':
                        row[j] = PointType.WALL;
                        break;
                    case 'S':
                        row[j] = PointType.START;
                        startPoint = new Point(j, i);
                        break;
                    case 'E':
                        row[j] = PointType.END;
                        endPoint = new Point(j, i);
                        break;
                    default:
                        throw new MazeCreationException("Niedozwolony znak w pliku");
                }
            }
        }
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public PointType getPointTypeAt(Point point) {
        try {
            return points[point.getY()][point.getX()];
        } catch(ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void printWithPoint(Point point) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int y = 0; y < points.length; y++) {
            PointType[] row = points[y];
            for (int x = 0; x < row.length; x++) {
                if(new Point(x, y).equals(point)) {
                    stringBuilder.append('#');
                } else {
                    PointType pointType = row[x];
                    switch (pointType) {
                        case PATH:
                            stringBuilder.append(' ');
                            break;
                        case WALL:
                            stringBuilder.append('+');
                            break;
                        case START:
                            stringBuilder.append('S');
                            break;
                        case END:
                            stringBuilder.append('E');
                            break;
                    }
                }
            }
            stringBuilder.append('\n');
        }
        System.out.println(stringBuilder.toString());
    }

    public PointType[][] getPoints() {
        return points;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (PointType[] row : points) {
            for (PointType pointType : row) {
                switch(pointType) {
                    case PATH:
                        stringBuilder.append(' ');
                        break;
                    case WALL:
                        stringBuilder.append('+');
                        break;
                    case START:
                        stringBuilder.append('S');
                        break;
                    case END:
                        stringBuilder.append('E');
                        break;
                }
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }
}
