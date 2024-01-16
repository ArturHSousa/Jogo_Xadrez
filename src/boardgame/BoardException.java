/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boardgame;

/**
 *
 * @author artuz
 */
public class BoardException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    
    public BoardException(String msg){
        super(msg);
    }
}
