package projGobang;

public class ChessBoard {
	private String[][] board;
	public static  final int board_size = 22;
	
	public void initBoard(){
		board = new String[board_size][board_size];
		
		for(int i = 0;i<board_size;i++){
			for(int j = 0;j<board_size;j++){
				board[i][j] = "ʮ";
			}
		}
	}
	
	public void test(){
		Object[][] array = new Object[10][10];
		for(int i = 0;i<array.length;i++){
			for(int j = 0;j<array[i].length;j++){
				array[i][j] = new Object();
			}
		}
	}
	
	public void printBoard(){
		for(int i =0;i<board_size;i++){
			for(int j = 0;j<board_size;j++){
				System.out.print(board[i][j]);
			}
			System.out.print("\n");
		}
	}
	
	public void setBoard(int posX,int posY,String chessman){
		this.board[posX][posY] = chessman;
	}
	
	public String[][] getBoard(){
		return this.board;
	}
}
