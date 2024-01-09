/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package application;

import Chess.ChessMatch;
import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

/**
 *
 * @author artuz
 */
public class sistema_xadrez {


    //Para a impressão funcionar deve usar um terminal colorido, no meu caso eu uso o NetBeans com o tema "Dark Nimbus"
    
    public static void main(String[] args) {
        
        ChessMatch chessMatch = new Chess.ChessMatch();
                UI.printBoard(chessMatch.getPieces());
         
    }
    
}
