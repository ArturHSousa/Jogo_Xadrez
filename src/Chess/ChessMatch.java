/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess;

import Chess.pieces.King;
import Chess.pieces.Rook;
import boardgame.Board;
import boardgame.Position;

/**
 *
 * @author artuz
 */
public class ChessMatch {
    
    private Board board;
    
   public ChessMatch(){
       board = new Board(8, 8);
       initialSetup();
   } 
   
   public ChessPiece[][] getPieces() {
       ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
       for (int i=0; i<board.getRows(); i++) {
           for (int j=0; j<board.getColumns(); j++){
               mat[i][i] = (ChessPiece) board.piece(i, j);
           }          
    }
       return mat;
  }
   
   private void initialSetup(){
       board.placePiece(new Rook(Color.WHITE, board), new Position(1, 2));
       board.placePiece(new King(Color.BLACK, board), new Position(0, 4));
   }
}
