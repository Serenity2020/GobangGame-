package projGobang;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GobangGame {
	private final int win_count = 5;
	private int posX = 0;
	private int posY = 0;
	
	private ChessBoard chessboard;//定义棋盘
	
	public GobangGame(){
		
	}
	
	public GobangGame(ChessBoard chessboard){
		this.chessboard = chessboard;
	}
	
	//此方法用来验证控制台的输入字符串是否合法，如果合法，返回true。如果不合法就返回false。抛出Exception异常
	public boolean isValid(String InputStr){
		String[] PosStrArray = InputStr.split(",");
		try{
			posX = Integer.parseInt(PosStrArray[0])-1;
			posY = Integer.parseInt(PosStrArray[1])-1;	
		}catch(NumberFormatException e){
			chessboard.printBoard();
			System.out.println("请以(数字，数字)的格式输入,输入时不需要打括号");
			return false;
		}
		
		if(posX < 0 || posX >= ChessBoard.board_size || posY < 0 || posY >= ChessBoard.board_size){
			chessboard.printBoard();
			System.out.println("X和Y坐标必须处于0和"+ChessBoard.board_size+"之间，"+"请重新输入");
			return false;
		}
		
		String[][] board = chessboard.getBoard();
		if(board[posX][posY] != "十"){
			chessboard.printBoard();
			System.out.println("此位置已经有棋子，请重新输入： ");
			return false;
		}
		return true;
	}
	
	//开始下棋 ，以下是下棋实现的方法
	public void start() throws Exception{
		boolean IsOver = false;//如果是true游戏就结束
		chessboard.initBoard();//创建棋盘
		chessboard.printBoard();
		
		//获取键盘的输入
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String InputStr = null;
		
		while((InputStr = br.readLine()) != null){
			IsOver = false;
			if(!isValid(InputStr)){
				continue;
			}
			
			//把对应的数组元素变为黑色棋子
			String chessman = Chessman.BLACK.getChessman();
			chessboard.setBoard(posX, posY, chessman);
			
			if(isWin(posX,posY,chessman)){
				IsOver = true;//如果连子则游戏结束
			}else{
				//计算机随机选择位置坐标
				int[] ComputerPosArray = computerDo();
				chessman = Chessman.WHITE.getChessman();
				chessboard.setBoard(ComputerPosArray[0], ComputerPosArray[1], chessman);
				
				//判断计算机是否赢了
				if(isWin(ComputerPosArray[0],ComputerPosArray[1],chessman)){
					IsOver = true;
				}
			}
			
			if(IsOver){
				//如果已经一方胜利，则询问用户是否继续游戏。如果继续，则初始化棋盘，继续游戏
				if(isReplay(chessman)){
					chessboard.initBoard();
					chessboard.printBoard();
					continue;
				}
				//如果不继续，则退出程序
				break;
			}
			
			chessboard.printBoard();
			System.out.println("请输入下棋坐标，应以x,y的格式输入： ");
		}
	}
	
	public boolean isReply(String chessman) throws Exception{
		chessboard.printBoard();
		String message = chessman.equals(Chessman.BLACK.getChessman())? "恭喜你，你赢了":"很遗憾，未胜利！";
		System.out.println(message + "再来一局？（y/n）");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		if(br.readLine().equals("y")){
			//开始新一局
			return true;
		}
		return false;
	}
	
	/*计算机随机下棋*/
	public int[] computerDo(){
		int posX = (int) (Math.random() * (ChessBoard.board_size-1));
		int posY = (int) (Math.random() * (ChessBoard.board_size-1));
		
		String[][] board = chessboard.getBoard();
		while(board[posX][posY] != "十"){
			//当棋盘该位置已经被棋子占据的时候，则重新生成随机坐标
			posX = (int) (Math.random()*ChessBoard.board_size-1);
			posY = (int) (Math.random()*ChessBoard.board_size-1);
		}
		int[] result = {posX,posY};
		return result;
	}
	
	public boolean isWin(int posX,int posY,String ico){
		//直线起点的坐标X
		int startX = 0;
		//直线结束的坐标X
		int startY = 0;
		//直线结束X坐标
		int endX = ChessBoard.board_size - 1;
		//直线结束Y坐标
		int endY = ChessBoard.board_size - 1;
		//同条直线上相邻棋子累积数
		int sameCount = 0;
		int temp = 0;
		//计算起点的最小X坐标和Y坐标
		temp = posX - win_count + 1;
		startX = temp < 0?0:temp;
		temp = posY - win_count + 1;
		startY = temp<0?0:temp;
		//计算终点的最大X坐标与Y坐标
		temp = posX + win_count - 1;
		endX = temp>ChessBoard.board_size-1?ChessBoard.board_size-1:temp;
		temp = posY + win_count - 1;
		endY = temp>ChessBoard.board_size-1?ChessBoard.board_size-1:temp;
		
		//从左到右方向计算相同相邻棋子的数目
		String[][] board = chessboard.getBoard();
		for(int j = startY;j<endY;j++){
			if(board[posX][j] == ico && board[posX][j] == ico){
				sameCount++;
			}else if(sameCount != win_count - 1){
				sameCount = 0;
			}
		}
		
		if(sameCount == 0){
			//从上到下计算相邻棋子的数目
			for(int i = startX;i<endX;i++){
				if(board[i][posY] == ico && board[i+1][posY] == ico){
					sameCount++;
				}else if(sameCount != win_count - 1){
					sameCount = 0;
				}
			}
		}
		
		if(sameCount == 0){
			//从左上到右下计算相同相邻棋子的数目
			int j = startY;
			for(int i = startX;i<endX;i++){
				if(j<endY){
					if(board[i][j] == ico && board[i+1][j+1] == ico){
						sameCount++;
					}else if(sameCount != win_count - 1){
						sameCount = 0;
					}
					j++;
				}
			}
		}
		
		return sameCount == win_count - 1?true:false;
	}
	
	public boolean isReplay(String chessman) throws Exception{
		chessboard.printBoard();
		String message = chessman.equals(Chessman.BLACK.getChessman())?"恭喜您，您赢了":"很遗憾，您输了";
		System.out.println(message + "再下一局？(y/n)");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		if(br.readLine().equals("y")){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception{
		GobangGame gb = new GobangGame(new ChessBoard());
		gb.start();
	}
}
