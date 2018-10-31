package com.android.DAO;

import com.android.VO.MemberVO; 
//DAO 인터페이스  // database의 data에 access 하는 트랜잭션 객체
public interface MemberDAO {
	public String getTime();  //현재시간을 출력 

	public void insertMeMber(MemberVO memberVo); //멤버를 추가 
	
	public MemberVO readWithPW(String usernum,String password) throws Exception; //아이디와 비밀번호를 입력받아 나머지 정보를 검색
	
}
