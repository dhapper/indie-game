package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import utilz.LoadSave;

public abstract class Button {

	protected int xPos, yPos, rowIndex, index, width, height;
	
	BufferedImage[] imgs;
	protected boolean mouseOver, mousePressed;
	protected Rectangle bounds;
	protected String fileName;
	
	public void update() {
		index = 0;
		if(mouseOver)
			index = 1;
		if(mousePressed)
			index = 2;
	}
	
	public void draw(Graphics g) {
		g.drawImage(imgs[index], xPos, yPos, width, height, null);
		
	}
	
	protected void initBounds(int width, int height) {
		bounds = new Rectangle(xPos, yPos, width, height);
	}
	
	protected void loadImgs(String fileName, int defaultWidth, int defaultHeight) {
		imgs = new BufferedImage[3];
		BufferedImage temp = LoadSave.LoadImage(fileName);
		
		for(int i = 0; i < imgs.length; i++)
			imgs[i] = temp.getSubimage(i * defaultWidth, rowIndex * defaultHeight, defaultWidth, defaultHeight);
	}
	
	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}
	
	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}
	
	
}
