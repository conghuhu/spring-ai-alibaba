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

import { Tabs } from "antd";
import type { TabsProps, SelectProps } from "antd";
import styles from "./index.module.css";
import { SetupValue } from "./types";
import TabMenu from "./TabMenu";

type Props = {
  setupValue: SetupValue;
  onChange: (config: any) => void;
};

export default function Setup(props: Props) {
  // const { setupValue } = props;
  const { onChange } = props;

  const setupValue = [
    {
      tabName: "config",
      tabType: [
        {
          type: "select",
          options: [
            { value: "qwen-plus", label: "qwen-plus" },
            { value: "wanx-v1", label: "wanx-v1" }
          ] as SelectProps["options"]
        },
        {
          type: "InputNumber"
        }
      ],
      tabValue: []
    },
    {
      tabName: "prompt",
      tabType: [
        {
          type: "textArea"
        }
      ],
      tabValue: []
    },
    {
      tabName: "tool",
      tabType: [],
      tabValue: []
    }
  ];

  const tabItems: TabsProps["items"] = setupValue.map((item) => {
    return {
      key: item.tabName,
      label: item.tabName,
      children: (
        <TabMenu
          tabType={item.tabType}
          tabValue={item.tabValue}
          onChange={onChange}
        />
      )
    };
  });

  return (
    <div className={styles.container}>
      <Tabs defaultActiveKey="config" items={tabItems} />
    </div>
  );
}
