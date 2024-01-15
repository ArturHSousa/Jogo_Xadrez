/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess;

import Chess.pieces.King;
import Chess.pieces.Rook;
import boardgame.Board;
import boardgame.Piece;
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
               mat[i][j] = (ChessPiece) board.piece(i, j);
           }          
    }
       return mat;
  }
    
   public ChessPiece perforChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){
       Position source = sourcePosition.toPosition();
       Position target = targetPosition.toPosition();
       validadeSoucePosition(source);
       validadeTargetPosition(source, target);
       Piece capturedPiece = makeMove(source, target);
       return (ChessPiece)capturedPiece;
   }
                        
   private Piece makeMove(Position source, Position target){        
       Piece p = board.removePiece(source); //souce = posição de origem
       Piece capturedPiece = board.removePiece(target);
       board.placePiece(p, target); //target = posição de destino
       return capturedPiece;
   }
   
   private void validadeSoucePosition(Position position){
       if (!board.thereIsAPiece(position)){
           throw new ChessException("Não há nenhuma peça na posição de origem");
       }
       if (!board.piece(position).isThereAnyPossibleMove()) {
           throw new ChessException("Não a movimentos possiveis para a peça escolhida");
       }
   }
   
   private void validadeTargetPosition(Position source, Position target){
       if (!board.piece(source).possibleMove(target)){
           throw new ChessException("A peça escolhida não pode se mover para a posição de destino");
       }
   }
   
   private void placeNewPiece(char column, int row, ChessPiece piece){
       board.placePiece(piece,new ChessPosition(column, row).toPosition());
   }
   
   private void initialSetup(){

       placeNewPiece('c', 1, new Rook(Color.WHITE, board));
        placeNewPiece('c', 2, new Rook(Color.WHITE, board));
        placeNewPiece('d', 2, new Rook(Color.WHITE, board));
        placeNewPiece('e', 2, new Rook(Color.WHITE, board));
        placeNewPiece('e', 1, new Rook(Color.WHITE, board));
        placeNewPiece('d', 1, new King(Color.WHITE, board));

        placeNewPiece('c', 7, new Rook(Color.BLACK, board));
        placeNewPiece('c', 8, new Rook(Color.BLACK, board));
        placeNewPiece('d', 7, new Rook(Color.BLACK, board));
        placeNewPiece('e', 7, new Rook(Color.BLACK, board));
        placeNewPiece('e', 8, new Rook(Color.BLACK, board));
        placeNewPiece('d', 8, new King(Color.BLACK, board));
   }


}
