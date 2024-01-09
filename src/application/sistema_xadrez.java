/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package application;

import Chess.ChessMatch;
import Chess.ChessPiece;
import Chess.ChessPosition;
import java.util.Scanner;

/**
 *
 * @author artuz
 */
public class sistema_xadrez {


    //Para a impressão funcionar deve usar um terminal colorido, no meu caso eu uso o NetBeans com o tema "Dark Nimbus"
    
    public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        
                while (true) {
                    UI.printBoard(chessMatch.getPieces());
                    System.out.println();
                    System.out.print("Source: ");
                    ChessPosition source = UI.readChessPosition(sc);
                    
                    System.out.println();
                    System.out.print("Target: ");
                    ChessPosition target = UI.readChessPosition(sc);
                    
                    ChessPiece capturedPiece = chessMatch.perforChessMove(source, target);
                }
    }
    
}
