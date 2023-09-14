package com.dinh.logistics.ultils;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.dinh.logistics.ultils.ApiError;

@Data
@NoArgsConstructor
public class BaseResponse{
	private int result_code;
	private String result_description;
	private ApiError error;
	private List<ApiSubError> subErrors;
	private Object data;
	
	public BaseResponse(int result_code, String result_description, ApiError error) {
		this.result_code = result_code;
		this.result_description = result_description;
		this.error = error;
	}
	public BaseResponse(int result_code, String result_description) {
		this.result_code = result_code;
		this.result_description = result_description;
	}

	public BaseResponse(int result_code, String result_description,Object data) {
		this.result_code = result_code;
		this.result_description = result_description;
		this.data = data;
	}
	
	private void addSubError(ApiSubError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }
	
	abstract class ApiSubError {

    }
}

