package com.android.VO;

public class MemberVO { //도메인 객체 클래스  테이블의 속성과 유사 //Value Object 
	private String userid;
	private String usernum;
	private String password;
	private String name;
	private String university;

	public String getUserId() {
		return userid;
	}

	public void setUserId(String userid) {
		this.userid = userid;
	}

	
	public String getUserNum() {
		return usernum;
	}

	public void setUserNum(String userNum) {
		this.usernum = userNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}



	@Override
	public String toString() {
		return "MemberVO [userid=" + userid + ", usernum=" + usernum + ", password=" + password + ", name=" + name
				+ ", university=" + university + "]";
	}
	

}
