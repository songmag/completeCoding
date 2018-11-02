package com.android.test;


import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.android.DAO.MemberDAO;
import com.android.VO.MemberVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/*.xml" })  // 애플리케이션 컨텍스트의 설정파일위치를 지정
public class MemberDAOTest {
	@Inject
	private MemberDAO dao;

	@Test //junit 어노테이션  테스트
	public void testTime() throws Exception {
		System.out.println("현재 시간 : " + dao.getTime());
	}

	@Test
	public void testInsertMember() throws Exception {
		MemberVO vo = new MemberVO();
		vo.setUserId("3");
		vo.setUserNum("12345678");
		vo.setPassword("112233");
		vo.setName("Honggildong");
		vo.setUniversity("Sejong");
		dao.insertMeMber(vo);
	}
	
	@Test
	public void testReadMember() throws Exception {
		String check_num = "122126";
		String check_password = "1234";
		
		dao.readWithPW(check_num,check_password );
	}
	

	
}

	