package com.odjwd.demo.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="Member24")
public class Member24 {
	@Id
	@NotBlank(message="* 아이디는 필수 입력입니다!")
	@Column(nullable=false)
	private String id;
	
	@NotNull
	@Size(min=4, message="* 최소 4자 이상!")
	@Column(length=50,nullable=false)
	private String password;
	
	@Column(length=45,nullable=true)
	private String mail;
	
	@NotNull
	@Size(min=2, message="* 이름은 필수 입력입니다!")
	@Column(length=50, nullable = false)
	private String name;
	
	@NotNull
	@Size(min=8,max=8, message="* 8자로 입력하세요!")
	@Column(length=30, nullable = false)
	private String birth;
	
	@NotBlank(message="* 성별을 선택하세요!")
	@Column(length=10,nullable=false)
	private String gender;
	
	@NotNull
	@Size(min=11,max=11, message="* - 생략!")
	@Column(length=13,nullable=false)
	private String phone;
}
