package com.android.DAO;

import com.android.VO.MemberVO; 
//DAO �������̽�  // database�� data�� access �ϴ� Ʈ����� ��ü
public interface MemberDAO {
	public String getTime();  //����ð��� ��� 

	public void insertMeMber(MemberVO memberVo); //����� �߰� 
	
	public MemberVO readWithPW(String usernum,String password) throws Exception; //���̵�� ��й�ȣ�� �Է¹޾� ������ ������ �˻�
	
}
