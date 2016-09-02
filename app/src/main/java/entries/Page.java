package entries;

import java.io.Serializable;
import java.util.List;

/**
 *分页模型类
 */
public class Page implements Serializable {

	/**
	 * size : 10
	 * totalPages : 1
	 * page : 1
	 * content : [{"answer":"错误","pubTime":1406044863000,"cataid":1,"typeid":3,"id":1,"content":"do while 和 while 没有区别."},{"answer":"3","pubTime":1406044974000,"options":"[{\"title\":\"1个\",\"checked\":false},{\"title\":\"2个\",\"checked\":false},{\"title\":\"3个\",\"checked\":false},{\"title\":\"4个\",\"checked\":true}]","cataid":1,"typeid":1,"id":2,"content":"int类型的占用几个字节？"}]
	 * totalElements : 2
	 */

	private int size;
	private int totalPages;
	private int page;
	private int totalElements;
	/**
	 * answer : 错误
	 * pubTime : 1406044863000
	 * cataid : 1
	 * typeid : 3
	 * id : 1
	 * content : do while 和 while 没有区别.
	 */

	private List<ContentBean> content;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	public List<ContentBean> getContent() {
		return content;
	}

	public void setContent(List<ContentBean> content) {
		this.content = content;
	}

	public static class ContentBean {
		private String answer;
		private long pubTime;
		private int cataid;
		private int typeid;
		private int id;
		private String content;

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
	}
}
