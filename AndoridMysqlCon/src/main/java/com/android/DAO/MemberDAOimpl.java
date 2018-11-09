package com.android.DAO;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.android.VO.MemberVO;

@Repository  // DAO�� spring �� (��Ŭ��������) �ν� ��Ű�� ���� ����ϴ� ������̼�
public class MemberDAOimpl implements MemberDAO { //DAO ���� Ŭ����
	@Inject // JAVA���� ����ϴ� Ÿ�Կ� ���� �������ִ� ����� ���� ������̼�
	private SqlSession sqlSession;

	private static final String namespace = "com.android.memberMapper"; 

	@Override  //DAO �������̽��� �߻�Ŭ���� ���� �������̵� // �ð����
	public String getTime() { 
		return sqlSession.selectOne(namespace + ".getTime");
	}

	@Override//��� ���ڵ� �߰�
	public void insertMeMber(MemberVO memberVo) { 
		sqlSession.insert(namespace + ".insertMember", memberVo);
	}

	@Override //���̵�� ��й�ȣ�� �Է¹޾� ������ ������ �˻�
	public MemberVO readWithPW(String usernum, String password) throws Exception {
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("usernum", usernum);
		paramMap.put("password", password);
		
		return sqlSession.selectOne(namespace+".readWithPw", paramMap);
	}


}
