package pt.josealm3ida.aoc.restroom_redoubt;

import java.util.ArrayList;
import java.util.List;

import pt.josealm3ida.aoc.utils.Coord;
import pt.josealm3ida.aoc.utils.Direction;
import pt.josealm3ida.aoc.utils.Utils;

public class Main {

   private static int SPACE_WIDTH = 101;
   private static int SPACE_HEIGHT = 103;
   private static int N_SECONDS = 100;

   public static void main(String[] args) {
      List<Robot> robots = Utils.readLines("input/restroom-redoubt.txt").stream().map(l -> Utils.readAllIntegersInString(l)).map(l -> new Robot(new Coord(l.get(0), l.get(1)), new Coord(l.get(2), l.get(3)))).toList();
      System.out.println(part1(robots));
      System.out.println(part2(robots));
   }

   private static int part1(List<Robot> robots) {

      List<Coord> finalPositions = robots.stream().map(r -> r.getRobotAfterNSeconds(N_SECONDS)).map(Robot::p).toList();
      int safetyFactor = 1;
      Coord midPoint = new Coord(SPACE_WIDTH / 2, SPACE_HEIGHT / 2);
      for (int i = 0; i < 4; i++) {
         final int q = i;
         List<Coord> positionsInQuadrant = finalPositions.stream().filter(c -> c.getQuadrant(midPoint) == q).toList();
         safetyFactor *= positionsInQuadrant.size();
      }

      return safetyFactor;
   }
   
   private static int part2(final List<Robot> robots) {
      List<Robot> currRobots = new ArrayList<>(robots);

      int i;
      for (i = 1; i <= N_SECONDS * 100; i++) {
         currRobots = currRobots.stream().map(r -> r.getRobotAfterNSeconds(1)).toList();
         List<Coord> positions = currRobots.stream().map(Robot::p).toList();
         Coord midPoint = new Coord(SPACE_WIDTH / 2, SPACE_HEIGHT / 2);

         if (!positions.contains(midPoint)) {
            continue;
         }

         boolean allNeighborsOccupied = true;
         for (Direction d : Direction.values()) {
            int x = d.calcNextCol(midPoint.x());
            int y = d.calcNextRow(midPoint.y());
            
            if (!positions.contains(new Coord(x, y))) {
               allNeighborsOccupied = false;
               break;
            }
         }

         if (allNeighborsOccupied) {
            break;
         }

      }

      prettyPrint(currRobots.stream().map(Robot::p).toList());
      return i;
   }

   private static void prettyPrint(List<Coord> coords) {
      for (int y = 0; y < SPACE_HEIGHT; y++) {
         for (int x = 0; x < SPACE_WIDTH; x++) {
            System.out.print(coords.contains(new Coord(x, y)) ? "#" : ".");
         }
         System.out.println();
      }
   }

   private record Robot(Coord p, Coord v) {

      public Robot getRobotAfterNSeconds(int n) {
         int currX = p.x();
         int currY = p.y();

         for (int i = 0; i < n; i++) {
            int newX = currX + v.x();
            if (newX < 0) {
               newX += SPACE_WIDTH;
            } else if (newX > SPACE_WIDTH - 1) {
               newX -= SPACE_WIDTH;
            }

            int newY = currY + v.y();
            if (newY < 0) {
               newY += SPACE_HEIGHT;
            } else if (newY > SPACE_HEIGHT - 1) {
               newY -= SPACE_HEIGHT;
            }

            currX = newX;
            currY = newY;
         }

         return new Robot(new Coord(currX, currY), v);
      }

   }

}
