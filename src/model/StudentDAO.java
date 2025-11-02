package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// import static : 지정된 경로에 존재하는 static 구문을 모두 얻어와
// 클래스명.메서드명() 이 아닌 메서드명() 만 작성해도 호출 가능하게 함.
import static common.JDBCTemplate.*;
import static common.JDBCTemplate.close;
import static common.JDBCTemplate.close;


// (Model 중 하나) DAO (Data Access Object : 데이터 접근 객체)
// 데이터가 저장된 곳(DB)에 접근하는 용도의 객체
// -> DB에 접근하여 Java에서 원하는 결과를 얻기 위해
// SQL을 수행하고 결과를 반환받는 역할
public class StudentDAO {
	
	// 필드
	// - DB 접근 관련한 JDBC 객체 참조 변수 선언
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	/** 1. User 등록 DAO
	 * @param conn : DB 연결 정보가 담겨있는 Connection 객체
	 * @param user : 입력받은 id,pw,name 이 세팅된 User 객체
	 * @return INSERT 결과 행의 갯수
	 */
	public int insertUser(Connection conn, Student student) throws Exception {
		
		// 1. 결과 저장용 변수 선언
		int result = 0;
		
		try {
			// 2. SQL 작성
			String sql = """
					INSERT INTO TB_USER
					VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, ?, ?, DEFAULT)
					""";
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 위치홀더에 알맞은 값 대입
			pstmt.setInt(1, student.getSTD_NO());
			pstmt.setString(2, student.getSTD_NAME());
			pstmt.setInt(3, student.getSTD_AGE());
			pstmt.setString(3, student.getMAJOR());
			pstmt.setString(3, student.getENT_DATE());
			
			// 5. SQL(INSERT) 수행(executeUpdate()) 후 
			// 결과(삽입된 행의 갯수) 반환 받기
			result = pstmt.executeUpdate();
			
		} finally {
			
			// 6. 사용한 jdbc 객체 자원 반환
			close(pstmt);
			
		}
		
		
		// 결과 저장용 변수에 저장된 최종 값 반환
		return result;
	}

	/** 2. User 전체 조회 DAO
	 * @param conn
	 * @return List<User> userList
	 */
	public List<Student> selectAll(Connection conn) throws Exception {
		
		// 1. 결과 저장용 변수 선언
		List<Student> studentList = new ArrayList<Student>();
		
		try {
			// 2. SQL 작성 
			String sql = """
				SELECT STD_NO, STD_NAME, STD_AGE, MAJOR, ENT_DATE, 
				TO_CHAR(ENT_DATE, 'YYYY"년" MM"월" DD"일"') ENT_DATE
				FROM KH_STUDENT
				ORDER BY STD_NO
				""";
			
			// 3. PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? (위치홀더)에 알맞은 값 대입(없으면 패스)
			
			// 5. SQL(SELECT) 수행(executeQuery()) 후 결과 반환(ResultSet) 받기
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과(rs)를 1행씩 접근하여 컬럼 값 얻어오기
			// 몇 행이 조회될지 모른다 -> while
			// 무조건 1행만 조회된다   -> if
			
			while(rs.next()) {
				
				int stdNo = rs.getInt("STD_NO");
				String stdName = rs.getString("STD_NAME");
				int stdAge = rs.getInt("STD_AGE");
				String Major = rs.getString("MAJOR");
				String entDate= rs.getString("ENT_DATE");
				// java.sql.Date 타입으로 값을 저장하지 않은 이유
				// -> SELECT 문에서 TO_CHAR()를 이용하여 문자열 형태로 
				// 변환해 조회해왔기 때문.
				
				// User 객체 새로 생성하여 DB에서 얻어온 컬럼값 필드로 세팅
				Student student = new Student(stdNo, stdName, stdAge, Major, entDate);
				
				studentList.add(student);
				
			}
			
		} finally {
			// 7. 사용한 자원 반환
			close(rs);
			close(pstmt);
			
		}
		
		// 조회 결과가 담긴 List 반환
		return studentList;
	}

	

	/** 4. USER_NO를 입력받아 일치하는 User 조회 DAO
	 * @param conn
	 * @param major
	 * @return
	 */
	public Student selectUser(Connection conn, int input) throws Exception {
		
		Student student = null;
		
		try {
			String sql = """
			SELECT STD_NO, STD_NAME, STD_AGE, MAJOR, ENT_DATE 
			TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENT_DATE
			FROM KH_STUDENT
			WHERE STD_NO = ?
			""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				int stdNo = rs.getInt("STD_NO");
				String stdId = rs.getString("STD_NAME");
				int stdAge = rs.getInt("STD_AGE");
				String major = rs.getString("MAJOR");
				String entDate = rs.getString("ENT_DATE");
				
				student = new Student(stdNo, stdId, stdAge, major, entDate);
				
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return student;
	}

	/** 5. USER_NO를 입력 받아 일치하는 User 삭제 DAO
	 * @param conn
	 * @param input
	 * @return
	 */
	public int deleteUser(Connection conn, int input) throws Exception{
		
		int result = 0;
		
		try {
			String sql = """
					DELETE FROM KH_STUDENT
					WHERE STD_NO = ?
					""";
		
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, input);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
			
		}
		
		return result;
	}

	/** 6. 전공 일치하는 회원의 USER_NO 조회 DAO
	 * @param MAJOR
	 * @return
	 */
	public String selectUser(Connection conn, String major) 
			throws Exception {
		
		String MAJOR = null;
		
		try {
			String sql = """
					SELECT STD_NO
					FROM KH_STUDENT
					WHERE MAJOR = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, MAJOR);
			
			
			rs = pstmt.executeQuery();
			
			// 조회된 행이 1개가 있을 경우
			if(rs.next()) {
				MAJOR = rs.getString("MAJOR");
			}
			
			
		} finally {
			close(rs);
			close(pstmt);
		}
	
		return MAJOR; // 조회 성공 USER_NO, 실패 0 반환
	}

	/** 6-2. USER_NO가 일치하는 회원의 이름 수정 DAO
	 * @param conn
	 * @param name
	 * @param userNo
	 * @return
	 */
	public int updateName(Connection conn, String STD_NAME, int STD_NO) 
													throws Exception {
		
		int result = 0;
		
		try {
			String sql = """
					UPDATE KH_STUDENT
					SET STD_NAME = ?
					WHERE STD_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, STD_NAME);
			pstmt.setInt(2, STD_NO);
			
			result = pstmt.executeUpdate();
			
			
		} finally {
			close(pstmt);
			
		}
		
		return result;
	}
	
	/** 6-1. ID, PW가 일치하는 회원의 USER_NO 조회 DAO
	 * @param conn
	 * @param userId
	 * @param userPw
	 * @return
	 */
	public int selectUser(Connection conn, String STD_NAME, String STD_NO) 
			throws Exception {
		
		int userNo = 0;
		
		try {
			String sql = """
					SELECT USER_NO
					FROM KH_STUDENT
					WHERE USER_ID = ?
					AND USER_PW = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, STD_NO);
			pstmt.setString(2, STD_NAME);
			
			rs = pstmt.executeQuery();
			
			// 조회된 행이 1개가 있을 경우
			if(rs.next()) {
				userNo = rs.getInt("USER_NO");
			}
			
			
		} finally {
			close(rs);
			close(pstmt);
		}
	
		return userNo; // 조회 성공 USER_NO, 실패 0 반환
	}
	
}