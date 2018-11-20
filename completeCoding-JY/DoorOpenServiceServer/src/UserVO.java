
public class UserVO {
    private String id;
    private String password;
    private int flag;
    private String company;
    private String name;
    public UserVO() {
    	
    }
    public UserVO(String id,String password,int flag,String company,String name){
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
    
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

