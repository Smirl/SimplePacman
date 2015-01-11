import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;

public class Pacman extends JFrame {//implements ActionListener{
    Board BB;


    public Pacman() {
	BB=new Board();
	add(BB);
	
	setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(675, 350);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
	this.addKeyListener(BB);
	
    }

    //public Pacman(int r){

    //}
    
    //public void actionPerformed(ActionEvent e){
    //System.out.println("hey");
    //BB.runb();
    //}


    public static void main(String[] argv) {
	new Pacman();
    }
}
