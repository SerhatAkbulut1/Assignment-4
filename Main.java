
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    public static int gamePointController(String[][] board,int  x_coordinate, int y_coordinate){
        //Here, I used a function to check the player's total score for control purposes.
        int game_point = 0;
        if(board[x_coordinate][y_coordinate].equals("R")){
            board[x_coordinate][y_coordinate] = "X";
            game_point += 10;
        }
        if(board[x_coordinate][y_coordinate].equals("Y")){
            board[x_coordinate][y_coordinate] = "X";
            game_point += 5;
        }
        if(board[x_coordinate][y_coordinate].equals("B")){
            board[x_coordinate][y_coordinate] = "X";
            game_point -= 5;
        }
        return game_point;
    }
    public static int[] findStar(String[][] board, int row_length,int column_length){
        // I wrote this function with the aim of finding the position of the star.
        int[] star_coordinate = new int[2];
        for(int i=0; i <row_length;i++){
            for(int j=0; j <column_length;j++){
                if(board[i][j].equals("*")){
                    star_coordinate[0] = i;
                    star_coordinate[1] = j;
                }
            }
        }return star_coordinate;
    }
    public static ArrayList<String> moveReader(String move_txt){
        //I wrote this function with the aim of opening the move.txt file.
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(move_txt)))){
            ArrayList<String> player_moves = new ArrayList<String>();
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                line = line.replace(" ","");
                player_moves.add(line);
            }
            return player_moves;
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main (String[] args) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt",true))){
            try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(args[0])))) {
                ArrayList<String> boardInfo = new ArrayList<String>();            //I kept the inputted board in an array list to add it to my 2-dimensional list.
                ArrayList<String> player_moves = moveReader(args[1]); //I kept the move input in an array list to integrate the player's moves into the code.

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    line = line.replace(" ", "");
                    boardInfo.add(line);
                }
                int row_length = boardInfo.size();                                //I wrote this code to find the number of rows in our 2-dimensional list.
                int column_length = boardInfo.get(0).length();                    //I wrote this code to find the number of columns in our 2-dimensional list.
                String[][] board = new String[row_length][column_length];

                for (int i = 0; i < row_length; i++) {                            //I wrote this code to integrate the inputted board into our 2-dimensional list.
                    for (int j = 0; j < column_length; j++) {
                        String character = String.valueOf(boardInfo.get(i).charAt(j));
                        board[i][j] = character;
                    }
                }
                writer.write("Game Board:" + "\n");
                for (int i = 0; i < row_length; i++) {                             //I wrote this code to write the initial state of the game board to the output.txt file.
                    for (int j = 0; j < column_length; j++) {
                        writer.write(board[i][j] + " ");
                    }
                    writer.write("\n");
                }
                int[] star_coordinates = findStar(board, row_length, column_length); //I kept the code I wrote to find the position of the star in a list in order to integrate it into the program.
                int x_coordinate = star_coordinates[0];                              //The first element of the list gives the x-coordinate.
                int y_coordinate = star_coordinates[1];                              //The second element of the list gives the y-coordinate.
                int game_point = 0;                                                  //I created a score variable to calculate the player's score.

                String colours_sign = "GPODLFNX";                                    //I introduced the first letter of only the colors that will change in the game.
                boolean game_over = false;                                           //I defined this variable for the game to end when the ball reaches "H".
                int count = 0;                                                       //I created a variable to keep track of the number of moves the player makes before the game ends.

                for (int i = 0; i < player_moves.get(0).length(); i++) {
                    String temp_colour = "";                                         //I created a variable to keep track of the coordinates of the color in the temporary position where the colors will be swapped.
                    count++;
                    if (String.valueOf(player_moves.get(0).charAt(i)).equals("L")) {  //I wrote a code to check the validity of the position the player wants to move to.
                        int old_y_coordinate = y_coordinate;
                        if ((y_coordinate - 1) < 0) {                       //I wrote this code to bring the star back to the board in case it goes out of the board.
                            y_coordinate = column_length - 1;
                            old_y_coordinate = 0;
                        } else {
                            y_coordinate = old_y_coordinate - 1;
                        }
                        game_point += gamePointController(board, x_coordinate, y_coordinate);  //I called my function which calculates the player's score and replaces the letters "R", "B", and "Y" with "X".
                        temp_colour = board[x_coordinate][y_coordinate];
                        if ((colours_sign.contains(board[x_coordinate][y_coordinate]))) {  //I wrote this code to change the letter in the position where the star will move.
                            board[x_coordinate][y_coordinate] = "*";
                            board[x_coordinate][old_y_coordinate] = temp_colour;
                        }
                        if (board[x_coordinate][y_coordinate].equals("H")) { //I implemented the actions to be taken in case the ball falls into "H" in this code.
                            board[x_coordinate][old_y_coordinate] = " ";
                            game_over = true;
                            break;
                        }
                        if (board[x_coordinate][y_coordinate].equals("W")) { //I implemented the actions to be taken in case the ball falls into "W" in this code.
                            y_coordinate = old_y_coordinate;
                            if ((y_coordinate + 1) > column_length - 1) {  //I wrote this code to bring the star back to the board in case it goes out of the board.
                                y_coordinate = 0;
                                old_y_coordinate = column_length - 1;
                            } else {
                                y_coordinate = old_y_coordinate + 1;
                            }
                            game_point += gamePointController(board, x_coordinate, y_coordinate); //I called my function which calculates the player's score and replaces the letters "R", "B", and "Y" with "X".
                            temp_colour = board[x_coordinate][y_coordinate];
                            if ((colours_sign.contains(board[x_coordinate][y_coordinate]))) {  //I wrote this code to change the letter in the position where the star will move.
                                board[x_coordinate][y_coordinate] = "*";
                                board[x_coordinate][old_y_coordinate] = temp_colour;
                            }
                            if (board[x_coordinate][y_coordinate].equals("H")) { //I implemented the actions to be taken in case the ball falls into "H" in this code.
                                board[x_coordinate][old_y_coordinate] = " ";
                                game_over = true;
                                break;
                            }
                        }
                    }

                    if (String.valueOf(player_moves.get(0).charAt(i)).equals("R")) { //I wrote a code to check the validity of the position the player wants to move to.
                        int old_y_coordinate = y_coordinate;
                        if ((y_coordinate + 1) > column_length - 1) {  //I wrote this code to bring the star back to the board in case it goes out of the board.
                            y_coordinate = 0;
                            old_y_coordinate = column_length - 1;
                        } else {
                            y_coordinate = old_y_coordinate + 1;
                        }
                        game_point += gamePointController(board, x_coordinate, y_coordinate); //I called my function which calculates the player's score and replaces the letters "R", "B", and "Y" with "X".
                        temp_colour = board[x_coordinate][y_coordinate];
                        if ((colours_sign.contains(board[x_coordinate][y_coordinate]))) {  //I wrote this code to change the letter in the position where the star will move.
                            board[x_coordinate][y_coordinate] = "*";
                            board[x_coordinate][old_y_coordinate] = temp_colour;
                        }
                        if (board[x_coordinate][y_coordinate].equals("H")) { //I implemented the actions to be taken in case the ball falls into "H" in this code.
                            board[x_coordinate][old_y_coordinate] = " ";
                            game_over = true;
                            break;
                        }
                        if (board[x_coordinate][y_coordinate].equals("W")) { //I implemented the actions to be taken in case the ball falls into "W" in this code.
                            y_coordinate = old_y_coordinate;
                            if ((y_coordinate - 1) < 0) {       //I wrote this code to bring the star back to the board in case it goes out of the board.
                                y_coordinate = column_length - 1;
                                old_y_coordinate = 0;
                            } else {
                                y_coordinate = old_y_coordinate - 1;
                            }
                            game_point += gamePointController(board, x_coordinate, y_coordinate); //I called my function which calculates the player's score and replaces the letters "R", "B", and "Y" with "X".
                            temp_colour = board[x_coordinate][y_coordinate];
                            if ((colours_sign.contains(board[x_coordinate][y_coordinate]))) {  //I wrote this code to change the letter in the position where the star will move.
                                board[x_coordinate][y_coordinate] = "*";
                                board[x_coordinate][old_y_coordinate] = temp_colour;
                            }
                            if (board[x_coordinate][y_coordinate].equals("H")) {  //I implemented the actions to be taken in case the ball falls into "H" in this code.
                                board[x_coordinate][old_y_coordinate] = " ";
                                game_over = true;
                                break;
                            }
                        }
                    }

                    if (String.valueOf(player_moves.get(0).charAt(i)).equals("U")) { //I wrote a code to check the validity of the position the player wants to move to.
                        int old_x_coordinate = x_coordinate;
                        if ((x_coordinate - 1) < 0) {    //I wrote this code to bring the star back to the board in case it goes out of the board.
                            x_coordinate = row_length - 1;
                            old_x_coordinate = 0;
                        } else {
                            x_coordinate = old_x_coordinate - 1;
                        }
                        game_point += gamePointController(board, x_coordinate, y_coordinate); //I called my function which calculates the player's score and replaces the letters "R", "B", and "Y" with "X".
                        temp_colour = board[x_coordinate][y_coordinate];
                        if (colours_sign.contains(board[x_coordinate][y_coordinate])) {  //I wrote this code to change the letter in the position where the star will move.
                            board[x_coordinate][y_coordinate] = "*";
                            board[old_x_coordinate][y_coordinate] = temp_colour;
                        }
                        if (board[x_coordinate][y_coordinate].equals("H")) { //I implemented the actions to be taken in case the ball falls into "H" in this code.
                            board[old_x_coordinate][y_coordinate] = " ";
                            game_over = true;
                            break;
                        }
                        if (board[x_coordinate][y_coordinate].equals("W")) { //I implemented the actions to be taken in case the ball falls into "W" in this code.
                            x_coordinate = old_x_coordinate;
                            if ((x_coordinate + 1) > row_length - 1) {   //I wrote this code to bring the star back to the board in case it goes out of the board.
                                x_coordinate = 0;
                                old_x_coordinate = row_length - 1;
                            } else {
                                x_coordinate = old_x_coordinate + 1;
                            }
                            game_point += gamePointController(board, x_coordinate, y_coordinate); //I called my function which calculates the player's score and replaces the letters "R", "B", and "Y" with "X".
                            temp_colour = board[x_coordinate][y_coordinate];
                            if (colours_sign.contains(board[x_coordinate][y_coordinate])) {  //I wrote this code to change the letter in the position where the star will move.
                                board[x_coordinate][y_coordinate] = "*";
                                board[old_x_coordinate][y_coordinate] = temp_colour;
                            }
                            if (board[x_coordinate][y_coordinate].equals("H")) { //I implemented the actions to be taken in case the ball falls into "H" in this code.
                                board[old_x_coordinate][y_coordinate] = " ";
                                game_over = true;
                                break;
                            }
                        }
                    }

                    if (String.valueOf(player_moves.get(0).charAt(i)).equals("D")) { // I wrote a code to check the validity of the position the player wants to move to.
                        int old_x_coordinate = x_coordinate;
                        if ((x_coordinate + 1) > row_length - 1) {   //I wrote this code to bring the star back to the board in case it goes out of the board.
                            x_coordinate = 0;
                            old_x_coordinate = row_length - 1;
                        } else {
                            x_coordinate = old_x_coordinate + 1;
                        }
                        game_point += gamePointController(board, x_coordinate, y_coordinate); //I called my function which calculates the player's score and replaces the letters "R", "B", and "Y" with "X".
                        temp_colour = board[x_coordinate][y_coordinate];
                        if (colours_sign.contains(board[x_coordinate][y_coordinate])) {  //I wrote this code to change the letter in the position where the star will move.
                            board[x_coordinate][y_coordinate] = "*";
                            board[old_x_coordinate][y_coordinate] = temp_colour;
                        }
                        if (board[x_coordinate][y_coordinate].equals("H")) { //I implemented the actions to be taken in case the ball falls into "H" in this code.
                            board[old_x_coordinate][y_coordinate] = " ";
                            game_over = true;
                            break;
                        }
                        if (board[x_coordinate][y_coordinate].equals("W")) { //I implemented the actions to be taken in case the ball falls into "W" in this code.
                            x_coordinate = old_x_coordinate;
                            if ((x_coordinate - 1) > 0) {   //I wrote this code to bring the star back to the board in case it goes out of the board.
                                x_coordinate = row_length - 1;
                                old_x_coordinate = 0;
                            } else {
                                x_coordinate = old_x_coordinate - 1;
                            }
                            game_point += gamePointController(board, x_coordinate, y_coordinate); //I called my function which calculates the player's score and replaces the letters "R", "B", and "Y" with "X".
                            temp_colour = board[x_coordinate][y_coordinate];
                            if (colours_sign.contains(board[x_coordinate][y_coordinate])) {  //I wrote this code to change the letter in the position where the star will move.
                                board[x_coordinate][y_coordinate] = "*";
                                board[old_x_coordinate][y_coordinate] = temp_colour;
                            }
                            if (board[x_coordinate][y_coordinate].equals("H")) { //I implemented the actions to be taken in case the ball falls into "H" in this code.
                                board[old_x_coordinate][y_coordinate] = " ";
                                game_over = true;
                                break;
                            }
                        }
                    }
                }
                String player_moves_string = player_moves.get(0);
                writer.write("\n" + "Your movement is:" + "\n");
                for (int move = 0; move < count; move++) {                      //I wrote this code to write the moves made by the player to output.txt.
                    writer.write(player_moves_string.charAt(move) + " ");
                }
                writer.write("\n\n" + "Your output is:" + "\n");
                for (int i = 0; i < row_length; i++) {                          //I wrote this code to write the final state of the game board to output.txt.
                    for (int j = 0; j < column_length; j++) {
                        writer.write(board[i][j] + " ");
                    }
                    writer.write("\n");
                }
                writer.write("\n");
                if (game_over) {
                    writer.write("Game Over!" + "\n");
                }
                writer.write("Score: " + game_point + "\n");                //I wrote this code to write the total score of the player at the end of the game.
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* Serhat Akbulut
        2210356052  */

}
