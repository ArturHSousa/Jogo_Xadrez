/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess;

import Chess.pieces.Bishop;
import Chess.pieces.King;
import Chess.pieces.Knight;
import Chess.pieces.Pawn;
import Chess.pieces.Queen;
import Chess.pieces.Rook;
import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.border.Border;

/**
 *
 * @author artuz
 */
public class ChessMatch {
    
    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check;
    private boolean checkMate;
	
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

        public boolean getCheck() {
            return check;
	}
        
        public boolean getCheckMate() {
            return checkMate;
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
        
        //Testa se o movimento coloca o jogador em Check
	if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece); //Desfaz o movimento e lança a exeção
            throw new ChessException("Você não pode se colocar em CHECK");
	}

            check = (testCheck(opponent(currentPlayer))) ? true : false;
            
    
		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn();
		}

                return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		
                ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
    
            if (capturedPiece != null) {
                    piecesOnTheBoard.remove(capturedPiece);
                    capturedPieces.add(capturedPiece);
	}
                
		return capturedPiece;
	}
   	private void undoMove(Position source, Position target, Piece capturedPiece) {
            
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
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
        
        //Metodo que identifica a cor do aversario "opponent"
        private Color opponent(Color color) {
                     return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
             }

        private ChessPiece king(Color color) {
            List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
            for (Piece p : list) {
                     if (p instanceof King) {
                             return (ChessPiece)p;
                     }
             }
            throw new IllegalStateException("Não existe Rei na cor: " + color + " no tabuleiro");
             }
             //Esse "testeCheck" vai testar os movimentos possiveis de todas a peças do adversario da rodada para saber se em algum desses movimentos o Rei pode estar sobe Check
        private boolean testCheck(Color color) {
             Position kingPosition = king(color).getChessPosition().toPosition();
             //Lista das peças da cor adversaria a do Rei
             List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
             for (Piece p : opponentPieces) {
                     boolean[][] mat = p.possibleMoves();
                     if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                             return true;
                     }
             }
            return false;
     }
          
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i=0; i<board.getRows(); i++) {
				for (int j=0; j<board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}	

        private void placeNewPiece(char column, int row, ChessPiece piece){
            board.placePiece(piece,new ChessPosition(column, row).toPosition());
            piecesOnTheBoard.add(piece);
        }

        private void initialSetup(){


        placeNewPiece('a', 1, new Rook  (Color.WHITE,board));
        placeNewPiece('b', 1, new Knight(Color.WHITE,board));
        placeNewPiece('c', 1, new Bishop(Color.WHITE,board));
        placeNewPiece('d', 1, new Queen (Color.WHITE,board));
        placeNewPiece('e', 1, new King  (Color.WHITE,board));
        placeNewPiece('f', 1, new Bishop(Color.WHITE,board));
        placeNewPiece('g', 1, new Knight(Color.WHITE,board));
        placeNewPiece('h', 1, new Rook  (Color.WHITE,board));
        placeNewPiece('a', 2, new Pawn  (Color.WHITE,board));
        placeNewPiece('b', 2, new Pawn  (Color.WHITE,board));
        placeNewPiece('c', 2, new Pawn  (Color.WHITE,board));
        placeNewPiece('d', 2, new Pawn  (Color.WHITE,board));
        placeNewPiece('e', 2, new Pawn  (Color.WHITE,board));
        placeNewPiece('f', 2, new Pawn  (Color.WHITE,board));
        placeNewPiece('g', 2, new Pawn  (Color.WHITE,board));
        placeNewPiece('h', 2, new Pawn  (Color.WHITE,board));


        placeNewPiece('a', 8, new Rook  (Color.BLACK,board));
        placeNewPiece('b', 8, new Knight(Color.BLACK,board));
        placeNewPiece('c', 8, new Bishop(Color.BLACK,board));
        placeNewPiece('d', 8, new Queen (Color.BLACK,board));
        placeNewPiece('e', 8, new King  (Color.BLACK,board));
        placeNewPiece('f', 8, new Bishop(Color.BLACK,board));
        placeNewPiece('g', 8, new Knight(Color.BLACK,board));
        placeNewPiece('h', 8, new Rook  (Color.BLACK,board));
        placeNewPiece('a', 7, new Pawn  (Color.BLACK,board));
        placeNewPiece('b', 7, new Pawn  (Color.BLACK,board));
        placeNewPiece('c', 7, new Pawn  (Color.BLACK,board));
        placeNewPiece('d', 7, new Pawn  (Color.BLACK,board));
        placeNewPiece('e', 7, new Pawn  (Color.BLACK,board));
        placeNewPiece('f', 7, new Pawn  (Color.BLACK,board));
        placeNewPiece('g', 7, new Pawn  (Color.BLACK,board));
        placeNewPiece('h', 7, new Pawn  (Color.BLACK,board));
   }


}
