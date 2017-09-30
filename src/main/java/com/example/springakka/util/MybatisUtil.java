package com.example.springakka.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ido on 2017/9/27.
 */
public class MybatisUtil {
    private  static SqlSessionFactory sqlSessionFactory;

    public static void initFactory(String resuousePath) throws IOException {
        String resource = resuousePath;
        InputStream inputStream = Resources.getResourceAsStream(resource);
         sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(inputStream);
    }

    public static SqlSessionFactory getSqlSessionFactory(){
        return sqlSessionFactory;
    }
}
