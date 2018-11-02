package com.android.DAO;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.android.VO.MemberVO;

@Repository  // DAO를 spring 에 (빈클래스임을) 인식 시키기 위해 사용하는 어노테이션
public class MemberDAOimpl implements MemberDAO { //DAO 구현 클래스
	@Inject // JAVA에서 사용하는 타입에 맞춰 연결해주는 기능을 가진 어노테이션
	private SqlSession sqlSession;

	private static final String namespace = "com.android.memberMapper"; 

	@Override  //DAO 인터페이스의 추상클래스 들을 오버라이딩 // 시간출력
	public String getTime() { 
		return sqlSession.selectOne(namespace + ".getTime");
	}

	@Override//멤버 레코드 추가
	public void insertMeMber(MemberVO memberVo) { 
		sqlSession.insert(namespace + ".insertMember", memberVo);
	}

	@Override //아이디와 비밀번호를 입력받아 나머지 정보를 검색
	public MemberVO readWithPW(String usernum, String password) throws Exception {
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("usernum", usernum);
		paramMap.put("password", password);
		
		return sqlSession.selectOne(namespace+".readWithPw", paramMap);
	}


}
