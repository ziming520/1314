package cn.itcast.bos.web.action.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 抽取 Action的公共代码 ，简化开发
 * 
 * @author itcast
 *
 */
public abstract class BaseAction<T> extends ActionSupport implements
		ModelDriven<T> {

	// 模型驱动
	protected T model;

	@Override
	public T getModel() {
		return model;
	}

	// 构造器 完成model实例化
	public BaseAction() {
		// 构造子类Action对象 ，获取继承父类型的泛型
		// AreaAction extends BaseAction<Area>
		// BaseAction<Area>
		// 在子类中的构造方法:this(super()...),过程中虽然调用父类的构造方法但不会创建父类的对象,所以由子类对象调用时,这里的this表示的是子类对象
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		// 获取类型第一个泛型参数
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		//getActualTypeArguments()返回表示此类型实际类型参数的 Type 对象的数组。
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.out.println("模型构造失败...");
		}
	}

	// 接收分页查询参数
	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	// 将分页查询结果数据，压入值栈的方法
	protected void pushPageDataToValueStack(Page<T> pageData) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());

		ActionContext.getContext().getValueStack().push(result);
	}
}
