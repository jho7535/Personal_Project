import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    public static void main(String[] args) {
        GameBoard gb = new GameBoard();
        gb.play();
    }
}

class GameBoard {
    final int MAX = 9;
    private boolean[][] boolMap = new boolean[MAX][MAX];
    private int[][] intMap = new int[MAX][MAX];
    private char[][] userMap = new char[MAX][MAX];

    public void initBoolMap() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int row = random.nextInt(MAX);
            int col = random.nextInt(MAX);

            if (boolMap[row][col])
                i--;
            else
                boolMap[row][col] = true;
        }
    }

    public boolean isMine(int row, int col) {
        return boolMap[row][col];
    }

    public void initIntMap() {
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++)
                intMap[i][j] = countMine(i, j);
        }
    }

    public int countMine(int row, int col) {
        int count = 0;

        if (checkRange(row - 1, col - 1) && isMine(row - 1, col - 1))
            count++;
        if (checkRange(row - 1, col) && isMine(row - 1, col))
            count++;
        if (checkRange(row - 1, col + 1) && isMine(row - 1, col + 1))
            count++;
        if (checkRange(row, col - 1) && isMine(row, col - 1))
            count++;
        if (checkRange(row, col + 1) && isMine(row, col + 1))
            count++;
        if (checkRange(row + 1, col - 1) && isMine(row + 1, col - 1))
            count++;
        if (checkRange(row + 1, col) && isMine(row + 1, col))
            count++;
        if (checkRange(row + 1, col + 1) && isMine(row + 1, col + 1))
            count++;

        return count;
    }

    public boolean checkRange(int row, int col) {
        return row >= 0 && row < MAX && col >= 0 && col < MAX;
    }

    public void initUserMap() {
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++)
                userMap[i][j] = '■';
        }
    }

    public void printUI() {
        System.out.println();
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++)
                System.out.print(userMap[i][j] + "  ");
            System.out.println();
        }
    }

    public void play() {
        initBoolMap();
        initIntMap();
        initUserMap();

        int currentRow, currentCol, command;
        boolean flag = true;
        while (flag) {
            printUI();

            if (winGame()) {
                System.out.println("\n\n게임 승리");
                break;
            }

            Scanner input = new Scanner(System.in);
            System.out.print("\n\nx 좌표 값 입력: ");
            currentCol = input.nextInt() - 1;
            System.out.print("y 좌표 값 입력: ");
            currentRow = input.nextInt() - 1;

            if (!checkRange(currentRow, currentCol)) {
                System.out.println("\n\n좌표 값 재입력");
                continue;
            }

            if (userMap[currentRow][currentCol] == '■') {
                userMap[currentRow][currentCol] = '*';
                printUI();
                System.out.println("\n\n1: 클릭, 2: 깃발, 3: 뒤로 가기");
                command = input.nextInt();
                switch (command) {
                    case 1 -> flag = click(currentRow, currentCol);
                    case 2 -> userMap[currentRow][currentCol] = '†';
                    case 3 -> userMap[currentRow][currentCol] = '■';
                }

            } else if (userMap[currentRow][currentCol] == '†') {
                userMap[currentRow][currentCol] = '*';
                printUI();
                System.out.println("\n\n1: 깃발 지우기, 2: 뒤로 가기");
                command = input.nextInt();
                switch (command) {
                    case 1 -> userMap[currentRow][currentCol] = '■';
                    case 2 -> userMap[currentRow][currentCol] = '†';
                }
            } else {
                System.out.println("\n\n좌표 값 재입력");
            }
        }


    }

    public boolean click(int row, int col) {
        if (boolMap[row][col]) {
            for (int i = 0; i < MAX; i++) {
                for (int j = 0; j < MAX; j++) {
                    System.out.print((boolMap[i][j] ? "@  " : "□  "));
                }
                System.out.println();
            }
            System.out.println("\n\n게임 오버");
            return false;
        } else {
            recur(row, col);
            return true;
        }
    }

    public void recur(int row, int col) {
        if (checkRange(row, col) && (userMap[row][col] == '*' || userMap[row][col] == '■')) {
            if (intMap[row][col] == 0) {
                userMap[row][col] = '□';
                recur(row - 1, col - 1);
                recur(row, col - 1);
                recur(row + 1, col - 1);
                recur(row - 1, col);
                recur(row + 1, col);
                recur(row - 1, col + 1);
                recur(row, col + 1);
                recur(row + 1, col + 1);
            } else if (intMap[row][col] > 0) {
                userMap[row][col] = Character.forDigit(intMap[row][col], 10);
            }
        }
    }

    public boolean winGame() {
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                if (!boolMap[i][j] && userMap[i][j] == '■')
                    return false;
            }
        }
        return true;
    }
}