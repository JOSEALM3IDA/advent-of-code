package pt.josealm3ida.aoc.rednosed_reports;

import java.util.ArrayList;
import java.util.List;

import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    public static void main(String[] args) {
        List<String> lines = Utils.readLines("input/rednosed-reports.txt"); 
        
        List<List<Integer>> reports = new ArrayList<>();
        lines.forEach(l -> {
            String[] splits = l.split(" ");
            
            List<Integer> report = new ArrayList<>();
            List.of(splits).stream().forEach(s -> {
                report.add(Integer.parseInt(s));
            });

            reports.add(report);
        });

        System.out.println(part1(reports));
        System.out.println(part2(reports));
    }

    private static boolean areLevelsSafe(int l1, int l2, boolean isIncreasing) {
        if (isIncreasing && !(l1 < l2) || !isIncreasing && !(l1 > l2)) {
            return false;
        }

        int diff = Math.abs(l1 - l2);
        return diff >= 1 && diff <= 3;
    }

    private static boolean removeAndRecheck(List<Integer> report, int idxToRemove) {
        List<Integer> newReport = new ArrayList<>(report);
        newReport.remove(idxToRemove);

        return isReportSafe(newReport, false);
    }

    private static boolean isReportSafe(List<Integer> report, boolean hasTolerance) {
        int firstLevel = report.get(0);
        int secondLevel = report.get(1);

        boolean isIncreasing = firstLevel < secondLevel;
        for (int i = 0; i < report.size() - 1; i++) {
            if (!areLevelsSafe(report.get(i), report.get(i + 1), isIncreasing)) {
                if (!hasTolerance) {
                    return false;
                }

                if (i == 1 && removeAndRecheck(report, 0)) {
                    return true;
                }

                if (removeAndRecheck(report, i)) {
                    return true;
                }
                
                if (removeAndRecheck(report, i + 1)) {
                    return true;
                }

                return false;
            }
        }
      
        return true;
    }

    private static int part1(List<List<Integer>> reports) {
        int count = 0;

        for (List<Integer> report : reports) {
            if (isReportSafe(report, false)) {
                count++;
            }
        }

        return count;
    }

    private static int part2(List<List<Integer>> reports) {
        int count = 0;

        for (List<Integer> report : reports) {
            if (isReportSafe(report, true)) {
                count++;
            }
        }

        return count; 
    }
}

