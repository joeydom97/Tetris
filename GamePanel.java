import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.awt.Color;
import java.util.ArrayList;
/**
 * Write a description of class GamePanel here.
 *
 * @author Greg Johnson, University of Connecticut
 * @version 0.3
 */
public class GamePanel extends JPanel implements ActionListener
{
    //
    // instance variables - replace the example below with your own
    private PieceProxy _piece;
    private Timer _timer;
    private Random _generator;
    private PieceProxy _test;
    private KeyUpListener _upKey;
    private KeyDownListener _downKey;
    private KeyLeftListener _leftKey;
    private KeyRightListener _rightKey;
    private KeyPListener _pauseKey;
    private KeySpaceListener _spaceKey;
    private boolean spacePress = false;

    SmartRectangle[][] _board;
    private boolean nextB;
    /**
     * Constructor for objects of class GamePanel
     */
    public GamePanel()
    {
        // initialise instance variables
        this.setBackground(Color.BLACK);
        this.setSize(new Dimension(TetrisConstants.BLOCK_SIZE*TetrisConstants.BOARD_WIDTH+8, TetrisConstants.BLOCK_SIZE*TetrisConstants.BOARD_HEIGHT+36));
        this.setPreferredSize(new Dimension(TetrisConstants.BLOCK_SIZE*TetrisConstants.BOARD_WIDTH+8, TetrisConstants.BLOCK_SIZE*TetrisConstants.BOARD_HEIGHT+36));

        _upKey = new KeyUpListener(this);
        _downKey = new KeyDownListener(this);
        _leftKey = new KeyLeftListener(this);
        _rightKey = new KeyRightListener(this);
        _pauseKey = new KeyPListener(this);
        _spaceKey = new KeySpaceListener(this, this);
        nextB = false;
        _generator = new Random();
        _board = new SmartRectangle[TetrisConstants.BOARD_HEIGHT][TetrisConstants.BOARD_WIDTH];
        
        _piece = new PieceProxy();
        _piece.setPiece(tetriminoFactory());
         
        _timer = new Timer(500, this);
        _timer.start();
                
        
            
        
       
    }
    
    public Tetrimino tetriminoFactory()
    /** 
     * This method implements the factory method design pattern to build new tetriminos during Tetris game play.
     */
    {
        Tetrimino newPiece;
        int randomNumber;
        
        int x = (TetrisConstants.BOARD_WIDTH/2) * TetrisConstants.BLOCK_SIZE;
        int y = 0;
        randomNumber = (int) (Math.floor(Math.random()*7)+1);
       switch(randomNumber) {
            case 1: newPiece = new Z(x,y, this);     break;
            case 2: newPiece = new S(x,y, this);     break;
            case 3: newPiece = new L(x,y, this);     break;
            case 4: newPiece = new J(x,y, this);     break;
            case 5: newPiece = new O(x,y, this);     break;
            case 6: newPiece = new I(x,y, this);     break;
            default: newPiece = new T(x,y, this);     break;
        }
       //_timer.setDelay(500);
        return newPiece;
    }
    
    public void paintComponent (java.awt.Graphics aBrush) 
    {
        super.paintComponent(aBrush);
        java.awt.Graphics2D betterBrush = (java.awt.Graphics2D)aBrush;
        
        
        _piece.fill(betterBrush);
        _piece.draw(betterBrush);
        
        for(int r = 0; r < _board.length; r++){
            for(int c = 0; c < _board[r].length; c++){
                if(_board[r][c] != null){
                _board[r][c].fill(betterBrush);
                _board[r][c].draw(betterBrush);
            }
            }
        }
    }
    /**
     * This method takes two integers representing the column and row of a cell on the game board a component rectangle into which a
     * tetrimino wishes to move. This can be prevented by either the cell being off of the game board (not a valid cell) or by the
     * cell being occupied by another SmartRectangle.
     * 
     * @param c The column of the cell in question on the game board.
     * @param r The row of the cell in question on the game board.
     * @return boolean This function returns whether the component rectangle can be moved into this cell.
     */
    public boolean canMove(int r, int c)
    {
         
        
        if(isValid(r,c)){
            if(r == 0){
             r = 0;   
            }
            else{
                r = r/TetrisConstants.BLOCK_SIZE;
            }
           if(isFree(r,c = c/TetrisConstants.BLOCK_SIZE)){
            return true;
        }
        else{
            nextB = true;
            return false;
        }
    }
    else{
        return false;
    }
    }
    
    /**
     * This method takes two integers representing the column and row of a cell on the game board a component rectangle into which a
     * tetrimino wishes to move. This method returns a boolean indicating whether the cell on the game board is empty.
     * 
     * @param c The column of the cell in question on the game board.
     * @param r The row of the cell in question on the game board.
     * @return boolean This function returns whether the cell on the game board is free.
     */    
    private boolean isFree(int r, int c)
    {
       
        if(_board[c][r] == null){
            
            return true;
            
        }
        else{
            
            return false;
            
        }
    }
    /**
     * This method takes two integers representing the column and row of a cell on the game board a component rectangle into which a
     * tetrimino wishes to move. This function checks to see if the cell at (c, r) is a valid location on the game board.
     * 
     * @param c The column of the cell in question on the game board.
     * @param r The row of the cell in question on the game board.
     * @return boolean This function returns whether the location (c, r) is within the bounds of the game board.
     */
    private boolean isValid(int i, int j)
    { 
        if((i < (TetrisConstants.BLOCK_SIZE*(TetrisConstants.BOARD_WIDTH)))
         & (j < TetrisConstants.BLOCK_SIZE*(TetrisConstants.BOARD_HEIGHT))
         & i >= 0){
             
            return true;
        }
        else{
            return false;
        }
    }
     /**
     * This method takes two integers representing the column and row of a cell on the game board a component rectangle into which a
     * tetrimino wishes to move. This can be prevented by either the cell being off of the game board (not a valid cell) or by the
     * cell being occupied by another SmartRectangle.
     * 
     * @param r The SmartRectangle to add to the game board.
     * @return Nothing
     */   
    public void addToBoard(int row, int col, SmartRectangle r)
    {
        
      row = row/TetrisConstants.BLOCK_SIZE;
      col = col/TetrisConstants.BLOCK_SIZE;
       _board[row][col]= r;
    }
    /**
     * This method takes one integer representing the row of cells on the game board to move down on the screen after a full 
     * row of squares has been removed.
     * 
     * @param row The row in question on the game board.
     * @return Nothing
     */
    private void moveBlocksDown(int row)
    {
        
           for(int col = 0; col < 10; col++){
           
              _board[row][col] = null;
            }
           for(int r = row; r > 1; r--){
            for(int col = 0; col < 10; col++){
                if(_board[r-1][col] != null){
              _board[r - 1][col].setLocation((int)_board[r - 1][col].getX(),
                ((int)_board[r-1][col].getY() + 25));
              _board[r][col] = _board[r-1][col];
              _board[r-1][col] = null;
              
            }
            }
            }
            
    }
    /**
     * This method checks each row of the game board to see if it is full of rectangles and should be removed. It calls
     * moveBlocksDown to adjust the game board after the removal of a row.
     * 
     * @return Nothing
     */
    private void checkRows(){
        int amount = 0;
        for(int c = 0; c < _board.length; c++){
            amount = 0;
            for(int r = 0; r < _board[c].length; r++){
                if(_board[c][r] != null){
                   amount++;
                }
                else{
                    amount = 0;
                }
                if(amount == 10){
                    moveBlocksDown(c);
                    amount = 0;
                }
               
            }
        }
    }
    /**
     * This method checks to see if the game has ended.
     * 
     * @return boolean This function returns whether the game is over or not.
     */
    private boolean checkEndGame()
    {
        boolean endGame = false;
        for(int c = 0; c < _board.length; c++){
         for(int r = 0; r < _board[c].length; r++){
                if(_board[c][r] != null){
                    if(_board[c][r].getY() == 0){
                        
                        endGame = true;
                        _timer.stop();
                    }
    
                }
            }
        }
        return endGame;
    }
    public void actionPerformed(ActionEvent e)
    {
        if(!checkEndGame()){
        _piece.moveDown();
        if(nextB){
            spacePress = false;
            _piece.setPiece(tetriminoFactory());
            _timer.setDelay(500);
            nextB = false;
        }
        checkRows();
        
    }
        repaint();
    }
    private class KeyUpListener extends KeyInteractor 
    {
        public KeyUpListener(JPanel p)
        {
            super(p,KeyEvent.VK_UP);
        }
        
        public  void actionPerformed (ActionEvent e) {
            if (!_timer.isRunning()) {
                //timer is paused, do nothing
                return;
            }
            if(spacePress == false){
            _piece.turnRight();
        }
            repaint();
        }
    }
    private class KeyDownListener extends KeyInteractor 
    {
        public KeyDownListener(JPanel p)
        {
            super(p,KeyEvent.VK_DOWN);
        }
        
        public  void actionPerformed (ActionEvent e) {
            spacePress = true;
            if (!_timer.isRunning()) {
                //timer is paused, do nothing
                return;
            }

                _timer.setDelay(100);

            repaint();
        }
    } 
    private class KeyLeftListener extends KeyInteractor 
    {
        public KeyLeftListener(JPanel p)
        {
            super(p,KeyEvent.VK_LEFT);
        }
        
        public  void actionPerformed (ActionEvent e) {
            if (!_timer.isRunning()) {
                //timer is paused, do nothing
                return;
            }
            if(spacePress == false){
            _piece.moveLeft();
        }
            repaint();
        }
    } 
    private class KeyRightListener extends KeyInteractor 
    {
        public KeyRightListener(JPanel p)
        {
            super(p,KeyEvent.VK_RIGHT);
        }
        
        public  void actionPerformed (ActionEvent e) {
            if (!_timer.isRunning()) {
                //timer is paused, do nothing
                return;
            }
            
            if(spacePress == false){
            _piece.moveRight();
        }
            repaint();
        }
    }
    private class KeyPListener extends KeyInteractor 
    {
        public KeyPListener(JPanel p)
        {
            super(p,KeyEvent.VK_P);
        }
        
        public  void actionPerformed (ActionEvent e) {
            if(_timer.isRunning())
                _timer.stop();
            
            else
                _timer.start();
        }
    }
    private class KeySpaceListener extends KeyInteractor{
        private GamePanel _panel;
        public KeySpaceListener(JPanel p, GamePanel panel){
            super(p, KeyEvent.VK_SPACE);
            _panel = panel;
        }
        public void actionPerformed(ActionEvent e){
            spacePress = true;
            if (!_timer.isRunning()) {
                //timer is paused, do nothing
                return;
            }
                _timer.setDelay(100);
        repaint();  
        }
    }
}
