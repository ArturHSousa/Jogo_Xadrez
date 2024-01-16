/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boardgame;

/**
 *
 * @author artuz
 */
public abstract class Piece {
    
    protected Position position; //somente essa camada pode usar essa classe por ser Protected
    private Board board;

    public Piece(Board board) {
        this.board = board;
        position = null; //Mesmo que vc não declare como null ela ira ficar null pois não foi criada um costrutor para ela
    }

    protected Board getBoard() {
        return board;
    }

    //Apenas sera usado o Get do tabuleiro(Board) para garantir que o tabuleiro não seja alterado
    
    public abstract boolean[][] possibleMoves();
    
    public boolean possibleMove(Position position){
        return possibleMoves()[position.getRow()][position.getColumn()];
        
    }
    //boolean só retorna verdadeiro ou falso 
    public boolean isThereAnyPossibleMove(){
        boolean [][] mat = possibleMoves();
        for (int i=0; i<mat.length; i++) {
            for (int j=0; j<mat.length; j++) {
                if (mat[i][j]){
                    return true;
                }
            }
        }
        return false;
    }
}
