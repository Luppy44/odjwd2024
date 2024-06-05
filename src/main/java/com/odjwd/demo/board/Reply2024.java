package com.odjwd.demo.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name="Reply2024")
public class Reply2024 {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable=false)
	private long replyid;
	
	@Column(length=40,nullable=false)
	private String id;
	
	@Column(length=40,nullable=false)
	private long boardid;
	
	@Column(nullable = false)
	private String recontent;
}
