import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import javax.swing.*;

public class MrPacman {

    private int xx;
    private int yy;

    private Color col;
    private boolean coin;
    private boolean open;
    private boolean base;
    private int dir=4;
    private boolean scared;


    public MrPacman(){
	setX(13);
	setY(7);
	col = new Color(255,0,0);
	coin = false;
	open= false;
	base=true;
	scared=false;
    }

     //************************************************************
    //Set ghost color
    //************************************************************
    public void setColors(int i, MrPacman[] pm){
	switch(i){
	case 0: break;
	    
	case 1: pm[1].setColor(Color.blue);
	    break;
	    
	case 2: pm[2].setColor(Color.pink);
	    break;
	    
	case 3: pm[3].setColor(Color.green);
	    break;
	    
	case 4: pm[4].setColor(Color.orange);
	    break;
	    
	default: pm[i].setColor(Color.red);
	    break;
	}
    }
   
    public  int moveGhost(MrPacman[] pac, String[][] map, int j,int randElem){
	
	int ran=0;
	int corr=0;
	int px=pac[j].getX();
	int py=pac[j].getY();
	int mrpx = pac[0].getX();
	int mrpy = pac[0].getY();
	boolean scar = pac[j].getScared();
	Random R;
	
	if(map[px][py-1].equals("w")){
	    corr++;
	}if(map[px][py+1].equals("w")){
	    corr++;
	}if(map[px-1][py].equals("w")){
	    corr++;
	}if(map[px+1][py].equals("w")){
	    corr++;
	}
	
	//If in base
	if(pac[j].getBase()==true){
		R=new Random();
		int out = R.nextInt(6);
		if(out>=2){
			return ran=0;
		}else{
			return ran=4;
		}
	}
	
	if(corr<2){
	if(!scar){
		R=new Random();
		int r5=R.nextInt(10);
		if(r5>=randElem){
			if(mrpx>px && mrpy<py){ //top Right
				R=new Random();
				r5=R.nextInt(2);
				if(r5==0){
					if(!map[px][py-1].equals("w")){
						return ran=0;
					}else if(!map[px+1][py].equals("w")){
						return ran=1;
					}else{
						R=new Random();
						return ran=R.nextInt(2)+2;
					}
				}else{
					if(!map[px+1][py].equals("w")){
						return ran=1;
					}else if(!map[px][py-1].equals("w")){
						return ran=0;
					}else{
						R=new Random();
						return ran=R.nextInt(2)+2;
					}
				}
			
			}else if(mrpx>px && mrpy>py){//bottom right
				R=new Random();
				r5=R.nextInt(2);
				if(r5==0){
					if(!map[px][py+1].equals("w")){
						return ran=2;
					}else if(!map[px+1][py].equals("w")){
						return ran=1;
					}else{
						R=new Random();
						int rr=R.nextInt(2);
						if(rr==0){
							return ran=3;
						}else{
							return ran=0;
						}
					}
				}else{
					if(!map[px+1][py].equals("w")){
						return ran=1;
					}else if(!map[px][py+1].equals("w")){
						return ran=2;
					}else{
						R=new Random();
						int rr=R.nextInt(2);
						if(rr==0){
							return ran=3;
						}else{
							return ran=0;
						}
					}
				}
			
			}else if(mrpx<px && mrpy>py){//bottom left
				R=new Random();
				r5=R.nextInt(2);
				if(r5==0){
					if(!map[px][py+1].equals("w")){
						return ran=2;
					}else if(!map[px-1][py].equals("w")){
						return ran=3;
					}else{
						R=new Random();
						return ran=R.nextInt(2);
					}
				}else{
					if(!map[px-1][py].equals("w")){
						return ran=3;
					}else if(!map[px][py+1].equals("w")){
						return ran=2;
					}else{
						R=new Random();
						return ran=R.nextInt(2);
					}
				}
				
			}else if(mrpx<px && mrpy<py){//top left
				R=new Random();
				r5=R.nextInt(2);
				if(r5==0){
					if(!map[px][py-1].equals("w")){
						return ran=0;
					}else if(!map[px-1][py].equals("w")){
						return ran=3;
					}else{
						R=new Random();
						return ran=R.nextInt(2)+1;
					}
				}else{
					if(!map[px-1][py].equals("w")){
						return ran=3;
					}else if(!map[px][py-1].equals("w")){
						return ran=0;
					}else{
						R=new Random();
						return ran=R.nextInt(2)+1;
					}
				}
				
			}else if(mrpx==px && mrpy<py){//above
				if(!map[px][py-1].equals("w")){
					return ran=0;
				}else{
					R=new Random();
					return ran=R.nextInt(3)+1;
				}
				
			}else if(mrpx>px && mrpy==py){//right of
				if(!map[px+1][py].equals("w")){
					return ran=1;
				}else {
					R=new Random();
					int r2=R.nextInt(3);
					if(r2==0){
						return ran=0;
					}else if(r2==1){
						return ran=2;
					}else if(r2==2){
						return ran=3;
					}
				}
				
			}else if(mrpx==px && mrpy>py){//below
				if(!map[px][py+1].equals("w")){
					return ran=2;
				}else {
					R=new Random();
					int r3=R.nextInt(3);
					if(r3==0){
						return ran=0;
					}else if(r3==1){
						return ran=1;
					}else if(r3==2){
						return ran=3;
					}
				}
				
			}else if(mrpx<px && mrpy==py){//left of
				if(!map[px-1][py].equals("w")){
					return ran=3;
				}else {
					R=new Random();
					return ran=R.nextInt(3);
				}
			}
		}else{
			R=new Random();
			return ran=R.nextInt(4);
		}
	}else if(scar){//****************************When Scared**********
	R=new Random();
		int r5=R.nextInt(10);
		if(r5>=7){
			if(mrpx<px && mrpy>py){ //bottom left
				R=new Random();
				r5=R.nextInt(2);
				if(r5==0){
					if(!map[px][py-1].equals("w")){
						return ran=0;
					}else if(!map[px+1][py].equals("w")){
						return ran=1;
					}else{
						R=new Random();
						return ran=R.nextInt(2)+2;
					}
				}else{
					if(!map[px+1][py].equals("w")){
						return ran=1;
					}else if(!map[px][py-1].equals("w")){
						return ran=0;
					}else{
						R=new Random();
						return ran=R.nextInt(2)+2;
					}
				}
			
			}else if(mrpx<px && mrpy<py){//top left
				R=new Random();
				r5=R.nextInt(2);
				if(r5==0){
					if(!map[px][py+1].equals("w")){
						return ran=2;
					}else if(!map[px+1][py].equals("w")){
						return ran=1;
					}else{
						R=new Random();
						int rr=R.nextInt(2);
						if(rr==0){
							return ran=3;
						}else{
							return ran=0;
						}
					}
				}else{
					if(!map[px+1][py].equals("w")){
						return ran=1;
					}else if(!map[px][py+1].equals("w")){
						return ran=2;
					}else{
						R=new Random();
						int rr=R.nextInt(2);
						if(rr==0){
							return ran=3;
						}else{
							return ran=0;
						}
					}
				}
			
			}else if(mrpx>px && mrpy<py){//top right
				R=new Random();
				r5=R.nextInt(2);
				if(r5==0){
					if(!map[px][py+1].equals("w")){
						return ran=2;
					}else if(!map[px-1][py].equals("w")){
						return ran=3;
					}else{
						R=new Random();
						return ran=R.nextInt(2);
					}
				}else{
					if(!map[px-1][py].equals("w")){
						return ran=3;
					}else if(!map[px][py+1].equals("w")){
						return ran=2;
					}else{
						R=new Random();
						return ran=R.nextInt(2);
					}
				}
				
			}else if(mrpx>px && mrpy>py){//bottom right
				R=new Random();
				r5=R.nextInt(2);
				if(r5==0){
					if(!map[px][py-1].equals("w")){
						return ran=0;
					}else if(!map[px-1][py].equals("w")){
						return ran=3;
					}else{
						R=new Random();
						return ran=R.nextInt(2)+1;
					}
				}else{
					if(!map[px-1][py].equals("w")){
						return ran=3;
					}else if(!map[px][py-1].equals("w")){
						return ran=0;
					}else{
						R=new Random();
						return ran=R.nextInt(2)+1;
					}
				}
				
			}else if(mrpx==px && mrpy>py){//below
				if(!map[px][py-1].equals("w")){
					return ran=0;
				}else{
					R=new Random();
					return ran=R.nextInt(3)+1;
				}
				
			}else if(mrpx<px && mrpy==py){//left of
				if(!map[px+1][py].equals("w")){
					return ran=1;
				}else {
					R=new Random();
					int r2=R.nextInt(3);
					if(r2==0){
						return ran=0;
					}else if(r2==1){
						return ran=2;
					}else if(r2==2){
						return ran=3;
					}
				}
				
			}else if(mrpx==px && mrpy<py){//above
				if(!map[px][py+1].equals("w")){
					return ran=2;
				}else {
					R=new Random();
					int r3=R.nextInt(3);
					if(r3==0){
						return ran=0;
					}else if(r3==1){
						return ran=1;
					}else if(r3==2){
						return ran=3;
					}
				}
				
			}else if(mrpx>px && mrpy==py){//right of
				if(!map[px-1][py].equals("w")){
					return ran=3;
				}else {
					R=new Random();
					return ran=R.nextInt(3);
				}
			}
		}else{
			R=new Random();
			return ran=R.nextInt(4);
		}
	}
	}
	
	if(corr==2){
	    if(dir==0 && (!map[px][py-1].equals("w"))){
	    return ran=0;
	    }
	    if(dir==1 && (!map[px+1][py].equals("w"))){
	    return ran=1;
	    }
	    if(dir==2 && (!map[px][py+1].equals("w"))){
	    return ran=2;
	    }
	    if(dir==3 && (!map[px-1][py].equals("w"))){
	    return ran=3;
	    }
	
	    if(dir==0 && (!map[px+1][py].equals("w"))){
	    return ran=1;
	    }
	    if(dir==1 && (!map[px][py+1].equals("w"))){
	    return ran=2;
	    }
	    if(dir==2 && (!map[px-1][py].equals("w"))){
	    return ran=3;
	    }
	    if(dir==3 && (!map[px][py-1].equals("w"))){
	    return ran=0;
	    }

	    if(dir==0 && (!map[px-1][py].equals("w"))){
	    return ran=3;
	    }
	    if(dir==1 && (!map[px][py-1].equals("w"))){
	    return ran=0;
	    }
	    if(dir==2 && (!map[px+1][py].equals("w"))){
	    return ran=1;
	    }
	    if(dir==3 && (!map[px][py+1].equals("w"))){
	    return ran=2;
	    }
	}
	if(corr==3){
	    ran=pac[j].getDir()+2;
		if(ran>3){
		    ran=ran-4;
		}
		return ran;
	}
	return ran;
    }

    public void setX(int x){xx=x;}
    public void setY(int y){yy=y;}
    public void setCoin(boolean b){coin=b;}
    public void setColorYellow(){col=new Color(255,255,0);}
    public void setColor(Color c){col=c;}
    public void setDir(int i){dir=i;}
    public void setOpen(boolean b){open=b;}
    public void setBase(boolean b){base=b;}
    public void setScared(boolean b){scared=b;}


    public int getX(){return xx;}
    public int getY(){return yy;}
    public boolean getCoin(){return coin;}
    public Color getColor(){return col;}
    public int getDir(){return dir;}
    public boolean getOpen(){return open;}
    public boolean getBase(){return base;}
    public boolean getScared(){return scared;}

}


    