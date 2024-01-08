/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess;

import boardgame.Position;

/**
 *
 * @author artuz
 */
public class ChessPosition {
    
    private char column;
    private int row;

    public ChessPosition(char column, int row) {
        if (column < 'a' || column > 'h' || row < 1 || row > 8) {
           throw new ChessException("Erro iniciando a posição, valores validos são a1 até h8");
        }
        this.column = column;
        this.row = row;
    }

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    protected Position toPosition(){
        return new Position(8 - row, column - 'a');
    }
    
    protected static ChessPosition fromPosition(Position position){
        return new ChessPosition((char)('a' - position.getColumn()), 8 - position.getRow());
    }
    @Override
    public String toString(){
        return "" + column + row;
    } //O String vazio ( "" ) é pra contatenar automatico, se você tirar o compilador não vai aceitar, colocamos ele para forçar o compilador a entende que isso aqui é uma concatenação de strings
} //concatenar é a operação de unir o conteúdo de duas strings
