/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess.pieces;

import Chess.ChessPiece;
import Chess.Color;
import boardgame.Board;

/**
 *
 * @author artuz
 */
public class King extends ChessPiece{
    
    public King(Color color, Board board) {
        super(color, board);
    }
    
    @Override
    public String toString() {
        return "K";
    }  

    @Override
    public boolean[][] possibleMoves() {
        boolean [][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        return mat;
    }
}
