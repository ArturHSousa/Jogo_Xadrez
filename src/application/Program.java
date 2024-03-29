/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package application;

import Chess.ChessException;
import Chess.ChessMatch;
import Chess.ChessPiece;
import Chess.ChessPosition;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author artuz
 */
public class Program {

    
    //Para a impressão funcionar deve usar um terminal colorido, no meu caso eu uso o GitBash
    //[][] mat (matriz)
    public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();
        
                while (!chessMatch.getCheckMate()) {
                    try {
                        UI.clearScreen();
                        UI.printMatch(chessMatch, captured);
                        System.out.println();
                        System.out.print("Escolha sua Peça: ");
                        ChessPosition source = UI.readChessPosition(sc);
                        
                    boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                        UI.clearScreen();
                        UI.printBoard(chessMatch.getPieces(), possibleMoves);
                        System.out.println();
                        System.out.print("Escolha o destino de sua Peça: ");
                        ChessPosition target = UI.readChessPosition(sc);

                        ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
                    
                    if (capturedPiece != null) {
			captured.add(capturedPiece);
			}
                    if (chessMatch.getPromoted() != null) {
			System.out.print("Escreva a peça que deseja receber da promoção (BISPO/CAVALO/TORRE/RAINHA): ");
					String type = sc.nextLine().toUpperCase();
					while (!type.equals("BISPO") && !type.equals("CAVALO") && !type.equals("TORRE") & !type.equals("RAINHA")) {
						System.out.print("Peça Invalida! Escreva a peça que deseja receber da promoção (BISPO/CAVALO/TORRE/RAINHA: ");
						type = sc.nextLine().toUpperCase();
					}
                                        chessMatch.replacePromotedPiece(type);
			}
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
		UI.clearScreen();
		UI.printMatch(chessMatch, captured); 
    }
    
}
