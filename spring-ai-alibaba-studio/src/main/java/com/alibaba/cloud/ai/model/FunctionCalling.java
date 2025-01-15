/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.ai.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Data
public class FunctionCalling {

	@Schema(description = "The name of the application associated with the function.")
	private String applicationName;

	@Schema(description = "The description of the function.")
	private String description;

	@Schema(description = "The input parameters for the function.")
	private Map<String, String> inputParameters;

	@Schema(description = "The output parameters or response of the function.")
	private Map<String, String> outputParameters;

}
