package com.mads.spring.parse;

import com.mads.spring.configbean.MadsProtocol;
import com.mads.spring.configbean.MadsReference;
import com.mads.spring.configbean.MadsRegistry;
import com.mads.spring.configbean.MadsService;
import com.mads.spring.parse.impl.MadsProtocolBeanDefinitionParse;
import com.mads.spring.parse.impl.MadsReferenceBeanDefinitionParse;
import com.mads.spring.parse.impl.MadsRegistryBeanDefinitionParse;
import com.mads.spring.parse.impl.MadsServiceBeanDefinitionParse;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/******
 * 这个类是用来注册 自定义标签的解析类的
 * 一切的起点始于此
 * @author mads
 */
public class MadsSOANamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        /*****
         * 第一个参数  是 在我们自定义的xsd文件的名字。后面是对应的 解析类，
         * XSD文件里有几个标签这里就要写几个注册O
         */
        this.registerBeanDefinitionParser("madsregistry", new MadsRegistryBeanDefinitionParse(MadsRegistry.class));
        this.registerBeanDefinitionParser("madsreference", new MadsReferenceBeanDefinitionParse(MadsReference.class));
        this.registerBeanDefinitionParser("madsprotocol", new MadsProtocolBeanDefinitionParse(MadsProtocol.class));
        this.registerBeanDefinitionParser("madsservice", new MadsServiceBeanDefinitionParse(MadsService.class));
    }
}
