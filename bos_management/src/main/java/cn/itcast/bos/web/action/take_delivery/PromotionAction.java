package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.elasticsearch.action.get.GetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;
import cn.itcast.bos.web.action.common.BaseAction;

/**
 * 
 * 宣传活动管理
 * 
 * */

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
	
	private File titleImgFile;
	private String titleImgFileFileName;

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	@Autowired
	private PromotionService promotionService;

	@Action(value = "promotion_save", results = { @Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html") })
	public String save() throws IOException {
		//宣传图上传
		String savePath = ServletActionContext.getServletContext().getRealPath(
				"/upload/");
		String saveUrl = ServletActionContext.getRequest().getContextPath()
				+ "/upload/";
		UUID uuid = UUID.randomUUID();
		String ext = titleImgFileFileName.substring(titleImgFileFileName
				.lastIndexOf("."));
		String randomFileName = uuid + ext;

		FileUtils.copyFile(titleImgFile, new File(savePath + "/"
				+ randomFileName));

		model.setTitleImg(ServletActionContext.getRequest().getContextPath()
				+ "/upload/" + randomFileName);

		promotionService.save(model);

		return SUCCESS;
	}

	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 构造分页参数 6
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Promotion> pageData = promotionService.findPageData(pageable);

		pushPageDataToValueStack(pageData);
		return SUCCESS;

	}
}
