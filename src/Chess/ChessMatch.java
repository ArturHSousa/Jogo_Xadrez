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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author artuz
 */
public class ChessMatch {
    
    private int turn;
    private Color currentPlayer;
    private Board board;
	
        //Lista para ter controle de quantas peças foram capturadas e quantas estão no tabuleiro
    	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
        
	public ChessMatch() {
		board = new Board(8, 8);
                turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
	
        public int getTurn() {
            return turn;
	}

	public Color getCurrentPlayer() {
            return currentPlayer;
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSoucePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSoucePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
                nextTurn();
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
    
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
                
		return capturedPiece;
	}
   
   private void validateSoucePosition(Position position){
       if (!board.thereIsAPiece(position)){
           throw new ChessException("Não há nenhuma peça na posição de origem");
       }
       	if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
            throw new ChessException("Essa peça não é sua!");
	}
       if (!board.piece(position).isThereAnyPossibleMove()) {
           throw new ChessException("Não há movimentos possiveis para a peça escolhida");
       }
   }
   
   private void validateTargetPosition(Position source, Position target){
       if (!board.piece(source).possibleMove(target)){
           throw new ChessException("A peça escolhida não pode se mover para a posição de destino");
       }
   }
   
   private void nextTurn() {
	turn++;
	currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

   private void placeNewPiece(char column, int row, ChessPiece piece){
       board.placePiece(piece,new ChessPosition(column, row).toPosition());
       piecesOnTheBoard.add(piece);
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
