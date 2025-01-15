package com.alibaba.cloud.ai.controller;

import com.alibaba.cloud.ai.api.FunctionCallingAPI;
import com.alibaba.cloud.ai.common.R;
import com.alibaba.cloud.ai.model.FunctionCalling;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@CrossOrigin
@RestController
@RequestMapping("studio/api/function-calling")
public class FunctionCallingAPIController implements FunctionCallingAPI {

	@Resource
	private ApplicationContext applicationContext;

	@Override
	public R<List<FunctionCalling>> listAllFunctionCallings() {
		List<FunctionCalling> functionCallings = new ArrayList<>();

		String[] beanNames = applicationContext.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			Object bean = applicationContext.getBean(beanName);
			Class<?> beanClass = bean.getClass();

			for (Method method : beanClass.getDeclaredMethods()) {
				if (method.isAnnotationPresent(Description.class)) {
					Description description = method.getAnnotation(Description.class);

					// Create FunctionCalling object
					FunctionCalling functionCalling = getFunctionCalling(beanName, description, method);

					functionCallings.add(functionCalling);
				}
			}
		}
		return R.success(functionCallings);
	}

	private static FunctionCalling getFunctionCalling(String beanName, Description description, Method method) {
		FunctionCalling functionCalling = new FunctionCalling();
		// 增加一个 beanName 转换函数，把后缀为 AutoConfiguration 转为 Function
		functionCalling.setApplicationName(convertBeanName(beanName));
		functionCalling.setDescription(description.value());

		// 获取方法的入参类型
		Map<String, String> inputParameters = new LinkedHashMap<>();
		for (var parameter : method.getParameters()) {
			Class<?> paramType = (Class<?>) parameter.getParameterizedType();
			inputParameters.putAll(resolveFields(paramType));
		}
		functionCalling.setInputParameters(inputParameters);

		// 获取方法的返回值类型
		Class<?> returnType = method.getReturnType();
		Map<String, String> outputParameters = new LinkedHashMap<>();

		for (Type interfaceType : returnType.getGenericInterfaces()) {
			if (interfaceType instanceof ParameterizedType parameterizedType) {
				Type rawType = parameterizedType.getRawType();

				// 判断是否是 Function 接口
				if (rawType instanceof Class<?> && Function.class.isAssignableFrom((Class<?>) rawType)) {
					Type[] typeArguments = parameterizedType.getActualTypeArguments();
					if (typeArguments.length == 2) {
						// 提取入参类型
						Class<?> inputType = (Class<?>) typeArguments[0];
						inputParameters.putAll(resolveFields(inputType));

						// 提取出参类型
						Class<?> outputType = (Class<?>) typeArguments[1];
						outputParameters.putAll(resolveFields(outputType));
					}
				}
			}
		}

		functionCalling.setOutputParameters(outputParameters);
		return functionCalling;
	}

	private static String convertBeanName(String beanName) {
		if (beanName.endsWith("AutoConfiguration")) {
			return beanName.replace("AutoConfiguration", "Function");
		}
		else if (beanName.endsWith("Configuration")) {
			return beanName.replace("Configuration", "Function");
		}
		return beanName;
	}

	private static Map<String, String> resolveFields(Class<?> clazz) {
		Map<String, String> fields = new LinkedHashMap<>();

		if (clazz.isRecord()) {
			for (RecordComponent component : clazz.getRecordComponents()) {
				fields.put(component.getName(), component.getType().getSimpleName());
			}
		}
		else {
			for (Field field : clazz.getDeclaredFields()) {
				fields.put(field.getName(), field.getType().getSimpleName());
			}
		}
		return fields;
	}

}
