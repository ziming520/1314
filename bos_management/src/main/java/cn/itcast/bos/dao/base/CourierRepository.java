package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;

/**
 * JpaSpecificationExecutor 不属于Repository 体系。由于JpaSpecificationExecutor
 * 并不继承repository 接口，所以它不能单独使用，只能和jpa Repository 一起用。
 * JpaSpecificationExecutor特色：实现了带条件的查询， 类似于Hibernate 的cretira
 * 
 * T getOne(ID id); 
 * 
 * 继承JpaRepository，JpaSpecificationExecutor 接口的接口：IStudentJpaRepository
 * T findOne(Specification<T>); 
 * List<T> findAll(Specification<T>);
 * List<T> findAll(Specification<T>, Sort);
 * List<T> findAll(Specification<T>,Pageable); 
 * long count(Specification<T>);
 * 
 * */
public interface CourierRepository extends JpaRepository<Courier, Integer>,
		JpaSpecificationExecutor<Courier> {

	@Query(value = "update Courier set deltag='1' where id = ?")
	@Modifying
	public void updateDelTag(Integer id);

	@Query(value = "update Courier set deltag='' where id = ?")
	@Modifying
	public void updateDelTag_restore(int parseInt);

}
