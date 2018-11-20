package DataBases;

public class UserVO {
    private String id;
    private String password;
    private int flag;
    public UserVO() {
    	
    }
    public UserVO(String id,String password){
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
    public int getFlag() {
    	return flag;
    }
    public void setFlag(int flag) {
    	this.flag = flag;
    }
}

