package org.example;

import java.util.Arrays;
import java.util.Random;

public class PSO {
    private static final int numParticles = 50;
    private static final int numDimensions = 10;
    private static final int maxIterations = 100;
    private static final double inertiaWeight = 0.7;
    private static final double cognitiveWeight = 1.4;
    private static final double socialWeight = 1.4;
    private static final double velocityLimit = 0.5;

    public static void main(String[] args) {
        double[][] positions = initializePositions(numParticles, numDimensions);
        double[][] velocities = initializeVelocities(numParticles, numDimensions);
        double[][] pBestPositions = positions.clone();
        double[] pBestFitness = new double[numParticles];
        for (int i = 0; i < numParticles; i++) {
            pBestFitness[i] = evaluateFitness(positions[i]);
        }
        double[] gBestFitness = getGlobalBestFitness(pBestFitness);
        double[] gBestPosition = getGlobalBestPosition(pBestPositions, pBestFitness, gBestFitness);

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            for (int i = 0; i < numParticles; i++) {
                updateVelocity(velocities[i], positions[i], pBestPositions[i], gBestPosition);
                updatePosition(positions[i], velocities[i]);

                double fitness = evaluateFitness(positions[i]);
                if (fitness < pBestFitness[i]) {
                    pBestPositions[i] = positions[i].clone();
                    pBestFitness[i] = fitness;

                    if (fitness < gBestFitness[0]) {
                        gBestFitness[0] = fitness;
                        gBestPosition = positions[i].clone();
                    }
                }
            }
        }

        System.out.println("Best solution found: " + Arrays.toString(gBestPosition));
        System.out.println("Fitness: " + gBestFitness[0]);
    }

    private static double[][] initializePositions(int numParticles, int numDimensions) {
        double[][] positions = new double[numParticles][numDimensions];
        Random random = new Random();

        for (int i = 0; i < numParticles; i++) {
            for (int j = 0; j < numDimensions; j++) {
                positions[i][j] = random.nextDouble() * 10 - 5; // Valeurs aléatoires entre -5 et 5
            }
        }

        return positions;
    }

    private static double[][] initializeVelocities(int numParticles, int numDimensions) {
        double[][] velocities = new double[numParticles][numDimensions];
        Random random = new Random();

        for (int i = 0; i < numParticles; i++) {
            for (int j = 0; j < numDimensions; j++) {
                velocities[i][j] = random.nextDouble() * 2 - 1; // Valeurs aléatoires entre -1 et 1
            }
        }

        return velocities;
    }

    private static double evaluateFitness(double[] position) {
        double sum = 0;
        for (int j = 0; j < numDimensions; j++) {
            sum += Math.pow(position[j], 2);
        }
        return sum;
    }

    private static double[] getGlobalBestFitness(double[] pBestFitness) {
        double[] gBestFitness = new double[1];
        gBestFitness[0] = pBestFitness[0];

        for (int i = 1; i < pBestFitness.length; i++) {
            if (pBestFitness[i] < gBestFitness[0]) {
                gBestFitness[0] = pBestFitness[i];
            }
        }

        return gBestFitness;
    }

    private static double[] getGlobalBestPosition(double[][] pBestPositions, double[] pBestFitness, double[] gBestFitness) {
        int bestIndex = 0;
        double bestFitness = pBestFitness[0];

        for (int i = 1; i < pBestFitness.length; i++) {
            if (pBestFitness[i] < bestFitness) {
                bestFitness = pBestFitness[i];
                bestIndex = i;
            }
        }

        return pBestPositions[bestIndex].clone();
    }

    private static void updateVelocity(double[] velocity, double[] position, double[] pBestPosition, double[] gBestPosition) {
        Random random = new Random();

        for (int i = 0; i < velocity.length; i++) {
            double r1 = random.nextDouble();
            double r2 = random.nextDouble();

            velocity[i] = inertiaWeight * velocity[i]
                    + cognitiveWeight * r1 * (pBestPosition[i] - position[i])
                    + socialWeight * r2 * (gBestPosition[i] - position[i]);

            if (velocity[i] > velocityLimit) {
                velocity[i] = velocityLimit;
            } else if (velocity[i] < -velocityLimit) {
                velocity[i] = -velocityLimit;
            }
        }
    }

    private static void updatePosition(double[] position, double[] velocity) {
        for (int i = 0; i < position.length; i++) {
            position[i] += velocity[i];
        }
    }
}
