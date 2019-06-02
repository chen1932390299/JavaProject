preset:
maven 安装：
1.配置IDE构建的Maven存放目录(解压目录)
配置环境变量mvn -version验证环境安装
        Apache Maven 3.6.1 (d66c9c0b3152b2e69ee9bac180bb8fcc8e6af555; 2019-04-05T03:00:29+08:00)
        Maven home: C:\maven\apache-maven-3.6.1\bin\..
        Java version: 1.8.0_65, vendor: Oracle Corporation, runtime: C:\Program Files (x86)\Java\jdk1.8.0_65\jre
        Default locale: zh_CN, platform encoding: GBK
        OS name: "windows 7", version: "6.1", arch: "x86", family: "windows"

2.配置IDE的User setting file路径，修改setting配置文件
　　 配置本地仓库
　　 2 <localRepository>E:\Maven/mvn-localRepo
解压进入conf/setting.xml
找到proxy标签
配置公司内网代理:
<settings> 
   ...
   <proxies>
      <proxy>
         <id>my-proxy</id>
         <active>true</active>
         <protocol>http</protocol>
         <host>company-proxy-IP</host>
         <port>company-proxy-port</port>
         <!--
         <username></username>
         <password></password>
         <nonProxyHosts></nonProxyHosts>
         -->
      </proxy>
   </proxies>
 ...  
</settings>

镜像设置修改找到 <mirrors>标签：
<settings>
    
    <mirrors>
          <mirror>
              <id>alimaven</id>
             <name>aliyun maven</name>
             <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
             <mirrorOf>central</mirrorOf>
         </mirror>
    </mirrors>

</settings>


WIKI github 关于maven-jmeter-plugin 介绍地址:
https://github.com/jmeter-maven-plugin/jmeter-maven-plugin/wiki
使用注意事项:
1.
jmeter.results.shanhe.me.xsl 报告插件自定义开发优化的非jmeter自带，下载地址:
http://shanhe.me/download.php?file=jmeter.results.shanhe.me.xsl
2.下载一个3.3或者5.1版本Jmeter安装包解压后进入bin目录文件到projetc/src/test/jmeter目录
test下第一次默认没有的需要新建，复制如下:
jmeter.properties
saveservice.properties
upgrade.properties
system.properties
user.properties

3.jmeter.properties文件进行报文输出内容开放第539-565行修改如下：
jmeter.save.saveservice.data_type=true
jmeter.save.saveservice.label=true
jmeter.save.saveservice.response_code=true
# response_data is not currently supported for CSV output
jmeter.save.saveservice.response_data=true
# Save ResponseData for failed samples
jmeter.save.saveservice.response_data.on_error=false
jmeter.save.saveservice.response_message=true
jmeter.save.saveservice.successful=true
jmeter.save.saveservice.thread_name=true
jmeter.save.saveservice.time=true
jmeter.save.saveservice.subresults=true
jmeter.save.saveservice.assertions=true
jmeter.save.saveservice.latency=true
jmeter.save.saveservice.connect_time=true
jmeter.save.saveservice.samplerData=true
jmeter.save.saveservice.responseHeaders=true
jmeter.save.saveservice.requestHeaders=true
jmeter.save.saveservice.encoding=false
jmeter.save.saveservice.bytes=true
jmeter.save.saveservice.url=true
jmeter.save.saveservice.filename=true
jmeter.save.saveservice.hostname=true
jmeter.save.saveservice.thread_counts=true
jmeter.save.saveservice.sample_count=true
jmeter.save.saveservice.idle_time=true

其次还要注意将第528行修改数据测试结果文件保存格式为xml，因为默认是csv的:
jmeter.save.saveservice.output_format=xml

4.jemter 用户自定义properties需要配置到project/src/test/resources下，test下新建resources
文件夹:点击File>>project structure>>moudles>>点击新建resources文件夹,点击上方Test Resources，ok确定
5.pom配置以及build配置如下：

    <properties>
        <!--注解>设置编码格式utf-8</注解>-->
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <!--注解>定义并参数化result.jtl生成的目录，默认在${project.build.directory}/target目录下</注解>-->
        <jmeter.result.jtl.dir>${project.build.directory}\jmeter\results</jmeter.result.jtl.dir>
        <jmeter.result.html.dir>${project.build.directory}\jmeter\SummaryHtml</jmeter.result.html.dir>
        <ReportName>TestReport</ReportName>
    </properties>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>3.8.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>com.lazerycode.jmeter</groupId>
                <artifactId>jmeter-maven-plugin</artifactId>
                <version>2.8.0</version>
                <executions>
                    <execution>
                        <id>jmeter-tests</id>
                        <goals>
                            <goal>jmeter</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <!--注解>生成报告默认为true,csv格式，false禁用true时与resultFileFormat同时存在会覆盖其设置效果</注解-->
                    <generateReports>false</generateReports>
                    <!--注解>生成结果数据文件格式默认csv，xml则生成jtl文件</注解-->
                    <resultsFileFormat>xml</resultsFileFormat>
                </configuration>
            </plugin>
            <!--报告转换插件 -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>xml-maven-plugin</artifactId>
                <version>1.0-beta-3</version>
                <executions>
                    <execution>
                        <phase>verify</phase>
                        <goals>
                            <goal>transform</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <!--注解>详细报告转换jtl结果数据为html格式,报告模板detail-report_21.xsl</注解-->
                    <transformationSets>
                        <transformationSet>
                            <dir>${jmeter.result.jtl.dir}</dir>
                            <stylesheet>src\test\resources\jmeter-results-detail-report_21.xsl</stylesheet>
                            <outputDir>${jmeter.result.html.dir}\detailHtml</outputDir>
                            <fileMappers>
                                <fileMapper
                                        implementation="org.codehaus.plexus.components.io.filemappers.FileExtensionMapper">
                                    <targetExtension>html</targetExtension>
                                </fileMapper>
                            </fileMappers>
                        </transformationSet>
                        <!--注解>详细报告转换jtl结果数据为html格式报告模板shanhe</注解-->
                        <transformationSet>
                            <dir>${jmeter.result.jtl.dir}</dir>
                            <stylesheet>src\test\resources\jmeter.results.shanhe.me.xsl</stylesheet>
                            <outputDir>${jmeter.result.html.dir}\shaheHtml</outputDir>
                            <fileMappers>
                                <fileMapper
                                        implementation="org.codehaus.plexus.components.io.filemappers.FileExtensionMapper">
                                    <targetExtension>html</targetExtension>
                                </fileMapper>
                            </fileMappers>
                        </transformationSet>
                    </transformationSets>
                </configuration>
                <!-- 报告转换插件依赖versionXSLT 2.0 -->
                <dependencies>
                    <dependency>
                        <groupId>net.sf.saxon</groupId>
                        <artifactId>saxon</artifactId>
                        <version>8.7</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
 
 6关于mvn 使用jenkins或者windows cmd执行pom构建命令:
 mvn clean verify
 每次清理上一次构建内容
 
 7.JMeter3.0以后引入了Dashboard Reportr,3.0直接生成报告会出现中文乱码，
 用于生成HTML页面格式图形化报告的扩展模块建议使用3.3或最新版本dashboard manual generate 方式：
 jmeter配置HOME环境变量exp: D:/Jmeter5.1/bin 在压力测试结束时报告
 
     7.01.如果没有.jtl文件，运行如下命令： 
     jmeter -n -t source.jmx -l result.jtl -e -o /tmp/ResultReport 
     我们来看一下这条命令的参数： 
     -n ：以非GUI形式运行Jmeter 
     -t ：source.jmx 脚本路径 
     -l ：result.jtl 运行结果保存路径（.jtl）,此文件必须不存在 
     -e ：在脚本运行结束后生成html报告 
     -o ：用于存放html报告的目录 
     
     
     7.02.如果已经存在结果文件（.jtl）,可运行如下命令生成报告（带日志打印）基本命令格式：
     jmeter -n -t <test JMX file> -l <test log file> -e -o <Path to output folder>
     For example：
     jmeter -n -t F:\Performance\csdn.jmx -l saveLogDir -e -o ./htmlDir
     
     
     ---------------------
     Jmeter生成html报告的命令参数说明：
     -h 帮助 -> 打印出有用的信息并退出
     -n 非 GUI 模式 -> 在非GUI也就是无头headless模式下运行JMeter
     -t 测试文件 -> 要运行的 JMeter 测试脚本文件
     -l 日志文件 -> 记录结果的文件
     -r 远程执行 -> 启动all远程服务
     -R 远程执行 -> 启动指定远程服务
     -H 代理主机 -> 设置 JMeter 使用的代理主机
     -P 代理端口 -> 设置 JMeter 使用的代理主机的端口号
     -e 测试结束后，生成测试报告
     -o 指定测试报告的存放位置
     ---------------------
     
     这个报告总体分为两部分：Dashboard和Charts
     Dashboard:
        1.APDEX(Application Performance Index)：应用程序性能满意度的标准，
        范围在 0-1之间，1表示达到所有用户均满意，可以在配置文件设置。 
        2.Requests Summary: 请求的通过率(OK)与失败率(KO)，百分比显示
        3.Statistics: 数据分析，基本将 Summary Report 和 Aggrerate Report 的结果合并
        4.Errors: 错误情况，依据不同的错误类型，将所有错误结果展示。 
        5.Top 5 Errors by sampler：Top5错误信息采样
        6.
     
     
     Charts分三大块:
     时间维度信息(Over Time)、吞吐量(Thorughput)、响应时间(Response Times)
     1.Over Time 
       ●Response Times Over Time脚本运行时间内响应时间分布曲线 
       ●Response Time Percentiles Over Time (successful responses) 
       脚本运行时间内成功响应的请求，响应时间百分位 
       ●Active Threads Over Time 脚本运行时间内的活动线程分布 
       ●Bytes Throughput Over Time脚本运行时间内的吞吐量，单位是byte 
       ●Latencies Over Time脚本运行时间内毫秒级的响应延时 
       ●Connect Time Over Time脚本运行时间内平均连接时间
     2.Throughput
        ● Hits Per Second (excluding embedded resources) 每秒点击数曲线 
        ●Codes Per Second (excluding embedded resources)每秒状态码分布曲线 
        ●Transactions Per Second 每秒事物数曲线 
        ●Response Time Vs Request 响应时间中值与每秒请求数关系曲线 
        ● Latency Vs Request 延迟时间中值与每秒请求数关系曲线 
     3.Response Times
        ● Response Time Percentiles毫秒级百分位响应时间曲线 
        ● Response Time Overview 响应时间概述柱状图 
        ● Time Vs Threads 活动线程与平均响应时间变化曲线 
        
 最后关于dashboard如何自定义模板参考如下：
 https://www.jianshu.com/p/2a18d2b44db1

