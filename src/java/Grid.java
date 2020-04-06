package com.sunils.data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Grid {

    private static Random random = new Random();

    private static final String CELL_FORMAT = "%02d";
    private static final String EMPTY_CELL = "  ";

    private static final int NO_OF_COLUMNS = 9;
    private static final int NO_OF_ROWS = 3;
    private static final int ROW_SPACES_ALLOWED = 4;
    private static final int COL_SPACES_ALLOWED = 2;

    private List<List<Integer>> initialize() {
        List<List<Integer>> data = new ArrayList<>();
        for (int i = 0; i < NO_OF_COLUMNS; i++) {
            int start = i * 10;
            int end = start + 10;
            List<Integer> nums = IntStream.range(start + 1, end + 1)
                    .boxed()
                    .collect(Collectors.toList());
            data.add(nums);
        }

        return data;
    }


    public List<List<String>> generate() {
        List<List<Integer>> data = this.initialize();

        List<Integer> colSpaces = IntStream.range(0, NO_OF_COLUMNS)
                .map(x -> 0)
                .boxed()
                .collect(Collectors.toList());
        List<Integer> rowSpaces = IntStream.range(0, NO_OF_ROWS)
                .map(x -> 0)
                .boxed()
                .collect(Collectors.toList());

        List<List<String>> grid = new ArrayList<>();
        grid.add(new ArrayList<>());
        grid.add(new ArrayList<>());
        grid.add(new ArrayList<>());
        for (int i = 0; i < grid.size(); i++) {
            List<String> nums = new ArrayList<>();

            for (int j = 0; j < NO_OF_COLUMNS; j++) {
                int i1 = random.nextInt(data.get(j).size());
                nums.add(String.format(CELL_FORMAT, data.get(j).get(i1)));
                data.get(j).remove(i1);
            }
            grid.set(i, nums);
        }

        addSpaces(colSpaces, rowSpaces, grid);
        if (!validate(grid))
            return null;
        return grid;
    }

    private void addSpaces(List<Integer> colSpaces, List<Integer> rowSpaces, List<List<String>> grid) {
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < ROW_SPACES_ALLOWED; j++) {
                int i1 = random.nextInt(NO_OF_COLUMNS);
                while (rowSpaces.get(i) >= ROW_SPACES_ALLOWED ||
                        colSpaces.get(i1) >= COL_SPACES_ALLOWED ||
                        grid.get(i).get(i1).equals(EMPTY_CELL)) {
                    i1 = random.nextInt(NO_OF_COLUMNS);
                }
                grid.get(i).set(i1, EMPTY_CELL);

                colSpaces.set(i1, 1 + colSpaces.get(i1));
                rowSpaces.set(i, 1 + rowSpaces.get(i));
            }
        }
    }

    private boolean validate(List<List<String>> grid) {
        for (List<String> strings : grid) {
            Set<String> row = strings.stream()
                    .filter(x -> !x.equals(EMPTY_CELL))
                    .collect(Collectors.toSet());

            if (row.size() < 5)
                return false;
            long count = strings.stream()
                    .filter(x -> x.equals(EMPTY_CELL))
                    .count();
            if (count > ROW_SPACES_ALLOWED)
                return false;
        }

        return true;
    }

    public static String prettyPrint(List<List<String>> grid, boolean status) {
        String rowFormat = "| %s | %s | %s | %s | %s | %s | %s | %s | %s |\n";
        String topBottom = String.format("---------------------%s--------------------\n", (status ? "-----" : "ERROR"));
        StringBuilder builder = new StringBuilder(topBottom);
        for (List<String> strings : grid) {
            builder.append(String.format(rowFormat, strings.toArray()));
        }
        builder.append(topBottom).append("\n");
        return builder.toString();
    }
//    public static void main(String[] args) {
//        Grid grid = new Grid();
//
//        int i = 0;
//        int count = 0;
//        while (i++ < 100000) {
//            List<List<String>> gridValue = grid.generate();
//            boolean validate = grid.validate(gridValue);
//            if(!validate)
//                count++;
////            System.out.println(prettyPrint(gridValue, validate));
//        }
//        System.out.println("ERRORS : " + count);
//    }
}
