/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Proxy;

import java.io.Serializable;

/**
 *
 * @author JairoDavid
 */
public class Message implements Serializable {
    
    private int Start;
    private int End;
    private String Message;
    private final String Hora;

    public Message(int Start, int End, String Message,String Hora) {
        this.Start = Start;
        this.End = End;
        this.Message = Message;
        this.Hora = Hora;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public int getStart() {
        return Start;
    }

    public void setStart(int Start) {
        this.Start = Start;
    }

    public int getEnd() {
        return End;
    }

    public void setEnd(int End) {
        this.End = End;
    }
    
    @Override
    public String toString() {
        if (this.End != -1) {
            return this.Start + " says to " + this.End +": " + this.Message;
        } else {
            return this.Start + " sent a broadcast: " + this.Message;
        }
    }
}
