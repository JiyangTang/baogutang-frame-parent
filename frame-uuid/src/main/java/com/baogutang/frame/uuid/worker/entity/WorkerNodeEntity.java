package com.baogutang.frame.uuid.worker.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("worker_node")
public class WorkerNodeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * auto increment id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * host name
	 */
	private String hostName;
	/**
	 * port
	 */
	private String port;
	/**
	 * node type: ACTUAL or CONTAINER
	 */
	private Integer type;
	/**
	 * launch date
	 */
	private Date launchDate;
	/**
	 * modified time
	 */
	private Date modified;
	/**
	 * created time
	 */
	private Date created;

}
