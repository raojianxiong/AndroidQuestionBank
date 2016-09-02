package entries;

import java.io.Serializable;

/**
 * 试题实体类
 * @author qiujy
 */
public class Question implements Serializable {

	/**
	 * answer : 错误
	 * pubTime : 1406044863000
	 * cataid : 1
	 * typeid : 3
	 * id : 1
	 * content : do while 和 while 没有区别.
	 */

	private String answer;
	private long pubTime;
	private int cataid;
	private int typeid;
	private int id;
	private String content;

	public String options;
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public long getPubTime() {
		return pubTime;
	}

	public void setPubTime(long pubTime) {
		this.pubTime = pubTime;
	}

	public int getCataid() {
		return cataid;
	}

	public void setCataid(int cataid) {
		this.cataid = cataid;
	}

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}




	/**
	 * 选择题的选项实体类
	 * @author qiujy
	 */
	public static class Item implements Serializable{

		public String title;
		public boolean checked;


		public String toString() {
			return "Item [title=" + this.title + ", checked=" + this.checked
					+ "]";
		}
	}
}
