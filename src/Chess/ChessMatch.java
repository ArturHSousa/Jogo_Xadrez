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
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;
	
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

	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
        
	public ChessPiece getPromoted() {
		return promoted;
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

                ChessPiece movedPiece = (ChessPiece)board.piece(target);
            
            // #specialmove promotion PROMOÇÃO
            promoted = null;
            if (movedPiece instanceof Pawn) {
            if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
                    promoted = (ChessPiece)board.piece(target);
                    promoted = replacePromotedPiece("RAINHA");
			}
		}

                
                check = (testCheck(opponent(currentPlayer))) ? true : false;
            
    
            if (testCheckMate(opponent(currentPlayer))) {
                    checkMate = true;
            }
            else {
                    nextTurn();
            }
                
		// #specialmove en passant
            if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
                    enPassantVulnerable = movedPiece;
            }
            else {
                    enPassantVulnerable = null;
            }

            return (ChessPiece)capturedPiece;
	}
        
	public ChessPiece replacePromotedPiece(String type) {
            if (promoted == null) {
                throw new IllegalStateException("Não há nenhuma peça a ser promovida");
            }
            if (!type.equals("BISPO") && !type.equals("CAVALO") && !type.equals("TORRE") && !type.equals("RAINHA")) {
                return promoted;
            }

                    Position pos = promoted.getChessPosition().toPosition();
                    Piece p = board.removePiece(pos);
                    piecesOnTheBoard.remove(p);

                    ChessPiece newPiece = newPiece(type, promoted.getColor());
                    board.placePiece(newPiece, pos);
                    piecesOnTheBoard.add(newPiece);

                return newPiece;
	}

	private ChessPiece newPiece(String type, Color color) {
            if (type.equals("BISPO"))  return new Bishop(color, board, this);
            if (type.equals("CAVALO")) return new Knight(color, board, this);
            if (type.equals("RAINHA")) return new Queen (color, board, this);
            if (type.equals("TORRE"))  return new Rook (color, board, this);
            return new Rook(color, board, this);
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
            // #specialmove castling kingside "ROQUE"
            if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
                    Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
                    Position targetT = new Position(source.getRow(), source.getColumn() + 1);
                    ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
                    board.placePiece(rook, targetT);
                    rook.increaseMoveCount();
		}

            // #specialmove castling queenside "ROQUE"
            if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
                    Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
                    Position targetT = new Position(source.getRow(), source.getColumn() - 1);
                    ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
                    board.placePiece(rook, targetT);
                    rook.increaseMoveCount();
		}
                
            // #specialmove en passant
            if (p instanceof Pawn) {
		if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                            Position pawnPosition;
			if (p.getColor() == Color.WHITE) {
                                pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                                }
                            else {
                                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
                        }
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
                
		// #specialmove castling kingside "ROQUE" foi desfeito no metodo atual "undoMove" (para não duplicar a peça)
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}

		// #specialmove castling queenside "ROQUE" foi desfeito no metodo atual "undoMove" (para não duplicar a peça)
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		// #specialmove en passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)board.removePiece(target);
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(3, target.getColumn());
				}
				else {
					pawnPosition = new Position(4, target.getColumn());
				}
				board.placePiece(pawn, pawnPosition);
			}
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

        placeNewPiece('A', 1, new Rook  (Color.WHITE,board, this));
        placeNewPiece('B', 1, new Knight(Color.WHITE,board, this));
        placeNewPiece('C', 1, new Bishop(Color.WHITE,board, this));
        placeNewPiece('D', 1, new Queen (Color.WHITE,board, this));
        placeNewPiece('E', 1, new King  (Color.WHITE,board, this));
        placeNewPiece('F', 1, new Bishop(Color.WHITE,board, this));
        placeNewPiece('G', 1, new Knight(Color.WHITE,board, this));
        placeNewPiece('H', 1, new Rook  (Color.WHITE,board, this));
        placeNewPiece('A', 2, new Pawn  (Color.WHITE,board, this));
        placeNewPiece('B', 2, new Pawn  (Color.WHITE,board, this));
        placeNewPiece('C', 2, new Pawn  (Color.WHITE,board, this));
        placeNewPiece('D', 2, new Pawn  (Color.WHITE,board, this));
        placeNewPiece('E', 2, new Pawn  (Color.WHITE,board, this));
        placeNewPiece('F', 2, new Pawn  (Color.WHITE,board, this));
        placeNewPiece('G', 2, new Pawn  (Color.WHITE,board, this));
        placeNewPiece('H', 2, new Pawn  (Color.WHITE,board, this));


        placeNewPiece('A', 8, new Rook  (Color.BLACK,board, this));
        placeNewPiece('B', 8, new Knight(Color.BLACK,board, this));
        placeNewPiece('C', 8, new Bishop(Color.BLACK,board, this));
        placeNewPiece('D', 8, new Queen (Color.BLACK,board, this));
        placeNewPiece('E', 8, new King  (Color.BLACK,board, this));
        placeNewPiece('F', 8, new Bishop(Color.BLACK,board, this));
        placeNewPiece('G', 8, new Knight(Color.BLACK,board, this));
        placeNewPiece('H', 8, new Rook  (Color.BLACK,board, this));
        placeNewPiece('A', 7, new Pawn  (Color.BLACK,board, this));
        placeNewPiece('B', 7, new Pawn  (Color.BLACK,board, this));
        placeNewPiece('C', 7, new Pawn  (Color.BLACK,board, this));
        placeNewPiece('D', 7, new Pawn  (Color.BLACK,board, this));
        placeNewPiece('E', 7, new Pawn  (Color.BLACK,board, this));
        placeNewPiece('F', 7, new Pawn  (Color.BLACK,board, this));
        placeNewPiece('G', 7, new Pawn  (Color.BLACK,board, this));
        placeNewPiece('H', 7, new Pawn  (Color.BLACK,board, this));

   }


}
