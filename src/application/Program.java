/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package application;

import Chess.ChessException;
import Chess.ChessMatch;
import Chess.ChessPiece;
import Chess.ChessPosition;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author artuz
 */
public class Program {

    //[][] mat (matriz)
    //Para a impressão funcionar deve usar um terminal colorido, no meu caso eu uso o NetBeans com o tema "Dark Nimbus" ou o GitBash
    
    public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        
                while (true) {
                    try {
                        UI.clearScreen();
                        UI.printMatch(chessMatch);
                        System.out.println();
                        System.out.print("Source: ");
                        ChessPosition source = UI.readChessPosition(sc);
                        
                        boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                        UI.clearScreen();
                        UI.printBoard(chessMatch.getPieces(), possibleMoves);
                        System.out.println();
                        System.out.print("Target: ");
                        ChessPosition target = UI.readChessPosition(sc);

                        ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
                    }
                    catch (ChessException e){
                        System.out.println(e.getMessage());
                        sc.nextLine();   
                    }
                    catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
                }
    }
    
}
