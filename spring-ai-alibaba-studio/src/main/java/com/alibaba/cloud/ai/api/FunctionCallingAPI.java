package com.alibaba.cloud.ai.api;

import com.alibaba.cloud.ai.common.R;
import com.alibaba.cloud.ai.model.FunctionCalling;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface FunctionCallingAPI {

	@Operation(summary = "list all registered functions", description = "", tags = { "function-calling" })
	@GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
	default R<List<FunctionCalling>> listAllFunctionCallings() {
		return R.success(List.of());
	}

}
