package com.tin.entityDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
	private Integer category_id;
	private String name;
	private Boolean is_enable;
}
