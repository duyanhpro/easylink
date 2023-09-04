package easylink.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Role hierachy
 */
@Entity
@Table(name = "tbl_role_tree")
public class RoleTree extends BaseEntity {

	// Fields

	/**
	 * 
	 */
	private Integer parentId;
	private Integer childId;

	/** default constructor */
	public RoleTree() {
	}

	public RoleTree(Integer childId, Integer parentId) {
		super();
		this.parentId = parentId;
		this.childId = childId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getChildId() {
		return childId;
	}

	public void setChildId(Integer childId) {
		this.childId = childId;
	}


}