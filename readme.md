## KWIC System (Version：1.0)

### 目标系统需求概述

<img width="504" alt="目标系统需求概述github" src="https://github.com/xiahang-bupt/KWICSystem/assets/146155357/af4861a1-969a-4af7-9ea0-8e1bccadf3cb">


- 要求系统输入还可支持Socket输入、消息中间件输入、数据库输入等
- 系统中各个模块可独立升级
- 系统达到较高的性能
- 考虑如果修改排序顺序、增加处理步骤，你的程序如何进行修改
- 具备异常处理能力，如对于空行等的处理。

### 总体结构

- KWIC：总工程文件

  - KWICSystem：KWIC处理模块，负责KWIC索引系统处理逻辑及socket服务端；

  - KWICSocktClient： socket客户端模块，负责socket客户端连接；

  - input.txt：测试输入文本；

  - output.txt：测试输出文本；

### 系统行为概述

1. 输入指令选择功能：输入 1 选择命令行输入功能，跳转到步骤 2 ；输入 2 选择文件输入功能，跳转到步骤 3 ；输入 3 选择Socket输入功能，跳转到步骤 4 ；输入 4 选择数据库输入功能，跳转到步骤 5 ；输入 5 选择消息中间件输入功能，跳转到步骤6；输入 6 退出程序。
2. 根据系统提示输入需要转换的字符串，输入完成后回车结束，系统将返回输入字符串经转换后的字符串。随后返回步骤 1 。
3. 根据系统提示输入文件路径和文件名，输入完成后回车结束，系统将文件内容转换结果输出到指定文件中。随后返回步骤 1 。
4. 服务器端开启Socket，等待客户端连接。此时远程主机运行SocketClient 程序，与SocketServer建立连接。客户端成功连接后将会得到主机IP地址以及端口号信息。在客户端输入字符串，系统将会输出经转换后的字符串。
5. 进入数据库功能界面，根据提示选择不同的功能可以完成查询数据库内容、插入字符串、删除字符串、清空数据库以及对当前数据库内容执行KWIC操作并输出操作结果，完成后可以选择返回上一级菜单，即回到步骤 1 。
6. 消息中间件输入。获取MQ的连接后，生产者根据命令行输入发送消息到消息队列，消费者从消息队列获取用户的输入后交给KWIC系统处理。

### 各模块（类）的概述

- KWICSystem模块具有面向对象架构风格的解决方案将KWIC索引系统分解为八个模块及一个Main文件：

  - Line模块：具有Line和LineStorage两个接口，Line的实现类LineImpl负责对于一行字符串的操作；LineStorage的实现类LineStorageImpl负责对于多行字符串的操作与统一存储。
  - Input模块：具有Input接口，Input接口的实现类InputImpl负责完成读取输入。
  - shift模块：具有CircularShifter接口，CircularShifter接口的实现类CircularShifterImpl负责对于存储LineStorage中的所有字符串完成其中每个单词的循环移位，并将所有结果字符串存储在LineStorage中。
  - sort模块：具有Alphabetizer接口，Alphabetizer接口的实现类AlphabetizerImpl负责按字母顺序对循环移位进行排序。
  - Output模块：具有Output接口，Output接口的实现类OutputImpl负责打印排序后所有字符串结果或者按要求将结果存储在Output.txt文件中。
  - socket模块：具有SocketServer接口，SocketServer接口的实现类SocketServerImpl负责建立Socket服务，获取远程Socket客户端的输入并返回执行结果。
  - jdbc模块：采用MyBatis框架的设计模式，具有utils（工具类）、pojo（Java数据库对象类）、mapper（数据库与Java对象的映射方法类），以及resources文件夹下的相关配置文件，最终通过JdbcFunc接口的实现类JdbcFuncImpl完成数据库输入功能的处理逻辑。
  - rabbtimq模块：基于RabbitMQ中间件的简单队列模式实现。包括RabbitMq、Consumer和Producer三部分，其中RabbitMq部分实现对本地RabbitMQ的连接，Producer用于生产消息到消息队列，Consumer用于读取消息队列的消息。
  - KWIC主控制模块：具有KWIC接口，KWIC接口的实现类KWICImpl负责控制整个软件系统的运行逻辑以及其他模块中方法的调用顺序。
  - Main文件：程序运行入口
  
- KWICSocketClient模块只包含一个Main文件，在该文件中完成客户端连接服务端，以及接收客户端的输入传给服务端。
