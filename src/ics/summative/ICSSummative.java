/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ics.summative;

import java.io.*;
import java.util.*;

/**
 *
 * @author felix
 */
public class ICSSummative {

    /**
     * @param args the command line arguments
     */
    public static Scanner input = new Scanner(System.in);
    
    public static void main(String[] args) {
        while (true) {
            Game game = new Game();
            game.startRace();

            System.out.print(" Do you want to race again? (y/n): ");
            String again = input.next().toLowerCase();

            if (!again.equals("y")) {
                System.out.println(" Thanks for playing!");
                break;
            }
        }
    }
}

// Base class (inheritance)
class Animal {
    protected String name;
    protected int position = 0;

    public Animal(String name) {
        this.name = name;
    }

    // Overloaded constructor
    public Animal(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public void move() {
        position += (int) (Math.random() * 3) + 1;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public String toString() {
        return name + " is at position " + position;
    }
}

class FastAnimal extends Animal {
    public FastAnimal(String name) {
        super(name);
    }

    @Override
    public void move() {
        position += (int) (Math.random() * 4) + 3;
    }
}

class Game {
    private Animal[] racers = new Animal[12];
    private static int raceCount = 0;
    private String[][] track = new String[12][21];

    public Game() {
        racers[0] = new FastAnimal("Rat");
        racers[1] = new Animal("Ox");
        racers[2] = new FastAnimal("Tiger");
        racers[3] = new Animal("Rabbit");
        racers[4] = new Animal("Dragon");
        racers[5] = new FastAnimal("Snake");
        racers[6] = new Animal("Horse");
        racers[7] = new Animal("Goat");
        racers[8] = new Animal("Monkey");
        racers[9] = new Animal("Rooster");
        racers[10] = new Animal("Dog");
        racers[11] = new Animal("Pig");
    }

    public void startRace() {
        raceCount++;
        System.out.println("Welcome to the Zodiac Race!");

        System.out.println("Choose your animal:");
        for (int i = 0; i < racers.length; i++) {
            System.out.println((i + 1) + ". " + racers[i].getName());
        }

        int choice = ICSSummative.input.nextInt() - 1;
        Animal playerAnimal = racers[choice];

        Animal betAnimal = placeBet();

        boolean raceOver = false;
        long startTime = System.currentTimeMillis();

        while (!raceOver) {
            for (int i = 0; i < racers.length; i++) {
                racers[i].move();
                if (racers[i].getPosition() >= 20) {
                    raceOver = true;
                }
            }

            displayTrack();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(" Race time: " + duration / 1000.0 + " seconds");

        Animal winner = getWinner();
        System.out.println(" The winner is: " + winner.getName());

        if (winner.getName().equals(playerAnimal.getName())) {
            System.out.println(" You won the race!");
        } else {
            System.out.println(" Better luck next time!");
        }

        if (winner.getName().equals(betAnimal.getName())) {
            System.out.println(" You WON your bet on " + betAnimal.getName() + "!");
        } else {
            System.out.println(" You lost your bet on " + betAnimal.getName() + ".");
        }

        saveToFile(winner.getName());
        readFromFile();
    }

    public Animal placeBet() {
        System.out.println("Place your bet! Which animal do you think will win?");
        for (int i = 0; i < racers.length; i++) {
            System.out.println((i + 1) + ". " + racers[i].getName());
        }
        int betChoice = ICSSummative.input.nextInt() - 1;
        return racers[betChoice];
    }

    public void displayTrack() {
        for (int i = 0; i < racers.length; i++) {
            for (int j = 0; j < 21; j++) {
                track[i][j] = " ";
            }
            int pos = Math.min(racers[i].getPosition(), 20);
            track[i][pos] = racers[i].getName().substring(0, 1);
        }

        for (int i = 0; i < track.length; i++) {
            System.out.print(racers[i].getName() + ": ");
            for (int j = 0; j < track[i].length; j++) {
                System.out.print(track[i][j]);
            }
            System.out.println();
        }
        System.out.println("------------------------------");
    }

    public Animal getWinner() {
        Animal winner = racers[0];
        for (Animal a : racers) {
            if (a.getPosition() > winner.getPosition()) {
                winner = a;
            }
        }
        return winner;
    }

    public void saveToFile(String winnerName) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("race_results.txt", true));
            writer.println("Race #" + raceCount + ": " + winnerName + " won!");
            writer.close();
        } catch (IOException e) {
            System.out.println("File write error.");
        }
    }

    public void readFromFile() {
        System.out.println("Previous Race Results:");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("race_results.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(" - " + line);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("No past results found.");
        }
    }
}