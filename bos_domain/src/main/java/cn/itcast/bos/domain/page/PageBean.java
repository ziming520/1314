package cn.itcast.bos.domain.page;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import cn.itcast.bos.domain.take_delivery.Promotion;

/**
 * 自定义分页 数据Bean
 * 
 * @author itcast
 *
 * @param <T>
 */
@XmlRootElement(name = "pageBean")
@XmlSeeAlso({ Promotion.class })
public class PageBean<T> {
	private long totalElements;
	private List<T> content;

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

}
