package com.odjwd.demo.board;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name="Board2024")
public class Board2024 {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable=false)
	private long boardid;
	
	@NotBlank(message="* 지역을 선택하세요!")
	@Column(length=40,nullable=false)
	private String areacode;
	
	@Column(length=50, nullable = false)
	private String id;
	
	@Column(length=50, nullable = false)
	private String name;
	
	@NotNull
	@Size(min=2, message="* 제목을 2자 이상 입력하세요!")
	@Column(length=100, nullable = false)
	private String title;
	
	@NotBlank(message="* 내용을 1자 이상 입력하세요!")
	@Column(nullable = false)
	private String content;
	
	@Column(nullable=true)
	private LocalDate timestamp;
	
	@Column(columnDefinition = "integer default 0", nullable = false)
	private int hit;
	
	@Column(columnDefinition = "integer default 0", nullable = false)
	private long recnt;
	
	@Column(columnDefinition = "integer default 0", nullable = false)
	private int complete;
	
	@Column(nullable=true)
	private String filename;
	
	@Column(nullable=true)
	private String filepath;
}
