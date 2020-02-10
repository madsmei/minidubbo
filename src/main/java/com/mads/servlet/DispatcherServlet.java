package com.mads.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mads.spring.configbean.MadsService;
import com.mads.util.ServerGetMethodUtil;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/******
 * Http来实现RPC调用时
 * 消费者的url是怎么直接掉到生产者对应的实现类的呢?
 *结合我们正常服务调用的习惯，我们应该在生产者这里有个类似Controller的东西来接收请求并处理吧。
 * 所以这里我们实现一个自己的Servlet来做这件事，这里就有一个问题，既然这个Dubbo的框架 生产者和消费者都要引用，
 * 在生产者端 怎么让我们自定义的这个Servlet生效呢，我们必要要在web.xml 的配置文件里 注册一下我们这个自定义的Servlet
 * 每一个生产者都要去注册，不然请求没人处理呀 是不是。这违背了一个框架的初衷吧，所以这也是 Dubbo不建议使用Http协议作为RPC的原因
 *生产者自定义Servlet的web.xml配置：
 * <servlet>
 * 		<servlet-name>madssoaservlet</servlet-name>
 * 		<servlet-class>com.mads.servlet.DispatcherServlet</servlet-class>
 * 		<load-on-startup>1</load-on-startup>
 * 	</servlet>
 * 	<servlet-mapping>
 * 		<servlet-name>madssoaservlet</servlet-name>
 * 		<url-pattern>/soa/api</url-pattern>
 * 	</servlet-mapping>
 * @author mads
 */
public class DispatcherServlet extends HttpServlet {

    /*****
     * 这里 将会接收  消费者 远程调用的 http请求
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //调用真实方法。并返回结果
        Object result = ServerGetMethodUtil.invokeMethodFromRequest(httpProcess(req, resp));

        PrintWriter pw = resp.getWriter();
        pw.write(result.toString());
    }

    /*****
     * 从请求中拿到  请求参数信息
     * @param req
     * @param resp
     * @return
     */
    private JSONObject httpProcess(HttpServletRequest req, HttpServletResponse resp) {
        StringBuffer sb = new StringBuffer();
        try {
            ServletInputStream in = req.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "utf-8"));
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }

            if (sb.toString().length() <= 0) {
                return null;
            } else {
                return JSONObject.parseObject(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
