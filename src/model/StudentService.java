package model;


import java.sql.Connection;
import java.util.List;

import common.JDBCTemplate;
import model.Student;
	import model.StudentDAO;
	

	// (Model 중 하나) Service : 비즈니스 로직을 처리하는 계층, 
	// 데이터를 가공하고 트랜잭션(commit, rollback) 관리 수행
	public class StudentService {
		
		// 필드
		private StudentDAO dao = new StudentDAO();

		/** 1. User 등록 서비스
		 * @param student : 입력받은 id, pw, name 이 세팅된 객체
		 * @return insert 된 결과 행의 갯수
		 */
		public int insertUser(Student student) throws Exception {
			
			// 1. 커넥션 생성
			Connection conn = JDBCTemplate.getConnection();
			
			// 2. 데이터 가공(할 것 없으면 생략)
			
			// 3. DAO 메서드 호출 후 결과 반환받기
			int result = dao.insertUser(conn, student);
			
			// 4. DML(INSERT) 수행 결과에 따라 트랜잭션 제어 처리
			if(result > 0) { // INSERT 성공
				JDBCTemplate.commit(conn);
				
			} else { // INSERT 실패
				JDBCTemplate.rollBack(conn);
				
			}
			
			// 5. Connection 반환하기
			JDBCTemplate.close(conn);
			
			// 6. 결과 반환
			return result;
		}

		/** 2. User 전체 조회 서비스
		 * @return 조회된 User들이 담긴 List
		 */
		public List<Student> selectAll() throws Exception {
			
			// 1. 커넥션 생성
			Connection conn = JDBCTemplate.getConnection();
			
			// 2. DAO 메서드 호출(SELECT) 후 결과반환(List<User>) 받기
			List<Student> studentList = dao.selectAll(conn);
			
			// 3. Connection 반환
			JDBCTemplate.close(conn);
			
			// 4. 결과 반환
			return studentList;
		}


		/** 4. USER_NO를 입력받아 일치하는 USER 조회 서비스
		 * @param input
		 * @return
		 */
		public Student selectUser(int input) throws Exception{
			
			Connection conn = JDBCTemplate.getConnection();
			
			Student student = dao.selectUser(conn, input);
			
			JDBCTemplate.close(conn);
			
			return student;
		}

		/** 5. USER_NO를 입력받아 일치하는 User 삭제 서비스
		 * @param input
		 * @return
		 */
		public int deleteUser(int input) throws Exception {

			Connection conn = JDBCTemplate.getConnection();
			
			int result = dao.deleteUser(conn, input);
			
			if(result > 0) {
				JDBCTemplate.commit(conn);
			} else {
				JDBCTemplate.rollBack(conn);
			}
			
			JDBCTemplate.close(conn);
			
			return result;
		}

		/** 6. 전공 일치하는 회원이 있는지 조회(SELECT)
		 * @param major
		 * @return
		 */
		public String selectUserNo(String major) throws Exception{
			
			Connection conn = JDBCTemplate.getConnection();
			
			String MAJOR = dao.selectUser(conn, major);
			
			JDBCTemplate.close(conn);
			
			return MAJOR;
		}
		/** 6-1. ID, PW가 일치하는 회원이 있는지 조회(SELECT)
		 * @param userId
		 * @param userPw
		 * @return
		 */
		public int selectUserNo(String STD_NO, String STD_NAME) throws Exception{
			
			Connection conn = JDBCTemplate.getConnection();
			
			int userNo = dao.selectUser(conn, STD_NO, STD_NAME);
			
			JDBCTemplate.close(conn);
			
			return userNo;
		}

		/** 6-2. USER_NO가 일치하는 회원의 이름 수정 서비스(UPDATE)
		 * @param name
		 * @param userNo
		 * @return
		 */
		public int updateName(String name, int userNo) throws Exception{
			
			Connection conn = JDBCTemplate.getConnection();
			
			int result = dao.updateName(conn, name, userNo);
			
			if(result > 0) JDBCTemplate.commit(conn);
			else		   JDBCTemplate.rollBack(conn);
			
			JDBCTemplate.close(conn);
			
			return result;
		}

		
		
		
		

	


}