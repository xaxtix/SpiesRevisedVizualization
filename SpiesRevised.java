package com.company;

import java.util.*;

/**
 * Created by xaxtix on 23.11.17.
 */
public class SpiesRevised {
    public static void main(String[] args) {
        /*for (int n = 5; n < 20; n++) {
            if (n % 2 == 0) continue;
            System.out.println(n + "");
            printFor(n);
        }*/
        //printFor(7);

        printFor(19);


    }


    private static void printFor(int n) {
        System.out.println(n);
        State firstState = new State(n);
        Stack<State> statesStack = new Stack<>();
        List<State> initialState = firstState.nexStep();
        statesStack.addAll(initialState);
        while (!statesStack.isEmpty()) {
            List<State> nextStates = statesStack.pop().nexStep();
            for (State state : nextStates) {
                if (state.complete()) {
                    state.printState();
                    return;
                }
            }
            statesStack.addAll(nextStates);
        }
    }


    //  0     available
    // -1     visible
    //  1     spies
    public static class State {
        final int n;
        int currentY;
        final int matrix[][];

        public State(int n) {
            this.n = n;
            this.matrix = new int[n][n];
            currentY = 0;
        }

        public State(int n, int y, int[][] matrix) {
            this.n = n;
            this.matrix = matrix;
            currentY = y;
        }


        public List<State> nexStep() {
            ArrayList<State> nextStates = new ArrayList<>(n);
            for (int x = n - 1; x >= 0; x--)
                if (matrix[x][currentY] == 0) nextStates.add(createNextState(x));

            return nextStates;
        }

        public boolean complete() {
            return currentY == n;
        }

        private State createNextState(int x) {
            int[][] nextMatrix = new int[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    nextMatrix[i][j] = matrix[i][j];

            setSpies(x, currentY, nextMatrix);
            return new State(n, currentY + 1, nextMatrix);
        }

        public void printState() {
            printStateLine();
            System.out.println();
            printStateDebug();

           // printStateStepByStep();
        }

        private void printStateStepByStep() {
            State state = new State(n);
            for (int i = 0; i < n; i++) {
                if (i != 0) System.out.println();
                for (int j = 0; j < n; j++)
                    if (matrix[j][i] == 1) {
                        state.setSpies(j, i, state.matrix);
                        state.printStateDebug();
                        System.out.println();
                    }
            }
        }

        public void printStateDebug() {

            for (int i = 0; i < n; i++) {
                if (i != 0) System.out.println();
                for (int j = 0; j < n; j++)
                    System.out.print((matrix[j][i] == 1 ? 'S' : matrix[j][i] == -1 ? '-' : '*') + " ");
            }
        }

        public void printStateLine() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++)
                    if (matrix[j][i] == 1) stringBuilder
                            .append(j + 1)
                            .append(' ');
            }
            System.out.print(stringBuilder.toString());
        }


        private void setSpies(int x, int y, int[][] matrix) {
            matrix[x][y] = 1;
            //move bottom
            for (int i = y + 1; i < n; i++)
                matrix[x][i] = -1;

            //move left bottom
            int i = x - 1;
            int j = y + 1;
            while (i >= 0 && j < n) {
                matrix[i--][j++] = -1;
            }

            i = x + 1;
            j = y + 1;
            while (i < n && j < n) {
                matrix[i++][j++] = -1;
            }

            //create lines
            for (int y_i = 0; y_i < y; y_i++) {
                for (int x_i = 0; x_i < n; x_i++) {
                    if (matrix[x_i][y_i] == 1) {
                        int difX = x - x_i;
                        int difY = y - y_i;
                        int gcd = gcd(difX, difY);
                        if (gcd > 1) {
                            difX /= gcd;
                            difY /= gcd;
                        }

                        int sx = x + difX;
                        int sy = y + difY;
                        while (sx >= 0 && sx < n && sy < n) {
                            matrix[sx][sy] = -1;
                            sx += difX;
                            sy += difY;
                        }
                    }
                }
            }
        }
    }

    public static int gcd(int a, int b) {
        while (b != 0) {
            int tmp = a % b;
            a = b;
            b = tmp;
        }
        return a;
    }
}
