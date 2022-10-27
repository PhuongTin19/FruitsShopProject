package com.tin.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Accounts")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
	private Integer account_id;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "role_id", referencedColumnName = "role_id")
	@JsonIgnoreProperties(value = {"application", "hibernateLazyInitializer"})
	private Role role;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider;
	
	
	@Column(name = "verification_code")
    private String verificationCode;
	
	@Column(name = "fullname")
	@NotBlank(message = "Không để trống họ và tên!")
	@Type(type="org.hibernate.type.StringNVarcharType")
	private String fullname;
	
	@Column(name = "username")
	@NotBlank(message = "Không để trống tài khoản!")
	private String username;
	
	@Column(name = "password")
	@NotBlank(message = "Không để trống mật khẩu!")
	private String password;

	@Column(name = "email")
	@NotBlank(message="Không để trống email!")
	@Email(message = "Email không đúng định dạng!")
	private String email;
 
	
	@Transient
    public String getPhotosImagePath() {
        if (image == null || username == null) {
            return "/dashboard/img/user.png";
        }else if(image.equals("") || image.equals("user.png")){
            return "/dashboard/img/user.png";
        }
        else if(provider == null || provider.equals(Provider.DATABASE) || provider.equals(Provider.FACEBOOK)){
            return "/photos/" + username + "/" + image;
        }
        else if(provider.equals(Provider.GOOGLE)){
            return image;
        }

        return "/photos/" + username + "/" + image;
    }
	
	 
	@Column(name = "image")
	private String image;

	@Column(name = "phone")
	@NotBlank(message="Không để trống số điện thoại!")
	@Size(min=0,max=10 ,message ="Số điện thoại phải là 10 số!")
	private String phone;
	
	@Column(name = "address")
	@NotBlank(message="Không để trống địa chỉ!")
	@Type(type="org.hibernate.type.StringNVarcharType")
	private String address;

	@Column(name = "is_enable")
	private Boolean is_enable;

	
}
