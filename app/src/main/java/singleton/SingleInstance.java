package singleton;

/**
 * Created by 饶建雄 on 2016/8/30.
 */
public class SingleInstance {
    private static int user_id;
//总页数
    private static int totalElements;
    //当前点击的item
    private static int pos;
    //题目类型
    private static int id;

    private static SingleInstance instance;

    private static String nickName;
    public static SingleInstance newInstance(){
        synchronized (SingleInstance.class){
            if (instance == null){
                instance = new SingleInstance();
            }
        }
        return instance;
    }
    public void setNickName(String nickName){
        this.nickName = nickName;
    }
    public String getNickName(){
        return nickName;
    }

    public void setTotalElements(int totalElements){
        this.totalElements = totalElements;
    }
    public int getTotalElements(){
        return totalElements;
    }
    public void setPos(int pos){
        this.pos = pos;
    }
    public int getPos(){
        return pos;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setUser_id(int user_id){
        this.user_id = user_id;
    }
    public int getUser_id(){
        return user_id;
    }
}
