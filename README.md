# java-serialization-compare

用于基于控制变量法比较 Java 中多种主流序列化方式在相同数据语义下的性能和体积差异。

## 当前已接入的序列化方式

- JDK 原生序列化
- Jackson JSON
- Jackson Smile
- Protobuf
- Kryo
- Hessian2

## 场景矩阵

项目默认使用 `4 种数据格式 × 3 种数据规模 = 12 个场景`：

- 结构化-平铺对象
- 结构化-嵌套对象
- 非结构化-文本
- 非结构化-二进制

每种格式分别覆盖：

- 小数据
- 中数据
- 大数据

## 对比指标

- 序列化字节大小
- 序列化平均耗时
- 反序列化平均耗时
- 序列化吞吐量
- 反序列化吞吐量

## 运行方式

执行测试：

```bash
mvn test
```

运行完整基准并输出报告：

```bash
mvn -DskipTests compile exec:java "-Dexec.mainClass=io.github.stellhub.serialization.compare.SerializationBenchmarkApplication"
```

完整基准运行后会在 `target/serialization-benchmark-report.md` 输出 Markdown 报告。

[运行结果](./serialization-benchmark-report.md)