package projGobang;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GobangGame {
	private final int win_count = 5;
	private int posX = 0;
	private int posY = 0;
	
	private ChessBoard chessboard;//��������
	
	public GobangGame(){
		
	}
	
	public GobangGame(ChessBoard chessboard){
		this.chessboard = chessboard;
	}
	
	//�˷���������֤����̨�������ַ����Ƿ�Ϸ�������Ϸ�������true��������Ϸ��ͷ���false���׳�Exception�쳣
	public boolean isValid(String InputStr){
		String[] PosStrArray = InputStr.split(",");
		try{
			posX = Integer.parseInt(PosStrArray[0])-1;
			posY = Integer.parseInt(PosStrArray[1])-1;	
		}catch(NumberFormatException e){
			chessboard.printBoard();
			System.out.println("����(���֣�����)�ĸ�ʽ����,����ʱ����Ҫ������");
			return false;
		}
		
		if(posX < 0 || posX >= ChessBoard.board_size || posY < 0 || posY >= ChessBoard.board_size){
			chessboard.printBoard();
			System.out.println("X��Y������봦��0��"+ChessBoard.board_size+"֮�䣬"+"����������");
			return false;
		}
		
		String[][] board = chessboard.getBoard();
		if(board[posX][posY] != "ʮ"){
			chessboard.printBoard();
			System.out.println("��λ���Ѿ������ӣ����������룺 ");
			return false;
		}
		return true;
	}
	
	//��ʼ���� ������������ʵ�ֵķ���
	public void start() throws Exception{
		boolean IsOver = false;//�����true��Ϸ�ͽ���
		chessboard.initBoard();//��������
		chessboard.printBoard();
		
		//��ȡ���̵�����
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String InputStr = null;
		
		while((InputStr = br.readLine()) != null){
			IsOver = false;
			if(!isValid(InputStr)){
				continue;
			}
			
			//�Ѷ�Ӧ������Ԫ�ر�Ϊ��ɫ����
			String chessman = Chessman.BLACK.getChessman();
			chessboard.setBoard(posX, posY, chessman);
			
			if(isWin(posX,posY,chessman)){
				IsOver = true;//�����������Ϸ����
			}else{
				//��������ѡ��λ������
				int[] ComputerPosArray = computerDo();
				chessman = Chessman.WHITE.getChessman();
				chessboard.setBoard(ComputerPosArray[0], ComputerPosArray[1], chessman);
				
				//�жϼ�����Ƿ�Ӯ��
				if(isWin(ComputerPosArray[0],ComputerPosArray[1],chessman)){
					IsOver = true;
				}
			}
			
			if(IsOver){
				//����Ѿ�һ��ʤ������ѯ���û��Ƿ������Ϸ��������������ʼ�����̣�������Ϸ
				if(isReplay(chessman)){
					chessboard.initBoard();
					chessboard.printBoard();
					continue;
				}
				//��������������˳�����
				break;
			}
			
			chessboard.printBoard();
			System.out.println("�������������꣬Ӧ��x,y�ĸ�ʽ���룺 ");
		}
	}
	
	public boolean isReply(String chessman) throws Exception{
		chessboard.printBoard();
		String message = chessman.equals(Chessman.BLACK.getChessman())? "��ϲ�㣬��Ӯ��":"���ź���δʤ����";
		System.out.println(message + "����һ�֣���y/n��");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		if(br.readLine().equals("y")){
			//��ʼ��һ��
			return true;
		}
		return false;
	}
	
	/*������������*/
	public int[] computerDo(){
		int posX = (int) (Math.random() * (ChessBoard.board_size-1));
		int posY = (int) (Math.random() * (ChessBoard.board_size-1));
		
		String[][] board = chessboard.getBoard();
		while(board[posX][posY] != "ʮ"){
			//�����̸�λ���Ѿ�������ռ�ݵ�ʱ�������������������
			posX = (int) (Math.random()*ChessBoard.board_size-1);
			posY = (int) (Math.random()*ChessBoard.board_size-1);
		}
		int[] result = {posX,posY};
		return result;
	}
	
	public boolean isWin(int posX,int posY,String ico){
		//ֱ����������X
		int startX = 0;
		//ֱ�߽���������X
		int startY = 0;
		//ֱ�߽���X����
		int endX = ChessBoard.board_size - 1;
		//ֱ�߽���Y����
		int endY = ChessBoard.board_size - 1;
		//ͬ��ֱ�������������ۻ���
		int sameCount = 0;
		int temp = 0;
		//����������СX�����Y����
		temp = posX - win_count + 1;
		startX = temp < 0?0:temp;
		temp = posY - win_count + 1;
		startY = temp<0?0:temp;
		//�����յ�����X������Y����
		temp = posX + win_count - 1;
		endX = temp>ChessBoard.board_size-1?ChessBoard.board_size-1:temp;
		temp = posY + win_count - 1;
		endY = temp>ChessBoard.board_size-1?ChessBoard.board_size-1:temp;
		
		//�����ҷ��������ͬ�������ӵ���Ŀ
		String[][] board = chessboard.getBoard();
		for(int j = startY;j<endY;j++){
			if(board[posX][j] == ico && board[posX][j] == ico){
				sameCount++;
			}else if(sameCount != win_count - 1){
				sameCount = 0;
			}
		}
		
		if(sameCount == 0){
			//���ϵ��¼����������ӵ���Ŀ
			for(int i = startX;i<endX;i++){
				if(board[i][posY] == ico && board[i+1][posY] == ico){
					sameCount++;
				}else if(sameCount != win_count - 1){
					sameCount = 0;
				}
			}
		}
		
		if(sameCount == 0){
			//�����ϵ����¼�����ͬ�������ӵ���Ŀ
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
		String message = chessman.equals(Chessman.BLACK.getChessman())?"��ϲ������Ӯ��":"���ź���������";
		System.out.println(message + "����һ�֣�(y/n)");
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
