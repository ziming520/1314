package cn.itcast.bos.service.take_delivery;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

public interface PromotionService {

	// 保存宣传任务
	public void save(Promotion promotion);

	// 分页查询
	public Page<Promotion> findPageData(Pageable pageable);

	// rs分页查询
	@Path("/findPageData")
	@GET
	// @Consumes("application/json")
	@Produces({ "application/xml", "application/json" })
	public PageBean<Promotion> findPageData(@QueryParam("page") Integer page,
			@QueryParam("rows") Integer rows);

	@Path("/promotion/{id}")
	@GET
	// @Consumes("application/json")
	@Produces({ "application/xml", "application/json" })
	public Promotion findById(@PathParam("id") Integer id);

	// 设置活动过期
	void updateStatus(Date date);
}
