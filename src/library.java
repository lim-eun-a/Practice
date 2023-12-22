import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class library
{
	Scanner sc = new Scanner(System.in);
	public Connection con;
	public Statement stmt;
	public PreparedStatement psmt;
	public ResultSet rs;
	
	public library() {
		//sql 연결
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String id = "homework";
			String pass = "1234";

			con = DriverManager.getConnection(url,id,pass);
			System.out.println("Oracle 연결성공");
		}
		catch(Exception e) {
			System.out.println("Oracle 연결시 예외발생");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		library book = new library();
		book.doRun();
	}
	

	public void showMenu()
	{
		System.out.println("┌────[메뉴 선택]────┐");
		System.out.println("│1.    책 등록      │");
		System.out.println("│2.    도서조회     │");
		System.out.println("│3.전체 리스트 조회 │");
		System.out.println("│4. 낡은 책 버리기  │");
		System.out.println("│5.      종료       │");
		System.out.println("└───────────────────┘");
		System.out.print(" 선택 : ");
	}
	
	
	public void doRun() {
		while(true) {
			showMenu();
			int choice = sc.nextInt();
			sc.nextLine();
			switch(choice) {
			case 1:
				addBook();
				break;
			case 2:
				searchBook();
				break;
			case 3:
				allBook();
				break;
			case 4:
				delBook();
				break;
			case 5:
				System.out.println("프로그램을 종료합니다.");
				System.out.println("이용해주셔서 감사합니다.");
				return;
			default:
				System.out.println("잘못 입력하셨습니다.");
				break;	
			}
		}
	}
	
	// 1. 책 등록
	public void addBook() {
		try
		{
			System.out.print("제목 : ");
			String bookname = sc.nextLine();
			System.out.print("권수 : ");
			int howmany = sc.nextInt();
			
			String sql = "INSERT INTO BOOKDB VALUES(seq_book_num.nextval, ?, ?)";
			psmt = con.prepareStatement(sql);
			
			psmt.setString(1, bookname);
			psmt.setInt(2, howmany);
			
			int updateCount = psmt.executeUpdate();
			System.out.println(updateCount + "행이 추가되었습니다.");
			
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("데이터 입력에 실패했습니다.(#데이터예외)");
		}
	}
	
	//2. 책 조회
	public void searchBook() {
		System.out.print("조회할 책 제목 : ");
		String bookname = sc.nextLine();
		try{	
			String sql = "select * from bookDB where bookname = ?";
			
			psmt = con.prepareStatement(sql);
			psmt.setString(1, bookname);
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				System.out.println("책번호 : " + rs.getString("booknum"));
				System.out.println("제  목 : " + rs.getString("bookname"));
				System.out.println("수  량 : " + rs.getInt("howmany"));
				System.out.println("----------------------------------------");
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("데이터 입력에 실패했습니다.(#3)");
		}
	}
	
	//3. 전체 리스트 조회
	public void allBook() {
		try{	
			String sql = "select * from bookDB order by booknum";
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery(sql);
		
			while(rs.next()) {
				System.out.println("책번호 : "+rs.getString("booknum"));
				System.out.println("제  목 : "+rs.getString("bookname"));
				System.out.println("수  량 : " + rs.getInt("howmany"));
				System.out.println("----------------------------------------");
			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//4. 낡은 책 버리기
	public void delBook() {
		System.out.print("삭제할 책 제목 : ");
		String bookname = sc.nextLine();
		try{	
			String sql = "delete from bookDB where bookname = ?";
			psmt = con.prepareStatement(sql);
			psmt.setString(1, bookname);
			int updateCount = psmt.executeUpdate();
			System.out.println(updateCount+"행이 삭제되었습니다.");
		}catch(Exception e) {
			System.out.println("데이터베이스 삭제 에러입니다.");
		}
	}

}