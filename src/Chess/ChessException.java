/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess;

/**
 *
 * @author artuz
 */
public class ChessException extends RuntimeException{
    //Não a necessidade de declarar a serial versão mais
    public ChessException(String msg){
        super(msg);
    }
}
