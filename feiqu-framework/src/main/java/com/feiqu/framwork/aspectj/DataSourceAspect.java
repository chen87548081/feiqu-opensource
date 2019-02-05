package com.feiqu.framwork.aspectj;


import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 多数据源处理
 * 
 * @author ruoyi
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect
{
    protected static Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);


    //这里切到你的方法目录 读写分离用的 需要 mysql主从同步 这个很麻烦 不想弄 以后数据量大了再说吧
    /*@Before("execution(* com.feiqu.system.service.*.*(..))")
    public void process(JoinPoint joinPoint) {
        String methodName=joinPoint.getSignature().getName();
        if (methodName.startsWith("get")
                ||methodName.startsWith("count")
                ||methodName.startsWith("find")
                ||methodName.startsWith("list")
                ||methodName.startsWith("select")
                ||methodName.startsWith("check")){
            DynamicDataSourceContextHolder.setDateSoureType(DataSourceType.SLAVE.name());
        }else {
            //切换dataSource
            DynamicDataSourceContextHolder.setDateSoureType(DataSourceType.MASTER.name());
        }
    }*/

    /*@Pointcut("@annotation(com.feiqu.common.annotation.DataSource)")
    public void dsPointCut()
    {

    }



    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        MethodSignature signature = (MethodSignature) point.getSignature();

        Method method = signature.getMethod();

        DataSource dataSource = method.getAnnotation(DataSource.class);

        if (ObjectUtils.allNotNull(dataSource))
        {
            DynamicDataSourceContextHolder.setDateSoureType(dataSource.value().name());
        }

        try
        {
            return point.proceed();
        }
        finally
        {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDateSoureType();
        }
    }*/
}
