package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.constant.Constants;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

import com.opensymphony.xwork2.ActionContext;

import freemarker.template.Configuration;
import freemarker.template.Template;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class PromotionAction extends BaseAction<Promotion> {
	
	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 基于WebService 获取bos_management 的活动列表 数据信息
		PageBean pageData = WebClient
				.create("http://localhost:9001/bos_management/services/promotionService/findPageData?page="
						+ page + "&rows=" + rows)
				.accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("totalCount", pageData.getTotalElements());
		result.put("pageData", pageData.getContent());

		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	/**
	 * 
	 * 页面静态化
	 * 
	 * */
	@Action(value = "promotion_showDetail")
	public String showDetail() throws Exception {
		// 先判断对应的html是否存在,如果存在 直接返回
		String htmlRealPath = ServletActionContext.getServletContext()
				.getRealPath("/freemarker");
		
		File htmlFile = new File(htmlRealPath + "/" + model.getId() + ".html");
		// 如果文件不存在
		if (!htmlFile.exists()) {
			
			Configuration configuration = new Configuration(
					Configuration.VERSION_2_3_22);
			configuration.setDirectoryForTemplateLoading(new File(
					ServletActionContext.getServletContext().getRealPath(
							"/WEB-INF/freemarker_templates")));

			// 获取模版对象
			Template template = configuration
					.getTemplate("promotion_detail.ftl");
			// 动态数据
			Promotion promotion = WebClient
					.create(
							Constants.BOS_MANAGEMENT_URL
							//"http://localhost:9001/bos_management"
							+ "/services/promotionService/promotion/"
							+ model.getId()).accept(MediaType.APPLICATION_JSON)
					.get(Promotion.class);
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("promotion", promotion);
			// 合并输出
			template.process(parameterMap, new OutputStreamWriter(
					new FileOutputStream(htmlFile), "utf-8"));
		}

		// 如果文件存在,直接将文件返回
		ServletActionContext.getResponse().setContentType(
				"text/html;charset=utf-8");
		FileUtils.copyFile(htmlFile, ServletActionContext.getResponse()
				.getOutputStream());
		return NONE;

	}
}