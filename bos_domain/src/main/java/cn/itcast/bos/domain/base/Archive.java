package cn.itcast.bos.domain.base;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @description:妗ｆ绫伙紝璁板綍鎵�鏈夌殑鍒嗙被淇℃伅锛屽湪瀛愭。涓�
 */

@Entity
@Table(name = "T_ARCHIVE")
public class Archive {
	@Id
	@GeneratedValue
	@Column(name = "C_ID")
	private Integer id; // 涓婚敭
	@Column(name = "C_ARCHIVE_NUM", unique = true )
	private String archiveNum;// 妗ｆ缂栧彿
	@Column(name = "C_ARCHIVE_NAME")
	private String archiveName; // 妗ｆ鍚嶇О
	@Column(name = "C_REMARK")
	private String remark; // 澶囨敞
	@Column(name = "C_HASCHILD")
	private Integer hasChild;// 鏄惁鍒嗙骇 0浠ｈ〃涓嶅垎绾� 1浠ｈ〃鍒嗙骇
	@Column(name = "C_OPERATING_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date operatingTime;// 鎿嶄綔鏃堕棿
	@Column(name = "C_OPERATOR")
	private String operator; // 鎿嶄綔鍛�
	@Column(name = "C_OPERATING_COMPANY")
	private String operatingCompany; // 鎿嶄綔鍗曚綅

	@OneToMany(mappedBy = "archive")
	private Set<SubArchive> subArchives = new HashSet<>(); // 瀛愭。妗�

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getArchiveNum() {
		return archiveNum;
	}

	public void setArchiveNum(String archiveNum) {
		this.archiveNum = archiveNum;
	}

	public String getArchiveName() {
		return archiveName;
	}

	public void setArchiveName(String archiveName) {
		this.archiveName = archiveName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getOperatingTime() {
		return operatingTime;
	}

	public void setOperatingTime(Date operatingTime) {
		this.operatingTime = operatingTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Set<SubArchive> getSubArchives() {
		return subArchives;
	}

	public void setSubArchives(Set<SubArchive> subArchives) {
		this.subArchives = subArchives;
	}

	public String getOperatingCompany() {
		return operatingCompany;
	}

	public void setOperatingCompany(String operatingCompany) {
		this.operatingCompany = operatingCompany;
	}

	public Integer getHasChild() {
		return hasChild;
	}

	public void setHasChild(Integer hasChild) {
		this.hasChild = hasChild;
	}

}
