import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import javax.imageio.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.ImageIO;


public class RightyBird implements ActionListener, MouseListener, KeyListener
{
	
	public static RightyBird rightyBird;

	public final int WIDTH = 1920	, HEIGHT = 1080;

	public Renderer renderer;

	public Rectangle bird;

	public ArrayList<Rectangle> columns;

	public int ticks, yMotion, y2Motion, speed, x2Motion, score, flag=1, highScore=0;

	public boolean gameOver, started;

	public Random rand;
	
	public double increment = 1;
	

	public RightyBird()
	{
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);

		renderer = new Renderer();
		rand = new Random();

		jframe.add(renderer);
		jframe.setTitle("Rigthy Bird");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);

		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
		columns = new ArrayList<Rectangle>();

		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);

		timer.start();
	}

	public void addColumn(boolean start)
	{
		int space = 200;
		int width = 50;
		int height = 50 + rand.nextInt(HEIGHT/ 2);

		if (start)
		{
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
	
		}
		else
		{
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}

	public void paintColumn(Graphics g, Rectangle column)
	{
		g.setColor(Color.yellow.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}

	public void jump()
	{
		if (gameOver)
		{
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			y2Motion = 0;
			increment = 1;
			
			speed = 0;
			score = 0;
			flag = 0;

			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);

			gameOver = false;
		}

		if (!started)
		{
			started = true;
		}
		else if (!gameOver)
		{
			if (yMotion > 0)
			{
				yMotion = 0;
			}
			if (speed > 0)
			{
				//speed = 0;
			}
			
			if (flag == 0)
			{	
				yMotion -= 5;
				speed =5;
				flag = 1;
			}
			else if (flag == 1)
			{
				speed = 0;
				yMotion= -7;
				flag = 2;
			}
			else if (flag == 2)
			{
				yMotion = 0;
				speed = -5;
				flag = 3;
			}
			else if (flag == 3)
			{
				speed = 0;
				yMotion = -7;
				flag = 0;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		int speed = 5;

		ticks++;
		
		if ((score % 5) == 0 && score != 0)
		{
			increment += 0.1;
		}

		if (started)
		{
			for (int i = 0; i < columns.size(); i++)
			{
				Rectangle column = columns.get(i);
				
				column.x -= speed * increment;
				
			}

			if (ticks % 2 == 0 && yMotion < 15)
			{
				yMotion += 0;
			}

			for (int i = 0; i < columns.size(); i++)
			{
				Rectangle column = columns.get(i);

				if (column.x + column.width < 0)
				{
					columns.remove(column);
					score++;

					if (column.y == 0)
					{
						addColumn(false);
					}
				}
			}
			if (flag == 0)
			{
				bird.y += yMotion;
				bird.x -= 0.3 * speed;
			}
			else if (flag == 1)
			{
				bird.x += 0.7 *speed;
			}
			else if (flag == 2)
			{
				bird.y -= yMotion;
				bird.x -= 0.3*speed;
			}
			else if (flag == 3)
			{
				bird.x -= 2 * speed;
			}

			for (Rectangle column : columns)
			{
				if (score%2 == 0 &&column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10)
				{
					//score++;
				}
				else{
					if (score%2 == 1 && column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10)
					{
				
				//	score++;
					}
				}

				if (column.intersects(bird))
				{
					gameOver = true;

					if (bird.x <= column.x)
					{
						bird.x = column.x - bird.width;

					}
					else
					{
						if (column.y != 0)
						{
							bird.y = column.y - bird.height;
						}
						else if (bird.y < column.height)
						{
							bird.y = column.height;
						}
					}
				}
			}

			if (bird.y > HEIGHT - 120 || bird.y < 0)
			{
				gameOver = true;
			}

			if (bird.y + yMotion >= HEIGHT - 120)
			{
				bird.y = HEIGHT - 120 - bird.height;
				gameOver = true;
			}
		}
		if(score > highScore && gameOver==false) {
			highScore=score;
		}
		if (bird.x <=0 || bird.x >= WIDTH) {
			gameOver = true;
		}
		renderer.repaint();
	}

	public void repaint(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);

		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);

		g.setColor(Color.yellow);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);

		for (Rectangle column : columns)
		{
			paintColumn(g, column);
		}

		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 50));

		if (!started)
		{
			g.drawString("Click to start!", 75, HEIGHT / 2 - 50);
		}

		if (gameOver)
		{
			g.drawString("Game Over!", 280, HEIGHT / 2 - 100);
			g.drawString("Highscore : "+ highScore/2,250, HEIGHT / 2 - 0);
		}

		if (!gameOver && started)
		{
			g.drawString("Highscore : "+ highScore, 10, HEIGHT / 10 - 120);
			g.drawString(String.valueOf(score/2), WIDTH / 2 - 25, 100);
		}
	}

	public static void main(String[] args)
	{
		rightyBird = new RightyBird();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		jump();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			jump();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{

	}

}
