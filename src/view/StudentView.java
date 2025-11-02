package view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


import model.Student;
import model.StudentService;

// View : 사용자와 직접 상호작용하는 화면(UI)를 담당,
// (사용자에게) 입력을 받고 결과를 출력하는 역할
public class StudentView {
	
	// 필드
	private StudentService service = new StudentService();
	private Scanner sc = new Scanner(System.in);
	
	/**
	 * User 관리 프로그램 메인 메뉴 UI (View)
	 */
	public void mainMenu() {
		
		int input = 0; // 메뉴 선택용 변수
		
		do {
			try {
				
				System.out.println("\n===== STUDENT 관리 프로그램 =====\n");
				System.out.println("1. 학생 등록(INSERT)");
				System.out.println("2. 학생 전체 조회(SELECT)");
				System.out.println("3. 전공 학생 조회");
				System.out.println("4. STD_NO를 입력 받아 일치하는 User 조회(SELECT)");
				System.out.println("5. STD_NO를 입력 받아 일치하는 User 삭제(DELETE)");
				System.out.println("6. STD_NO, STD_ID 가 일치하는 회원이 있을 경우 이름 수정(UPDATE)");
				System.out.println("0. 프로그램 종료");

				System.out.print("메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine(); // 버퍼에 남은 개행문자 제거

				switch (input) {
				case 1: insertUser(); break;
				case 2: selectAll(); break;
				//case 3: selectMajor(); 
				case 4: selectUser(); break;
				case 5: deleteUser(); break;
				case 6: updateName(); break;
				
				case 0: System.out.println("\n[프로그램 종료]\n"); break;
				default: System.out.println("\n[메뉴 번호만 입력하세요]\n");
				}

				System.out.println("\n-------------------------------------\n");
				
				
			} catch (InputMismatchException e) {
				// Scanner 를 이용한 입력 시 자료형이 잘못된 경우
				System.out.println("\n***잘못 입력 하셨습니다***\n");
				
				input = -1; // 잘못 입력해서 while문 멈추는걸 방지
				sc.nextLine(); // 입력 버퍼에 남아있는 잘못된 문자 제거
				
			} catch (Exception e) {
				// 발생되는 예외를 모두 해당 catch 구문으로 모아서 처리
				e.printStackTrace();
			}
			
		} while(input != 0);
		
		
	}


	/** 6. ID, PW가 일치하는 회원이 있을(SELECT) 경우 이름 수정(UPDATE)
	 * 
	 */
	private void updateName() throws Exception{
		
		System.out.println("\n===6. 학번, 이름 일치하는 학생의 이름 수정===\n");
		
		System.out.print("학번 : ");
		String STD_NO = sc.next();
		
		System.out.print("이름 : ");
		String STD_NAME = sc.next();
		
		// 입력받은 ID, PW 가 일치하는 회원이 존재하는지 조회(SELECT)
		// -> 수정할 때 필요한 데이터 USER_NO 조회해오기.
		int userNo = service.selectUserNo(STD_NO, STD_NAME);
		
		// 조회 결과 없을 때
		if(userNo == 0) {
			System.out.println("학번, 이름 일치하는 학생 없음");
			return;
		}
		
		// 조회 결과 있을 때
		System.out.print("수정할 이름 입력 : ");
		String name = sc.next();
		
		// 위에서 조회된 회원(userNo)의 이름을 수정
		// 서비스 호출(UPDATE) 후 결과 반환(int) 받기
		int result = service.updateName(name, userNo);
		
		if(result > 0) System.out.println("수정 성공!!!");
		else		   System.out.println("수정 실패...");
		
		
	}

	/** 5. USER_NO를 입력받아 일치하는 User 삭제(DELETE)
	 * * DML 이다!! 
	 * 
	 * -- 삭제 성공했을 때 : 삭제 성공
	 * -- 삭제 실패했을 때 : 사용자 번호가 일치하는 User가 존재하지 않음
	 * 
	 */
	private void deleteUser() throws Exception{
		System.out.println("\n===5. STUDENT_NO를 입력받아 일치하는 STUDENT 삭제===\n");
		
		System.out.print("삭제할 사용자 번호 입력 : ");
		int input = sc.nextInt();
		
		int result = service.deleteUser(input);
		
		if(result > 0) System.out.println("삭제 성공");
		else 		   System.out.println("사용자 번호가 일치하는 STUDENT가 존재하지 않음");
		
	}

	/** 4. USER_NO 를 입력받아 일치하는 User 조회
	 * * 딱 1행만 조회되거나 or 일치하는 것 못찾았거나
	 * 
	 * -- 찾았을 때 : User 객체 출력
	 * -- 없을 때   : USER_NO가 일치하는 회원 없음
	 * 
	 */
	private void selectUser() throws Exception{
		
		System.out.println("\n===4. STD_NO 를 입력받아 일치하는 User 조회===\n");
		
		System.out.print("사용자 번호 입력 : ");
		int input = sc.nextInt();
		
		// service 호출 후 결과 반환받기
		// USER_NO (PK) == 중복이 있을 수 없다!
		// == 일치하는 사용자가 있다면 딱 1행만 조회된다
		// -> 1행의 조회 결과를 담기 위해서 User DTO 객체 1개 사용
		Student student = service.selectUser(input);
		
		// 조회 결과가 없으면 null , 있으면 null 이 아님
		if(student == null) {
			System.out.println("STD_NO가 일치하는 회원 없음");
			return;
		}
		
		System.out.println(student);
		
	}


	/** 2. User 전체 조회 관련 View (SELECT)
	 * 
	 */
	private void selectAll() throws Exception{
		
		System.out.println("\n====2. STUDENT 전체 조회====\n");
		
		// 서비스 호출(SELECT) 후 결과 반환(List<User>) 받기
		List<Student> StudentList = service.selectAll();
		
		// 조회 결과가 없을 경우
		if(StudentList != null) {
		
		}
		else {
			System.out.println("\n***조회 결과가 없습니다***\n");
		
			return;
		}
		
		// 조회 결과가 있을 경우 
		// userList에 있는 모든 User 객체 출력 
		// 향상된 for문 이용!
		for(Student student : StudentList) {
			System.out.println(student);
		}
		
	}

	/** 1. User 등록 관련된 View 
	 * 
	 */
	private void insertUser() throws Exception {
		
		System.out.println("\n====1. STUDENT 등록====\n");
		
		System.out.print("학번 : ");
		int std_NO = sc.nextInt();
		
		System.out.print("학생명 : ");
		String std_NAME = sc.next();
		
		System.out.print("나이 : ");
		int std_AGE = sc.nextInt();
		
		System.out.println("전공 : ");
		String major = sc.next();
		
		System.out.println("입학일 :");
		String ent_Date = sc.next();
		
		// 입력받은 값 3개를 한번에 묶어서 전달할 수 있도록
		// User DTO 객체를 생성한 후 필드에 값을 세팅
		Student student = new Student();
		
		// setter 이용
		student.setSTD_NO(std_NO);
		student.setSTD_NAME(std_NAME);
		student.setSTD_AGE(std_AGE);
		student.setMAJOR(major);
		student.setENT_DATE(ent_Date);
		
		// 서비스 호출(INSERT) 후 결과 반환(int, 결과 행의 갯수) 받기
		int result = service.insertUser(student);
		// service 객체(UserService)에 있는 insertUser() 라는 이름의 메서드를 호출하겠다
		
		// 반환된 결과에 따라 출력할 내용 선택
		if(result > 0) {
			System.out.println("\n" + std_NO + " 사용자가 등록되었습니다.\n");
			
		} else {
			System.out.println("\n***등록 실패***\n");
			
		}
	}
	
		/** 3. User 중 이름에 검색어가 포함된 회원 조회
		 * 검색어 입력 : 유
		 * 
		 */
		private void selectName() throws Exception{
			System.out.println("\n===3. User 중 이름에 검색어가 포함된 회원 조회===\n");
			
			System.out.print("검색어 입력 : ");
			String keyword = sc.next();
			
			// 서비스 호출 후 결과 반환받기
			List<Student> searchList = service.selectUserNo(keyword);
			
			if(searchList.isEmpty()) {
				System.out.println("검색 결과 없음");
				return;
			}
			
			for(Student student : searchList) {
				System.out.println(student);
			}
			
			
		}
		
	}
	
	
	
	
	
	
	
		

}