package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.CourierService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements
		ModelDriven<Courier> {

	// 模型驱动
	private Courier courier = new Courier();

	@Override
	public Courier getModel() {
		return courier;
	}

	// 注入Service
	@Autowired
	private CourierService courierService;

	// 添加快递员方法
	@Action(value = "courier_save", results = { @Result(name = "success", location = "./pages/base/courier.html", type = "redirect") })
	public String save() {
		courierService.save(courier);
		return SUCCESS;
	}

	// 属性驱动接收客户端分页参数
	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	// 分页查询方法
	@Action(value = "courier_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 封装Pageable对象
		Pageable pageable = new PageRequest(page - 1, rows);

		// 规约模式
		// 封装条件查询对象 Specification
		Specification<Courier> specification = new Specification<Courier>() {

			// Root 用于获取属性字段，CriteriaQuery可以用于简单条件查询，CriteriaBuilder 用于构造复杂条件查询
			/**
			 * Root接口：代表Criteria查询的根对象，Criteria查询的查询根定义了实体类型，
			 * 能为将来导航获得想要的结果，它与SQL查询中的FROM子句类似
			 * Root实例是类型化的，且定义了查询的FROM子句中能够出现的类型。
			 * 
			 * CriteriaQuery接口：代表一个specific的顶层查询对象，它包含着查询的各个部分， 比如：select
			 * 、from、where、group by、order by等
			 * 注意：CriteriaQuery对象只对实体类型或嵌入式类型的Criteria查询起作用
			 * 
			 * CriteriaBuilder接口：用来构建CritiaQuery的构建器
			 * 
			 * <X> Expression<X> as(java.lang.Class<X> type)
			 * 执行类型转换时的表达式，返回一个新的表达式对象。
			 * 
			 * */
			@Override
			public Predicate toPredicate(Root<Courier> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();

				// 简单单表查询
				// 快递员编号查询
				if (StringUtils.isNotBlank(courier.getCourierNum())) {

					// 理解上类似正则表达式,pattern和Matcher 可以参考理解
					Predicate p1 = cb.equal(
					// 相当于匹配器(匹配类型和字段)
							root.get("courierNum").as(String.class),
							// 相当于匹配条件(匹配字符)
							courier.getCourierNum());
					list.add(p1);
				}
				// 快递员分公司查询,模糊查询
				if (StringUtils.isNotBlank(courier.getCompany())) {
					Predicate p2 = cb.like(
							root.get("company").as(String.class),
							"%" + courier.getCompany() + "%");
					list.add(p2);
				}
				// 快递员类型查询
				if (StringUtils.isNotBlank(courier.getType())) {
					Predicate p3 = cb.equal(root.get("type").as(String.class),
							courier.getType());
					list.add(p3);
				}

				// 快递员区域模糊查询
				// 多表查询
				Join<Courier, Standard> standardJoin = root.join("standard",
						JoinType.INNER);
				if (courier.getStandard() != null
						&& StringUtils.isNotBlank(courier.getStandard()
								.getName())) {
					Predicate p4 = cb.like(
							standardJoin.get("name").as(String.class), "%"
									+ courier.getStandard().getName() + "%");
					list.add(p4);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};

		// 调用业务层 ，返回 Page
		Page<Courier> pageData = courierService.findPageData(specification,
				pageable);
		// 将返回page对象 转换datagrid需要格式
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());
		// 将结果对象 压入值栈顶部
		ActionContext.getContext().getValueStack().push(result);

		return SUCCESS;
	}

	// 属性驱动
	private String ids;

	public void setIds(String ids) {
		this.ids = ids;
	}

	// 作废快递员
	@Action(value = "courier_delBatch", results = { @Result(name = "success", location = "./pages/base/courier.html", type = "redirect") })
	public String delBatch() {
		// 按,拆分ids得到数组
		String[] idArray = ids.split(",");
		// 调用业务层，批量作废
		courierService.delBatch(idArray);

		return SUCCESS;
	}

	// 快递员批量还原
	// courier_delBatch
	@Action(value = "courier_restoreBatch", results = { @Result(name = "success", type = "redirect", location = "./pages/base/courier.html") })
	public String restoreBatch() {
		String[] idArray = ids.split(",");
		// 调用业务层,批量作废
		courierService.restoreBatch(idArray);
		return SUCCESS;
	}

	@Action(value = "courier_findnoassociation", results = { @Result(name = "success", type = "json") })
	public String findnoassociation() {
		// 调用业务层，查询未关联定区的快递员
		List<Courier> couriers = courierService.findNoAssociation();
		// 将查询快递员列表 压入值栈
		ActionContext.getContext().getValueStack().push(couriers);
		return SUCCESS;
	}
}
