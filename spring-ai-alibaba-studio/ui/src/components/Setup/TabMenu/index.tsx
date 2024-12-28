/**
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

import {
  Form,
  Select,
  Slider,
  Input,
  Flex,
  InputNumber,
  Button,
  Switch
} from "antd";
import { TabValue, TabType, ItemType } from "../types";
import { useEffect } from "react";

type Props = {
  tabValue: TabValue;
  tabType: TabType;
  onChange: (config: any) => void;
};

export default function Config(props: Props) {
  const { tabValue, tabType, onChange } = props;
  const [form] = Form.useForm();

  const { TextArea } = Input;
  const makeFromItem = (item: ItemType) => {
    switch (item.type) {
      case "select":
        return <Select options={item.optinos} />;
      case "slider":
        return <Slider max={item.max} min={item.min} step={item.step} />;
      case "inputNumber":
        return <InputNumber min={item.min} max={item.max} />;
      case "switch":
        return <Switch />;
      case "input":
        return <Input placeholder={item.placeholder} />;
      case "textarea":
        return <TextArea rows={3} />;
      default:
        return <></>;
    }
  };

  const reset = () => {
    form.resetFields();
  };

  useEffect(() => {
    form.setFieldsValue(tabValue);
  }, [tabValue, form]);

  return (
    <>
      <Form
        layout="vertical"
        form={form}
        onValuesChange={(changedValues, allValues) => {
          console.log(changedValues, allValues);
          onChange(allValues);
        }}
      >
        {tabType.map((item) => {
          let field = makeFromItem(item);

          return (
            <Form.Item label={item.label} name={item.name}>
              {field}
            </Form.Item>
          );
        })}
      </Form>
      <Flex justify="center">
        <Button onClick={reset}>Reset</Button>
      </Flex>
    </>
  );
}
