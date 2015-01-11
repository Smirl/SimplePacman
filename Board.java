import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;


import javax.swing.*;

public class Board extends JPanel implements KeyListener, ActionListener{

    //************************************************************
    //Class variables that are need for many reasons
    //************************************************************
    int speed = 3;
    int nme=4;
    MrPacman[] pm;
    boolean death=false;
    int deathCount =0;
    boolean win=false;
    boolean startScreen=true;
    Color yell = new Color(255,255,0);
    javax.swing.Timer timer,timer1, timer2;
    int nCoins=0;
    int time=0;
    int anim, pacAnim=1;
    int randElem=3;
    int menu=0;
    int mapNo=1;
    int numberOfMaps=3;
    boolean up,left,right,down,paused=false;
    boolean tok=true;
    boolean stillScared=false;
    int scared=1;
		

    //************************************************************
    //The data arrays (Heart variables)
    //************************************************************
    private static String map[][]= new String[27][13];
    private static boolean[][] history= new boolean[27][13];
	
    //************************************************************
    //The Constructor
    //************************************************************
    public Board(){

	readMap();
	makeTimer();
	changeTimer();
	setHistory();
	setNCoins();
	
    }


    //************************************************************
    //Timer Classes
    //************************************************************
    public void makeTimer(){

	timer = new javax.swing.Timer(250,new ActionListener(){
		
		public void actionPerformed(ActionEvent e){
		    //Animate Pacman Mouth
		    if(pacAnim<20){
			if(pacAnim==3 || pacAnim==8 || pacAnim==11 || pacAnim==14 || pacAnim==17){
			    pacAnim += 2;
			}else{
			    pacAnim++;
			}
		    }else{
			pacAnim=1;
			moveThePacman();
		    }
			
		    if(stillScared && scared<500){

			scared++;
		    }else if(stillScared && scared==500){
			unscareG();
			scared=1;
		    }else{
		    }
	
		    //Move MrPacmen Smoothly
		    if(anim<25){
			anim++;
			if(anim==7 || anim==14){
			    tok=!tok;
			}
		    }else{
			anim=1;
			time++;
			runb();
		    }
		    repaint();
		}
		
		
	    });


    }

    public void changeTimer(){
	int sspeed = (int)(40.0/(double)speed);
	timer.setDelay(sspeed);

    }
	
    public void actionPerformed(ActionEvent e){
	runb();
    }

    //************************************************************
    //Read the map from file
    //************************************************************
    public void readMap(){
	
	pm=new MrPacman[nme+1];
	
	for(int j=0;j<pm.length;j++){
	    pm[j]=new MrPacman();
	    pm[j].setColors(j,pm);
	}
	

	pm[0].setX(13);
	pm[0].setY(9);
	pm[0].setColorYellow();
	pm[0].setBase(false);
	
	
	
	Scanner sc=null;
	String mapS = "../res/map" + mapNo + ".txt";
	try{
	    sc = new Scanner(new File(mapS));
	}catch(Throwable e){
	    System.out.println(e);
	}
	
	for(int ii=0;ii<13;ii++){
	    for(int i=0;i<27;i++){
		map[i][ii]=sc.next();
	    }
	}
	
    }
    //************************************************************
    //Set/count History & The coins
    //************************************************************
    public void setHistory(){
	for(int ii=0;ii<13;ii++){
	    for(int i=0;i<27;i++){
		history[i][ii]=false;
	    }
	}
	history[13][9]=true;
    }


    public void setNCoins(){
	for(int ii=0;ii<13;ii++){
	    for(int i=0;i<27;i++){
		if(map[i][ii].equals("c")){
		    nCoins++;
		}
	    }
	}
    }

    public int countHistory(){
	int hist=0;
	for(int ii=0;ii<13;ii++){
	    for(int i=0;i<27;i++){
		if(history[i][ii]==true){
		    hist++;
		}
	    }
	}
	return hist;
    }

    //************************************************************
    //resets
    //************************************************************

    public void resetForDir(){
	deathCount++;
	resetPacman();
	changeTimer();
	timer.restart();
	right=false;
	down=false;
	left=false;
	up=false;
	win=false;
	death=false;
	paused=false;
    }

    //************************************************************
    //Drawing clases
    //************************************************************
	
    //Draw the start screen
    public void drawStartScreen(Graphics2D g2){

	g2.setPaint(Color.black);
	g2.setColor(Color.black);
	g2.fill(new Rectangle2D.Double(0,0,675,350));
	g2.setPaint(Color.gray);
	g2.setColor(Color.gray);
	g2.fill(new Rectangle2D.Double(25,25,625,275));
	g2.setColor(Color.yellow);
	g2.fillArc(100,150,100,100,30,300);
	g2.setColor(Color.red);
	g2.setFont(new Font("Helvetica", Font.BOLD,  44));
	g2.drawString("PACMAN vs EVIL PACMEN?", 30,100);
		
	String speedS = Integer.toString(speed);
	String nmeS = Integer.toString(nme);
	String randElemS = Integer.toString(randElem);
	String mapNoS = Integer.toString(mapNo);
		
	Color[] menuCol = new Color[4];
	for(int c=0;c<4;c++){
	    menuCol[c]=Color.red;
	    if(menu==c){
		menuCol[c]=Color.white;
	    }
	}
	g2.setFont(new Font("Helvetica",Font.BOLD, 20));
		
	g2.setColor(menuCol[0]);
	g2.drawString("Enemy's Speed: ",250,140);
	g2.drawString(speedS,480, 140);
	g2.setColor(menuCol[1]);
	g2.drawString("No of Enemies: ",250,190);
	g2.drawString(nmeS,480, 190);
	g2.setColor(menuCol[2]);
	g2.drawString("Enemy Supidity: ",250,240);
	g2.drawString(randElemS,480, 240);
	g2.setColor(menuCol[3]);
	g2.drawString("Map Number: ",250,290);
	g2.drawString(mapNoS,480, 290);
    }
		
    //Draw the Game Over screen
    public void drawEnd(Graphics2D g2){

	timer.stop();
	
	g2.setPaint(Color.black);
	g2.setColor(Color.black);
	g2.fill(new Rectangle2D.Double(0,0,675,350));
	g2.setPaint(Color.gray);
	g2.setColor(Color.gray);
	g2.fill(new Rectangle2D.Double(25,25,625,275));
	g2.setColor(Color.red);
	g2.setFont(new Font("Helvetica", Font.BOLD,  46));
	g2.drawString("GAME OVER", 195,110);
	int points= (int)((double)(countHistory()+time)*(double)10);
	String s = "Score: " + points;
	g2.setFont(new Font("Helvetica", Font.BOLD, 30));
	g2.drawString(s,250,200);
    }

    //Draw the Congratulations Screen
    public void drawWin(Graphics2D g2){

	timer.stop();

	g2.setPaint(Color.black);
	g2.setColor(Color.black);
	g2.fill(new Rectangle2D.Double(0,0,675,350));
	g2.setPaint(Color.gray);
	g2.setColor(Color.gray);
	g2.fill(new Rectangle2D.Double(25,25,625,275));
	g2.setColor(Color.red);
	g2.setFont(new Font("Helvetica", Font.BOLD,  46));
	g2.drawString("Congratulations", 180,110);
	int points= ((int)((double)(countHistory()+time)*(double)100))+1000;
	String s = "Score: " + points;
	g2.setFont(new Font("Helvetica", Font.BOLD, 30));
	g2.drawString(s,250,200);
    }
		
    //Draw the array!!!
	
    public void animateArray(Graphics2D g2){
	for(int q=0;q<pm.length;q++){
			
	    if(!pm[q].getColor().equals(Color.yellow)){
		if(pm[q].getDir()==0){
		    g2.setColor(pm[q].getColor());
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+3,((pm[q].getY()+1)*25)+3-anim,19,19));
		    g2.fill(new RoundRectangle2D.Float(((pm[q].getX())*25)+3,((pm[q].getY()+1)*25)+13-anim, 19, 9 ,5 , 5));
		    g2.setColor(Color.white);
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+6,((pm[q].getY()+1)*25)+6-anim,5,5));
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+12,((pm[q].getY()+1)*25)+6-anim,5,5));
		    g2.setColor(Color.black);
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+7,((pm[q].getY()+1)*25)+6-anim,3,3));
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+13,((pm[q].getY()+1)*25)+6-anim,3,3));

		}else if(pm[q].getDir()==1){
		    g2.setColor(pm[q].getColor());
		    g2.fill(new Ellipse2D.Double(((pm[q].getX()-1)*25)+3+anim,((pm[q].getY())*25)+3,19,19));
		    g2.fill(new RoundRectangle2D.Float(((pm[q].getX()-1)*25)+3+anim,((pm[q].getY())*25)+13, 19, 9 ,5 , 5));
		    g2.setColor(Color.white);
		    g2.fill(new Ellipse2D.Double(((pm[q].getX()-1)*25)+6+anim,((pm[q].getY())*25)+6,5,5));
		    g2.fill(new Ellipse2D.Double(((pm[q].getX()-1)*25)+12+anim,((pm[q].getY())*25)+6,5,5));
		    g2.setColor(Color.black);
		    g2.fill(new Ellipse2D.Double(((pm[q].getX()-1)*25)+8+anim,((pm[q].getY())*25)+7,3,3));
		    g2.fill(new Ellipse2D.Double(((pm[q].getX()-1)*25)+14+anim,((pm[q].getY())*25)+7,3,3));

		}else if(pm[q].getDir()==2){
		    g2.setColor(pm[q].getColor());
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+3,((pm[q].getY()-1)*25)+3+anim,19,19));
		    g2.fill(new RoundRectangle2D.Float(((pm[q].getX())*25)+3,((pm[q].getY()-1)*25)+13+anim, 19, 9 ,5 , 5));
		    g2.setColor(Color.white);
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+6,((pm[q].getY()-1)*25)+6+anim,5,5));
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+12,((pm[q].getY()-1)*25)+6+anim,5,5));
		    g2.setColor(Color.black);
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+7,((pm[q].getY()-1)*25)+8+anim,3,3));
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+13,((pm[q].getY()-1)*25)+8+anim,3,3));

		}else if(pm[q].getDir()==3){
		    g2.setColor(pm[q].getColor());
		    g2.fill(new Ellipse2D.Double(((pm[q].getX()+1)*25)+3-anim,((pm[q].getY())*25)+3,19,19));
		    g2.fill(new RoundRectangle2D.Float(((pm[q].getX()+1)*25)+3-anim,((pm[q].getY())*25)+13, 19, 9 ,5 , 5));
		    g2.setColor(Color.white);
		    g2.fill(new Ellipse2D.Double(((pm[q].getX()+1)*25)+6-anim,((pm[q].getY())*25)+6,5,5));
		    g2.fill(new Ellipse2D.Double(((pm[q].getX()+1)*25)+12-anim,((pm[q].getY())*25)+6,5,5));
		    g2.setColor(Color.black);
		    g2.fill(new Ellipse2D.Double(((pm[q].getX()+1)*25)+6-anim,((pm[q].getY())*25)+7,3,3));
		    g2.fill(new Ellipse2D.Double(((pm[q].getX()+1)*25)+12-anim,((pm[q].getY())*25)+7,3,3));

		}else{
		    g2.setColor(pm[q].getColor());
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+3,((pm[q].getY())*25)+3,19,19));
		    g2.fill(new RoundRectangle2D.Float(((pm[q].getX())*25)+3,((pm[q].getY())*25)+13, 19, 9 ,5 , 5));
		    g2.setColor(Color.white);
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+6,((pm[q].getY())*25)+6,5,5));
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+12,((pm[q].getY())*25)+6,5,5));
		    g2.setColor(Color.black);
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+7,((pm[q].getY())*25)+7,3,3));
		    g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+12,((pm[q].getY())*25)+7,3,3));

		}
	    }else{
		g2.setColor(Color.yellow);
		if(tok){
		    if(pm[q].getDir()==0){
			g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+3,((pm[q].getY()+1)*25)+3-pacAnim,19,19));
		    }else if(pm[q].getDir()==1){
			g2.fill(new Ellipse2D.Double(((pm[q].getX()-1)*25)+3+pacAnim,((pm[q].getY())*25)+3,19,19));
		    }else if(pm[q].getDir()==2){
			g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+3,((pm[q].getY()-1)*25)+3+pacAnim,19,19));
		    }else if(pm[q].getDir()==3){
			g2.fill(new Ellipse2D.Double(((pm[q].getX()+1)*25)+3-pacAnim,((pm[q].getY())*25)+3,19,19));
		    }else{
			g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+3,((pm[q].getY())*25)+3,19,19));
		    }
		}else{
		    if(pm[q].getDir()==0){
			g2.fillArc(((pm[q].getX())*25)+3,((pm[q].getY()+1)*25)+3-pacAnim,19,19,120,300);										       
		    }else if(pm[q].getDir()==1){
			g2.fillArc(((pm[q].getX()-1)*25)+3+pacAnim,((pm[q].getY())*25)+3,19,19,30,300);
		    }else if(pm[q].getDir()==2){
			g2.fillArc(((pm[q].getX())*25)+3,((pm[q].getY()-1)*25)+3+pacAnim,19,19,300,300);
		    }else if(pm[q].getDir()==3){
			g2.fillArc(((pm[q].getX()+1)*25)+3-pacAnim,((pm[q].getY())*25)+3,19,19,210,300);
		    }else{
			g2.fill(new Ellipse2D.Double(((pm[q].getX())*25)+3,((pm[q].getY())*25)+3,19,19));
		    }
		}
	    }
	}
	
    }
    public void drawArray(Graphics2D g2) {
	for(int i =0;i<27;i++){
	    for(int ii=0;ii<13;ii++) {
		
		if(map[i][ii].equals("w")){
		    
		    g2.setPaint(Color.black);
		    g2.setColor(Color.black);
		    g2.fill(new Rectangle2D.Double((double)i*25,(double)ii*25,25,25));
		    
		}else if(map[i][ii].equals("s")){
		    Color grey = new Color(100,100,100);
		    g2.setColor(grey);
		    g2.setPaint(grey);
		    g2.fill(new Rectangle2D.Double((double)i*25,(double)ii*25,25,25));
		    g2.setColor(Color.white);
		    g2.setPaint(Color.white);
		    g2.fill(new Ellipse2D.Double(((double)i*25)+7,((double)ii*25)+7,11,11));

		}else if(map[i][ii].equals("b")){
		    Color grey = new Color(100,100,100);
		    g2.setColor(grey);
		    g2.setPaint(grey);
		    g2.fill(new Rectangle2D.Double((double)i*25,(double)ii*25,25,25));
		    
		}else if(map[i][ii].equals("c")){
		    Color grey = new Color(100,100,100);
		    g2.setColor(grey);
		    g2.setPaint(grey);
		    g2.fill(new Rectangle2D.Double((double)i*25,(double)ii*25,25,25));
		    g2.setColor(Color.white);
		    g2.setPaint(Color.white);
		    g2.fill(new Ellipse2D.Double(((double)i*25)+10,((double)ii*25)+10,5,5));
		}else if(map[i][ii].equals("g")){
		    Color grey = new Color(100,100,100);
		    g2.setColor(grey);
		    g2.setPaint(grey);
		    g2.fill(new Rectangle2D.Double((double)i*25,(double)ii*25,25,25));
		    //Random R=new Random();
		    //			g2.setPaint(Color.red);
		    //		    g2.fill(new Ellipse2D.Double(((double)i*25)+3,((double)ii*25)+3,19,19));
		}else if(map[i][ii].equals("p")){
		    Color grey = new Color(100,100,100);
		    g2.setColor(grey);
		    g2.setPaint(grey);
		    g2.fill(new Rectangle2D.Double((double)i*25,(double)ii*25,25,25));
		    // g2.setColor(Color.yellow);
		    //		    g2.setPaint(Color.yellow);
		    //		    g2.fill(new Ellipse2D.Double(((double)i*25)+3,((double)ii*25)+3,19,19));
		}else if(map[i][ii].equals("o")){
		    Color grey = new Color(100,100,100);
		    g2.setColor(grey);
		    g2.setPaint(grey);
		    g2.fill(new Rectangle2D.Double((double)i*25,(double)ii*25,25,25));
		    Color blue = new Color(0,0,200);
		    g2.setColor(blue);
		    g2.setPaint(blue);
		    g2.fill(new Rectangle2D.Double((double)i*25,(double)ii*25+10,25,5));	
		}		
	    }
	}
    }

    //************************************************************
    //Move the pacmen up, down, left, right classes
    //************************************************************
    //Move up
    public void pacUp(MrPacman pp){
	int px = pp.getX();
	int py = pp.getY();
	if(!map[px][py-1].equals("w")){

	    if(pp.getCoin()==false){
		map[px][py]="b";
	    }else{
		map[px][py]="c";
	    }
		
	    //getready to replace coin
	    if(map[px][py-1].equals("c")){
		pp.setCoin(true);
	    }else{
		pp.setCoin(false);
	    }
			
	    //getready to replace gate
	    if(px==13 && py-1==5){
		pp.setOpen(true);
	    }else{
		pp.setOpen(false);
	    }
			
	    if(px>=12 && py>=5 && px<=14 && py<=7){
		pp.setBase(true);
	    }else{
		pp.setBase(false);
	    }
	    //if pacman	
	    if(pp.getColor().equals(yell)){      
		if(!stillScared){
		    if(map[px][py-1].equals("g")){     
			if(deathCount<=2){           
			    resetForDir();           
			}else if(deathCount>=3){       
			    death=true;               
			}else{}                   
		    }else{        
			if(map[px][py-1].equals("s")){ 
			    scareG();
			}
			map[px][py-1]="p";
			history[px][py-1]=true;
			
		    }
		}else{
		    if(map[px][py-1].equals("g")){
			getResetG(px,py-1);
		    }else{
			map[px][py-1]="p";
			history[px][py-1]=true;
		    }
		}
	    }else{
		if(!stillScared){
		    if(map[px][py-1].equals("p")){
			if(deathCount<=2){
			    resetForDir();
			}else if(deathCount>=3){
			    death=true;
			}else{}
		    }else{
			map[px][py-1]="g";
		    }
		}else{
		    if(map[px][py-1].equals("p")){
			getResetG(px,py-1);
		    }else{
			map[px][py-1]="g";
		    }
		}
	    }
	    
	    
	    pp.setY(py-1);
	    pp.setDir(0);
			
	    if(!map[13][5].equals("g")){
		map[13][5]="o";
	    }
			
	}else{
	    pp.setDir(4);
	}
    }
    public void pacLeft(MrPacman pp){
	int px = pp.getX();
	int py = pp.getY();
	if(!map[px-1][py].equals("w")){

	    if(pp.getCoin()==false){
		map[px][py]="b";
	    }else{
		map[px][py]="c";
	    }

	    if(map[px-1][py].equals("c")){
		pp.setCoin(true);
	    }else{
		pp.setCoin(false);
	    }

	    if(pp.getColor().equals(yell)){      
		if(!stillScared){
		    if(map[px-1][py].equals("g")){     
			if(deathCount<=2){           
			    resetForDir();           
			}else if(deathCount>=3){       
			    death=true;               
			}else{}                   
		    }else{        
			if(map[px-1][py].equals("s")){ 
			    scareG();
			}
			map[px-1][py]="p";
			history[px-1][py]=true;
			
		    }
		}else{
		    if(map[px-1][py].equals("g")){
			getResetG(px-1,py);
		    }else{
			map[px-1][py]="p";
			history[px-1][py]=true;
		    }
		}
	    }else{
		if(!stillScared){
		    if(map[px-1][py].equals("p")){
			if(deathCount<=2){
			    resetForDir();
			}else if(deathCount>=3){
			    death=true;
			}else{}
		    }else{
			map[px-1][py]="g";
		    }
		}else{
		    if(map[px-1][py].equals("p")){
			getResetG(px-1,py);
		    }else{
			map[px-1][py]="g";
		    }
		}
	    }

	    pp.setX(px-1);
	    pp.setDir(3);
	   
	}else{
	    pp.setDir(4);
	}
    }
    public void pacDown(MrPacman pp){
	int px = pp.getX();
	int py = pp.getY();
	if(map[px][py+1].equals("w")){
	    pp.setDir(4);
	}else if(map[px][py+1].equals("o")){
	    pp.setDir(4);
	}else{

	    if(pp.getCoin()==false){
		map[px][py]="b";
	    }else{
		map[px][py]="c";
	    }

	    if(map[px][py+1].equals("c")){
		pp.setCoin(true);
	    }else{
		pp.setCoin(false);
	    }
	    
	     if(pp.getColor().equals(yell)){      
		if(!stillScared){
		    if(map[px][py+1].equals("g")){     
			if(deathCount<=2){           
			    resetForDir();           
			}else if(deathCount>=3){       
			    death=true;               
			}else{}                   
		    }else{        
			if(map[px][py+1].equals("s")){ 
			    scareG();
			}
			map[px][py+1]="p";
			history[px][py+1]=true;
			
		    }
		}else{
		    if(map[px][py+1].equals("g")){
			getResetG(px,py+1);
		    }else{
			map[px][py+1]="p";
			history[px][py+1]=true;
		    }
		}
	    }else{
		if(!stillScared){
		    if(map[px][py+1].equals("p")){
			if(deathCount<=2){
			    resetForDir();
			}else if(deathCount>=3){
			    death=true;
			}else{}
		    }else{
			map[px][py+1]="g";
		    }
		}else{
		    if(map[px][py+1].equals("p")){
			getResetG(px,py+1);
		    }else{
			map[px][py+1]="g";
		    }
		}
	    }
	       
	    pp.setY(py+1);
	    pp.setDir(2);
	    
	}
    }
    public void pacRight(MrPacman pp){
	int px = pp.getX();
	int py = pp.getY();
	if(!map[px+1][py].equals("w")){

	    if(pp.getCoin()==false){
		map[px][py]="b";
	    }else{
		map[px][py]="c";
	    }

	    if(map[px+1][py].equals("c")){
		pp.setCoin(true);
	    }else{
		pp.setCoin(false);
	    }
	       

	    if(pp.getColor().equals(yell)){      
		if(!stillScared){
		    if(map[px+1][py].equals("g")){     
			if(deathCount<=2){           
			    resetForDir();           
			}else if(deathCount>=3){       
			    death=true;               
			}else{}                   
		    }else{        
			if(map[px+1][py].equals("s")){ 
			    scareG();
			}
			map[px+1][py]="p";
			history[px+1][py]=true;
			
		    }
		}else{
		    if(map[px+1][py].equals("g")){
			getResetG(px+1,py);
		    }else{
			map[px+1][py]="p";
			history[px+1][py]=true;
		    }
		}
	    }else{
		if(!stillScared){
		    if(map[px+1][py].equals("p")){
			if(deathCount<=2){
			    resetForDir();
			}else if(deathCount>=3){
			    death=true;
			}else{}
		    }else{
			map[px+1][py]="g";
		    }
		}else{
		    if(map[px+1][py].equals("p")){
			getResetG(px+1,py);
		    }else{
			map[px+1][py]="g";
		    }
		}
	    }
	    
	    pp.setX(px+1);
	    pp.setDir(1);
	   
	}else{
	    pp.setDir(4);
	}
    }

    //************************************************************
    //Key Event listeners
    //************************************************************
    public void keyPressed(KeyEvent e){
	
	if(!startScreen){
	    if((!win && !death)){
		if(e.getKeyCode()==38){
		    up=true;
		    left=false;
		    right=false;
		    down=false;
		}else if(e.getKeyCode()==37){
		    left=true;
		    right=false;
		    up=false;
		    down=false;
		}else if(e.getKeyCode()==40){
		    down=true;
		    up=false;
		    left=false;
		    right=false;
		}else if(e.getKeyCode()==39){
		    right=true;
		    down=false;
		    left=false;
		    up=false;
		}else if(e.getKeyCode()==32){//spacebar
		    readMap();
		    setHistory();
		    nCoins=0;
		    setNCoins();
		    changeTimer();
		    timer.restart();
		    right=false;
		    down=false;
		    left=false;
		    up=false;
		    win=false;
		    death=false;
		    paused=false;
		    deathCount=0;
		    time=0;
		}else if(e.getKeyCode()==83){//s
		    startScreen=true;
		    win=false;
		    death=false;
		    deathCount=0;
		    paused=false;
		    timer.restart();

		}else if(e.getKeyCode()==80){//p
		    if(paused){
			paused=false;
			timer.restart();
				
		    }else{
			paused=true;
			timer.stop();

		    }
		}
	    }else {
		if(e.getKeyCode()==32){
		    readMap();
		    setHistory();
		    nCoins=0;
		    setNCoins();
		    changeTimer();
		    timer.restart();
		    right=false;
		    down=false;
		    left=false;
		    up=false;
		    death=false;
		    deathCount=0;
		    paused=false;
		    win=false;
		    time=0;
		    startScreen=false;
		}else if(e.getKeyCode()==83){
		    startScreen=true;
		    win = false;
		    death=false;
		    deathCount=0;
		    paused=false;
		    timer.stop();
		}
	    }
	}else if(startScreen){
	    if(e.getKeyCode()==32){
		readMap();
		setHistory();
		nCoins=0;
		setNCoins();
		changeTimer();
		timer.restart();
		right=false;
		down=false;
		left=false;
		up=false;
		death=false;
		deathCount=0;
		paused=false;
		win=false;
		time=0;
		startScreen=false;
	    }else if(e.getKeyCode()==38 && menu>0){
		menu--;
	    }else if(e.getKeyCode()==40 && menu<3){
		menu++;
	    }else if(e.getKeyCode()==39){
		if(menu==0 && speed<10){
		    speed++;
		}else if(menu==1 && nme<10){
		    nme++;
		}else if(menu==2 && randElem<10){
		    randElem++;
		}else if(menu==3 && mapNo<(numberOfMaps-1)){
		    mapNo++;
		}
	    }else if(e.getKeyCode()==37){
		if(menu==0 && speed>1){
		    speed--;
		}else if(menu==1 && nme>1){
		    nme--;
		}else if(menu==2 && randElem>1){
		    randElem--;
		}else if(menu==3 && mapNo>0){
		    mapNo--;
		}
			
	    }
	}
    
	repaint();
    }
    public void keyReleased(KeyEvent e){
	//System.out.println("keyRel" + e.getKeyCode());
    }
    public void keyTyped(KeyEvent e){
	//System.out.println("keyTyped" + e.getKeyCode());
    }
    
	
    //************************************************************
    //RUNB() Bitch
    //************************************************************
    public void runb(){
	for(int q=1;q<pm.length;q++){
	    int ran=pm[q].moveGhost(pm,map,q,randElem);
		   
	    if(ran==0){
		pacUp(pm[q]);
				
	    }else if(ran==1){
		pacRight(pm[q]);
			
	    }else if(ran==2){
		pacDown(pm[q]);
			
	    }else if(ran==3){
		pacLeft(pm[q]);
			
	    }else{
	    }
	}
	if(countHistory()>=nCoins+1){
	    win=true;
	}
    }
    
    //************************************************************
    //Slide pacman
    //************************************************************
    public void moveThePacman(){
	int pDir =pm[0].getDir();
	int px =pm[0].getX();
	int py=pm[0].getY();
	if(up){
	    //pacUp(pm[0]);
	    if(map[px][py-1]!="w"){
		pacUp(pm[0]);
	    }else{
		if(pDir==1){
		    pacRight(pm[0]);
		}else{
		    pacLeft(pm[0]);
		}
	    }
	}else if(right){
	    //pacRight(pm[0]);
	    if(map[px+1][py]!="w"){
		pacRight(pm[0]);
	    }else{
		if(pDir==0){
		    pacUp(pm[0]);
		}else{
		    pacDown(pm[0]);
		}
	    }
		
	}else if(down){
	    //pacDown(pm[0]);
	    if(map[px][py+1]!="w"){
		pacDown(pm[0]);
	    }else{
		if(pDir==1){
		    pacRight(pm[0]);
		}else{
		    pacLeft(pm[0]);
		}
	    }
		
	}else if(left){
	    //pacLeft(pm[0]);
	    if(map[px-1][py]!="w"){
		pacLeft(pm[0]);
	    }else{
		if(pDir==0){
		    pacUp(pm[0]);
		}else{
		    pacDown(pm[0]);
		}
	    }
	}
    }
	
    //************************************************************
    //Reset Pacman and ghosts after death count++
    //************************************************************
	
    public void resetPacman(){
	for(int i=1;i<nme;i++){
	    if(pm[i].getCoin()){
		map[pm[i].getX()][pm[i].getY()]="c";
	    }else if(!pm[i].getCoin()){
		map[pm[i].getX()][pm[i].getY()]="b";
	    }else{}
	}
	map[pm[0].getX()][pm[0].getY()]="b";
	map[13][7]="g";
	map[13][9]="p";
		
	pm=new MrPacman[nme+1];
	
	for(int j=0;j<pm.length;j++){
	    pm[j]=new MrPacman();
	    pm[j].setColors(j,pm);
	}
		

	pm[0].setX(13);
	pm[0].setY(9);
	pm[0].setColorYellow();
	pm[0].setBase(false);
	
    }

    
    //************************************************************
    //Scare the ghosts classes
    //************************************************************
    public void scareG(){
	stillScared=true;
	for(int i=1;i<pm.length;i++){
	    pm[i].setScared(true);
	    Color c = new Color(255,255,255);
	    pm[i].setColor(c);
	}
    }
    public void unscareG(){
	stillScared=false;
	for(int i=1;i<pm.length;i++){
	    pm[i].setScared(false);
	    pm[i].setColors(i,pm);
	}
    }

    public void getResetG(int x, int y){
	for(int i=1;i<pm.length;i++){
	    if(pm[i].getX()==x && pm[i].getY()==y){
		pm[i] = new MrPacman();
		pm[i].setColors(i,pm);
	    }
	}
    }
	    
    
    //************************************************************
    //Paint Class
    //************************************************************
    public void paint(Graphics g){
	
	pm[0].setCoin(false);
	pm[0].setScared(false);
	
	super.paint(g);
	
	Graphics2D g2 = (Graphics2D) g;
	
	RenderingHints rh =new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	
	rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	
	g2.setRenderingHints(rh);
	
	Dimension size = getSize();
	double w = size.getWidth();
	double h = size.getHeight();

	
	for(int i =0;i<27;i++){
	    for(int ii=0;ii<13;ii++) {
		if(!map[i][ii].equals("w") && !map[i][ii].equals("g") && !map[i][ii].equals("o") && !map[i][ii].equals("p") && history[i][ii]==false && !map[i][ii].equals("s")){
		    if(i>=12 && ii>=5 && i<=14 && ii<=7){
			map[i][ii]="b";
		    }else if((i==1 && ii==1) || (i==1 && ii==11)||(i==25 && ii==1)||(i==25 && ii==11)){
			map[i][ii]="s";
		    }else{
			map[i][ii]="c";
		    }
		}
	    }
	}


	drawArray(g2);
	animateArray(g2);
	
	int disPoints = (countHistory()-1)*10;
	String Spoints = Integer.toString(disPoints);
	g2.setColor(Color.red);
	g2.setFont(new Font("Helvetica", Font.BOLD,  25));
	g2.drawString(Spoints, (22*25)+3,22);

	g2.setColor(Color.yellow);
	switch(deathCount){
	case 0:	g2.fillArc(28,(12*25)+3,19,19,30,300);
	    g2.fillArc(53,(12*25)+3,19,19,30,300);
	    g2.fillArc(78,(12*25)+3,19,19,30,300);
	    break;
	case 1:	g2.fillArc(28,(12*25)+3,19,19,30,300);
	    g2.fillArc(53,(12*25)+3,19,19,30,300);
	    break;
	case 2: g2.fillArc(28,(12*25)+3,19,19,30,300);
	    break;
	default: break;
	}
	if(death){
	    drawEnd(g2);
	}
	
	if(win){
	    drawWin(g2);
	}
	
	if(startScreen){
	    drawStartScreen(g2);
	}
	
	if(paused){
	    g2.setColor(Color.red);
	    g2.setFont(new Font("Helvetica", Font.BOLD,  30));
	    g2.drawString("Paused", 285,223);
	}
    }
}


   
