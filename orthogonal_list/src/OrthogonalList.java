/**
 * 该文件主要实现了十字链表存储稀疏矩阵的操作
 *
 */

import java.util.Scanner;

class Node {
    Node(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
//        this.down = null;
//        this.right = null;
    }

    int value;
    int row;
    int col;
    Node down;
    Node right;
}

class Matrix {
    Node[] rows;
    Node[] colums;

    int rowNum;
    int columNum;
    int numbers;

    public Matrix(int rowNum, int columNum, int numbers) {
        this.rowNum = rowNum;
        this.columNum = columNum;
        this.numbers = numbers;

        this.rows = new Node[rowNum];
        this.colums = new Node[columNum];
    }
}

public class OrthogonalList {

    Scanner sc = new Scanner(System.in);

    private Matrix makeMatrix() {
        System.out.println("请输入矩阵行数和列数以及非零元素个数，以空格分开：");
        int rowNum = sc.nextInt();
        int columNum = sc.nextInt();
        int numbers = sc.nextInt();
        Matrix matrix = new Matrix(rowNum, columNum, numbers);
        return matrix;
    }

    private Matrix costructMatrix(Matrix matrix) {

        for (int i = 0; i < matrix.numbers; i++) {
            Boolean flag = true;
            int rowIndex;
            int value;
            int columnIndex;

            do {
                System.out.println("请输入非零元素的行，列，值，并以空格隔开");
                rowIndex = sc.nextInt();
                columnIndex = sc.nextInt();
                value = sc.nextInt();
                if (rowIndex > matrix.rowNum || columnIndex > matrix.columNum) {
                    flag = false;
                    System.out.println("输入有误，请重新输入");
                }
            } while (!flag);
            //  得到一个有效的输入值以后，开始生成一个节点，并放到对应的位置

            Node newNode = new Node(rowIndex, columnIndex, value);
            //// 行插入 start
            // 如果该行的第一个节点为空
            Node ched = matrix.rows[rowIndex];
            if (ched == null || ched.col > newNode.col) {
                matrix.rows[rowIndex] = newNode;
                if (ched != null) {
                    newNode.right = ched.right;
                }
            } else {
                // 如果改行第一个node 已经存在，并且该节点的列值比新节点的列值小，则找右边的节点，
                // 若是右边的节点为空，或者右边某个节点的列值小于该节点列值，则插入到该节点后面
                // 先找到前驱与后继
                Node front = matrix.rows[rowIndex], behind = matrix.rows[rowIndex].right;
                while (behind != null && behind.col < newNode.col) {
                    front = behind;
                    behind = behind.right;
                }
                front.right = newNode;
                newNode.right = behind;

            }
            //// 行插入 end

            //// 列插入 start
            ched = matrix.colums[columnIndex];
            if (ched == null || ched.row> newNode.row) {
                matrix.colums[columnIndex] = newNode;
                if (ched != null) {
                    newNode.down = ched;
                }
            } else {
                // 如果改列的第一个节点已经存在，并且小于新节点的行值，则依次沿着链表找到
                Node front = matrix.colums[columnIndex], behind = front.down;
                while (behind != null && behind.down.row < newNode.row) {
                    front = behind;
                    behind = behind.down;
                }
                front.down = newNode;
                newNode.down = behind;
            }
            //// 列插入 end
        }
        return matrix;
    }

    private int[][] get2DMatrixByRow(Matrix matrix) {

        int[][] valuesByRows = new int[matrix.rowNum][matrix.columNum];

        for (int i = 0; i < matrix.rowNum; i++) {
            for (int j = 0; j < matrix.columNum; j++) {
                valuesByRows[i][j] = 0;
            }
        }
        for (int i = 0; i < matrix.rowNum; i++) {
            for (Node node = matrix.rows[i]; node != null; node = node.right) {
                valuesByRows[node.row][node.col] = node.value;
            }
        }
        return valuesByRows;

    }

    private int[][] get2DMatrixByCol(Matrix matrix) {
        int[][] valuesByColumns = new int[matrix.rowNum][matrix.columNum];
        for (int i = 0; i < matrix.rowNum; i++) {
            for (int j = 0; j < matrix.columNum; j++) {
                valuesByColumns[i][j] = 0;
            }
        }
        for (int i = 0; i < matrix.rowNum; i++) {
            for (Node node = matrix.colums[i]; node != null; node = node.down) {
                valuesByColumns[node.row][node.col] = node.value;
            }
        }
        return valuesByColumns;
    }


    private void print2DMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "  ");
            }
            System.out.println("\n");

        }
    }

    public static void main(String[] args) {
        OrthogonalList orthogonalList = new OrthogonalList();
        Matrix matrix = orthogonalList.makeMatrix();
        matrix = orthogonalList.costructMatrix(matrix);
        int[][] _2dMatrixByRow = orthogonalList.get2DMatrixByRow(matrix);
        int[][] _2dMatrixByCol = orthogonalList.get2DMatrixByCol(matrix);
        orthogonalList.print2DMatrix(_2dMatrixByRow);
        orthogonalList.print2DMatrix(_2dMatrixByCol);

    }
}
