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
public class Rook extends ChessPiece {
    
    public Rook(Color color, Board board) {
        super(color, board);
    }
        
    @Override
    public String toString(){
        return "R";
    }
}
